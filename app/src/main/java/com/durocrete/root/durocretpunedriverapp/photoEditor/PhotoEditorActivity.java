package com.durocrete.root.durocretpunedriverapp.photoEditor;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.durocrete.root.durocretpunedriverapp.fragment.reportFragment;
import com.durocrete.root.durocretpunedriverapp.reports.Reportfragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.durocrete.root.durocretpunedriverapp.BaseActivity;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.databinding.ActivityPhotoEditorBinding;
import com.durocrete.root.durocretpunedriverapp.photoEditor.listener.ICompressImageListener;
import com.durocrete.root.durocretpunedriverapp.photoEditor.listener.OnItemSelected;
import com.durocrete.root.durocretpunedriverapp.photoEditor.listener.Properties;
import com.durocrete.root.durocretpunedriverapp.photoEditor.tools.EditingToolsAdapter;
import com.durocrete.root.durocretpunedriverapp.photoEditor.tools.ToolType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;

import ja.burhanrashid52.photoeditor.PhotoEditor;
//import ja.burhanrashid52.photoeditor.SaveSettings;
//import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;

import androidx.databinding.DataBindingUtil;

import org.jetbrains.annotations.NotNull;


public class PhotoEditorActivity extends BaseActivity implements OnPhotoEditorListener ,OnItemSelected,Properties ,ICompressImageListener {
    private ActivityPhotoEditorBinding binding;
    private PhotoEditor mPhotoEditor;
    private PropertiesBSFragment mPropertiesBSFragment;
    private EditingToolsAdapter mEditingToolsAdapter;
    private ICompressImageListener listener;
    private String imageName;
    Uri uri;
    private boolean isImageForEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_editor);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_photo_editor);
        Intent i = getIntent();
        listener = this;
        uri = Uri.parse(i.getStringExtra("photoEdit"));
        isImageForEdit = i.getBooleanExtra("isImageForEdit", false);
        binding.photoEditorView.getSource().setImageURI(uri);
        mEditingToolsAdapter = new EditingToolsAdapter(this);
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);


        mPhotoEditor = new PhotoEditor.Builder(this, binding.photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .build();
        setBrushDefaultValues();

        mPropertiesBSFragment = new PropertiesBSFragment();

        mPropertiesBSFragment.setPropertiesChangeListener(this);
        mPhotoEditor.setOnPhotoEditorListener(this);
        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvConstraintTools.setLayoutManager(llmTools);
        binding.rvConstraintTools.setAdapter(mEditingToolsAdapter);

    }

    private void setBrushDefaultValues() {
        mPhotoEditor.setBrushDrawingMode(true);
        mPhotoEditor.setBrushSize(12f);
        mPhotoEditor.setBrushColor(ContextCompat.getColor(getApplicationContext(), R.color.red_color_picker));

    }

    @Override
    public void onToolSelected(@NotNull ToolType toolType) {
        switch (toolType) {
            case SAVE:
                alertDialog();
                break;
            case BRUSH:
                setBrushDefaultValues();
                showBottomSheetDialogFragment(mPropertiesBSFragment);
                break;
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode) {
                        mPhotoEditor.addText(inputText, colorCode);
                    }

                });
                break;
            case ERASER:
                mPhotoEditor.brushEraser();
                // mTxtCurrentTool.setText(R.string.label_eraser_mode);
                break;
            case UNDO:
                mPhotoEditor.undo();
                break;
            case REDO:
                mPhotoEditor.redo();
                break;

        }

    }

    private void showBottomSheetDialogFragment(BottomSheetDialogFragment fragment) {
        if (fragment == null || fragment.isAdded()) {
            return;
        }
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                mPhotoEditor.editText(rootView, inputText, colorCode); // Pass the color code directly
            }
        });
    }


    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

//    @Override
//    public void onRemoveViewListener(int numberOfAddedViews) {
//
//    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
    }
    private Bitmap getBitmapFromView(PhotoEditorView photoEditorView) {
        Bitmap bitmap = Bitmap.createBitmap(photoEditorView.getWidth(), photoEditorView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        photoEditorView.draw(canvas);
        return bitmap;
    }



    private void saveImage() {
        progressDialog = new ProgressDialog(this); // or any other appropriate constructor
        progressDialog.setMessage("Saving Image...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        File image;
        if (isImageForEdit) {
            imageName = uri.getPath();
            image = new File(imageName);
        } else {
            imageName = "IMG_" + (System.currentTimeMillis()) + ".png";

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + File.separator + "DUROCRETE" + File.separator);
            if (!file.exists()) {
                file.mkdirs();
            }
            image = new File(file, imageName);
        }

        String currentPhotoPath = image.getAbsolutePath();

        Bitmap bitmap = getBitmapFromView(binding.photoEditorView); // Retrieve the edited bitmap from the PhotoEditorView
        if (bitmap != null) {
            try {
                FileOutputStream outputStream = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                // Image saved successfully
                progressDialog.dismiss();
                new ImageCompression(getApplicationContext(), listener).execute(currentPhotoPath);

            } catch (IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                showSnackbar("Failed to save Image");
                failedDataToResult();
            }
        } else {
            progressDialog.dismiss();
            showSnackbar("Failed to save Image");
            failedDataToResult();
        }
    }

    private void failedDataToResult() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Intent intent = new Intent();
        intent.putExtra("imageAbPath",imagePath);
        intent.putExtra("savedImagePath", imageName);
            setResult(Activity.RESULT_OK, intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        alertDialog();
    }

    private void alertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.info);
        alertDialog.setMessage(R.string.are_you_sure_want_to_save_image);
        alertDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        saveImage();
                    }
                });
        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        failedDataToResult();
                    }
                });
        alertDialog.show();
    }
//@Override
//public void onBackPressed() {
//    alertDialog();
//}
//
//    private void alertDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Exit");
//        builder.setMessage("Are you sure you want to exit?");
//        builder.setPositiveButton("Yes", (dialog, which) -> {
//            // If user confirms to exit, start a new activity with the desired layout
//            Intent intent = new Intent(PhotoEditorActivity.this, Reportfragment.class);
//            startActivity(intent);
//            finish(); // Finish the current activity
//        });
//        builder.setNegativeButton("No", (dialog, which) -> {
//            // If user cancels, dismiss the dialog
//            dialog.dismiss();
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    @Override
    public void imageProcessed(@NotNull String imagePath) {
        galleryAddPic(imagePath);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
