package com.durocrete.root.durocretpunedriverapp.Utillity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.durocrete.root.durocretpunedriverapp.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by root on 4/5/17.
 */
public class Utility {

    public static final boolean isUseAppStorage = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static Long chkNull(Long ll, Long def) {
        return ll != null ? ll : def;
    }

    public static Double chkNull(Double dd, Double def) {
        return dd != null ? dd : def;
    }

    public static Float chkNull(Float ff, Float def) {
        return ff != null ? ff : def;
    }

    public static Integer chkNull(Integer ii, Integer def) {
        return ii != null ? ii : def;
    }

    public static Boolean chkNull(Boolean bb, Boolean def) {
        return bb != null ? bb : def;
    }

    public static String chkNull(String str, String def) {
        return (str != null && str.trim().length() > 0 && !str.equalsIgnoreCase("null")) ? str : def;
    }

    public static void showLog(final String tag, final String msg){
        if(Utility.isDebugAPK) Log.e(tag, msg);
    }
    public static final boolean isDebugAPK= BuildConfig.DEBUG && BuildConfig.BUILD_TYPE.trim().equalsIgnoreCase("debug");
    public static Uri getUriFromFile(Context context, File file) {

        return isUseAppStorage || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? FileProvider.getUriForFile(context, context.getPackageName(), file) : Uri.fromFile(file);
    }

    public static void showFragment(final Class<? extends Fragment> fragmentClass, final Activity activityClass,
                                    final int frameLayoutId) {

        try {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    FragmentActivity fragmentActivity = (FragmentActivity) activityClass;
                    FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(frameLayoutId, Fragment.instantiate(fragmentActivity, fragmentClass.getCanonicalName()));
                    fragmentTransaction.commit();
                }
            }).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
    public static void noConnectionAlert(Context context) {
        AlertDialog.Builder connection_builder = new AlertDialog.Builder(context);
        connection_builder.setMessage("Please check internet connection");
        connection_builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog exit_alertDialog = connection_builder.create();
        exit_alertDialog.show();
    }


    public static String getEncoded64ImageStringFromBitmap1(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteFormat = stream.toByteArray();
            // get the base 64 string
            String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

            return imgString;
        }
        return null;
    }

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap,String contactName) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteFormat = stream.toByteArray();

            // Generate filename from contact name
           // String contactName="sayali";
            String filename = sanitizeFilename(contactName) + ".png";

            // get the base 64 strings
            String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

            // return filename and base64 encoded image string separated by a delimiter
            return filename;
        }
        return null;
    }

    public static String getFilenameFromBitmap(Bitmap bitmap, String contactName) {
        if (bitmap != null) {
            // Generate filename from contact name
            return sanitizeFilename(contactName) + ".png";
        }
        return null;
    }


    public static String sanitizeFilename(String filename) {
        // Replace characters not allowed in filenames with underscores
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }


    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity != null && activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity
                        .getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void errorDialog(String message , Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


}
