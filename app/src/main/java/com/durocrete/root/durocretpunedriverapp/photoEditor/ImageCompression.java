package com.durocrete.root.durocretpunedriverapp.photoEditor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;


import com.durocrete.root.durocretpunedriverapp.photoEditor.listener.ICompressImageListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageCompression extends AsyncTask<String, Void, String> {

    private static final String TAG = "ImageCompression";
    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;
    private Context context;
    private ICompressImageListener listener;


    public ImageCompression(Context context, ICompressImageListener onSaveListener) {
        this.context = context;
        listener = onSaveListener;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {
        if (strings.length == 0 || strings[0] == null)
            return null;

        // return compressImage(strings[0]);
        return drawTextToBitmap(context,strings[0]);
    }

    protected void onPostExecute(String imagePath) {
        Log.e(TAG, "onPostExecute: " + imagePath);
        listener.imageProcessed(imagePath);

        // imagePath is path of new compressed image.
    }

    public String compressImage(String imagePath) {
        SimpleDateFormat df11 = new SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.ENGLISH);
        String timestamp = df11.format(new Date());

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (Exception e) {
            e.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Paint paint = new Paint();

        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
       /* paint.setTextSize(50);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(24); // Text Size*/
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern

        Rect textRect = new Rect();
        paint.getTextBounds(timestamp, 0, timestamp.length(), textRect);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, paint);
        //canvas.drawText(timestamp, 9, 40, paint);
        Paint mTxtPaint = new Paint();
        Paint.FontMetrics fm1 = new Paint.FontMetrics();
        mTxtPaint.setColor(Color.WHITE);
        mTxtPaint.setTextSize(30.0f);

        mTxtPaint.getFontMetrics(fm1);

        int margin1 = 5;

        canvas.drawRect(30 - margin1, 30 + fm1.top - margin1,
                30 + mTxtPaint.measureText(timestamp) + margin1, 30 + fm1.bottom
                        + margin1, mTxtPaint);

        mTxtPaint.setColor(Color.RED);

        canvas.drawText(timestamp, 30, 30, mTxtPaint);
        /*Matrix matrix1 = new Matrix();
        canvas.drawBitmap(bmp, matrix1, paint);*/
        if (bmp != null) {
            bmp.recycle();
        }

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }

            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filepath = getFilename(imagePath);
        try {
            out = new FileOutputStream(filepath);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filepath;
    }


    public String drawTextToBitmap(Context gContext,
                                   String imagePath
    ) {
        Bitmap bitmap=null;
        SimpleDateFormat df11 = new SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.ENGLISH);
        String timestamp = df11.format(new Date());




        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];




        try {
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }

        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Path mPath = new Path();
        RectF mRectF = new RectF(20, 20, 240, 240);
        // mPath.addRect(mRectF, Path.Direction.CCW);
        paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, paint);

        paint.setColor(Color.WHITE);
        canvas.drawRect(30, 30,350, 70, paint);//// removed this for KSA


        paint.setColor(Color.RED);
        // paint.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/DS-DIGI.TTF"));
        paint.setTextSize((int) (14 * scale));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Rect bounds = new Rect();
        paint.getTextBounds(timestamp, 0, timestamp.length(), bounds);
       /* int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;*/
        int horizontalSpacing = 24;
        int verticalSpacing = 36;
        int x = horizontalSpacing;//(bitmap.getWidth() - bounds.width()) / 2;
        int y = bitmap.getHeight()-verticalSpacing;//(bitmap.getHeight() + bounds.height()) / 2;
        //canvas.drawText(timestamp, x, y, paint);
        canvas.drawText(timestamp, 10, 50, paint);// removed this for KSA
        // canvas.drawTextOnPath(timestamp, mPath, 0, 5, paint);









        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        FileOutputStream out = null;
        String filepath = getFilename(imagePath);
        try {
            out = new FileOutputStream(filepath);

            //write the compressed bitmap at the destination specified by filename.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filepath;
    }



    public String getFilename(String imagePath) {
       /* File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                                                + "/Android/data/"
                                                + context.getApplicationContext().getPackageName()
                                                + "/Files/Compressed");*/
       /* File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                                                + File.separator + "CQRA");*/
        File mediaStorageDir = new File(imagePath);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        //String mImageName="IMG_"+ String.valueOf(System.currentTimeMillis()) +".jpg";
        // String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ mImageName);;
        String uriString = mediaStorageDir.getAbsolutePath();
        return uriString;

    }

}
