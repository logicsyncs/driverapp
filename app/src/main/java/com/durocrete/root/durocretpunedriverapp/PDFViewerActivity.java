package com.durocrete.root.durocretpunedriverapp;

import static com.durocrete.root.durocretpunedriverapp.Utillity.Utility.chkNull;

import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.comman.IConstants;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewerActivity extends AppCompatActivity {


    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);


        final PDFView pdfView = findViewById(R.id.pdfView);
        if (getIntent() != null && getIntent().hasExtra("filePath") && chkNull(getIntent().getStringExtra("filePath"), "").length() > 0) {
            filePath = (getIntent().getStringExtra("filePath"));
            Utility.showLog(IConstants.TAG, "filePath" + filePath);
            final File pdfFile = new File(filePath);
            pdfView.fromFile(pdfFile).load();
           // pdfFileShare=new File(Environment.getExternalStorageDirectory(), MySharedPreference.url);
            // pdfFileShare=new File(pdfFile.getAbsolutePath());
            // System.out.println(pdfFileShare);
        } else finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {

            sharePdf();
        }
        return super.onOptionsItemSelected(item);
    }

    public void sharePdf(){

        File file=new File(filePath);
        if(!file.exists()){
            Toast.makeText(this,"file doesn't exist",Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("application/pdf");
                Uri test = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.putExtra(Intent.EXTRA_STREAM, test);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent chooserIntent = Intent.createChooser(intent, "Select App to share file");
                startActivity(chooserIntent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}