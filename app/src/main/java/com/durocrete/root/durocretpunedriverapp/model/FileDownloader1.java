package com.durocrete.root.durocretpunedriverapp.model;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.durocrete.root.durocretpunedriverapp.PDFViewerActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


public class FileDownloader1 extends AsyncTask<String, Integer, File> {

    private static final int MEGABYTE = 1024 * 1024;
    ProgressDialog mProgressDialog;
    int id = 1;
    private Activity context;
    //private NotificationManager mNotifyManager;
    // private NotificationCompat.Builder build;
    private File directory , folder;

    public FileDownloader1(Activity context ,File directory , File folder) {
        this.context = context;
        //mNotifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //build = new NotificationCompat.Builder(context);
        this.directory = directory;
        this.folder = folder;

    }

    public void downloadFile(String fileUrl, File directory) {
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {

                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected File doInBackground(String... strings) {
        String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
//        String fileName = strings[1];  // -> maven.pdf
//
//        String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//
//        File folderDownload = new File(extStorageDirectory);
//        folderDownload.mkdir();
//        File folder = new File(extStorageDirectory, "VaaJobs");
//
//        folder.mkdir();
//
//        File directory = new File(folder, fileName);
//        Log.v("www directory ", directory.toString());
//
//        if (directory.exists()) {
//            Log.v("www", "is presenet");
//            File file = new File(folder.getAbsolutePath());
//            Log.v("www file : ", file.toString());
//
//            if (directory.toString().contains("pdf")) {
//                Log.v("www", "pdf");
//                Uri path = Uri.fromFile(directory);
//                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                pdfIntent.setDataAndType(path, "application/pdf");
//                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(pdfIntent);
//            } else {
//                Log.v("www", "others");
//                Log.v("www fileUrl : ", fileUrl);
//                WebViewFragment fragment = new WebViewFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("currentURL", fileUrl);
//                fragment.setArguments(bundle);
//                ((UserDashboardActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.rlUserDashboardContainer, fragment).addToBackStack(null).commit();
//
//            }
//        } else {
           /* Log.v("www", "is not presenet");
            try {
                directory.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        /*for Notification*/
           /* int i;
            for (i = 0; i <= 100; i += 5) {
                // Sets the progress indicator completion percentage
                publishProgress(Math.min(i, 100));
                try {
                    // Sleep for 5 seconds
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    Log.d("Failure", "sleeping failure");
                }
            }*/
        //*****************//
//        FileDownloader.downloadFile(fileUrl, directory);
        //start download file and save in folder
           /* try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int lenghtOfFile = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                long total = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    total += bufferLength;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
//                    publishProgress((int) ((total * 100) / lenghtOfFile));
                    fileOutputStream.write(buffer, 0, bufferLength);
                }*/


//                Log.v("AAA : ", " filURL : " + fileUrl);
//                Log.v("AAA : ", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/VaaJobs/");
//                Log.v("AAA :", "folder :" + folder);
//                Log.v("AAA :", "directory :" + directory);
//
//
        //File file = new File(folder.getAbsolutePath());

        // Log.v("AAA file : ", file.toString());

        //Uri apkURI = Utility.getUriFromFile(context,directory);


        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        //intent.setDataAndType(Uri.fromFile(file), "*/*");
//            context.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

        // PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        //build.setContentIntent(pIntent);

        // fileOutputStream.close();
           /* } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        //end download file and save in folder
//        }

        //return null;
        File file=directory;
        try {
            URL aURL = new URL(fileUrl);
            URLConnection conn = aURL.openConnection();
            conn.connect();//CR#2021-Changes to handle Android 10 & Above
            //File myDir = new File(folder.getAbsolutePath());//chknull(rootPath,Utility.getBaseDirectory(context,"APK").getAbsolutePath()));//Environment.getExternalStorageDirectory()+File.separator+"Download"+File.separator+"Echallan"));
            //myDir.mkdirs();
            // file = new File(myDir,directory.getName());// chknull(docName,url[0].substring(url[0].lastIndexOf("/")+1)));
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while((read=bis.read(buffer))!=-1)
                out.write(buffer,0,read);
            bis.close();
            out.flush();
            out.close();
        }
        catch (IOException e) { e.printStackTrace();}
        return file;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please wait, Download in progress.");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        /*for Notification*/
        //build.setContentTitle("Download")
        //   .setContentText("Please wait, Download in progress.")
        //  .setSmallIcon(R.drawable.carat_bottom);
        //build.setProgress(100, 0, false);
        //mNotifyManager.notify(id, build.build());
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressDialog.setProgress(values[0]);
        /*for Notification*/
        //build.setProgress(100, values[0], false);
        //mNotifyManager.notify(id, build.build());

    }

    @Override
    protected void onPostExecute(final File file) {
        super.onPostExecute(file);
        mProgressDialog.dismiss();

        /*for Notification*/
        //build.setContentText("Click here to open.");
        // Removes the progress bar
        //build.setProgress(0, 0, false);
        //Toast.makeText(context ,"Download Completed, Click again to open it.",Toast.LENGTH_LONG).show();
        //mNotifyManager.notify(id, build.build());
        if(file!=null && file.exists() && file.length()>0){
            Log.e("Download path",file.getAbsolutePath());

            Intent viewPDF= new Intent(context, PDFViewerActivity.class);
            viewPDF.putExtra("filePath",directory.getAbsolutePath());
            context.startActivity(viewPDF);

            //Intent intent = new Intent(Intent.ACTION_VIEW);
            // intent.setDataAndType(Utility.getUriFromFile(context,file), "application/pdf");
            //intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);

            Toast.makeText(context ,"Download Completed",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(context ,"Unable to Download File!",Toast.LENGTH_LONG).show();
    }
}
