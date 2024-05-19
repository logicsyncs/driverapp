package com.durocrete.root.durocretpunedriverapp.comman;


import androidx.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static boolean deleteFile(@NonNull File file) {
        boolean b = file.exists() && file.delete();
        Log.e(TAG, "deleteFile: " + file.getAbsolutePath() + " " + b);
        return b;
    }

    public static boolean deleteFile(@NonNull String fileName) {
        if (!Validator.isValid(fileName))
            return false;
        File file = new File(fileName);
        boolean b = file.delete();
        Log.e(TAG, "deleteFileName : " + fileName + " " + b);
        return b;
    }

    public static boolean fileExist(@NonNull String fileName) {
        if (!Validator.isValid(fileName))
            return false;
        File file = new File(fileName);
        boolean b = file.exists();
        Log.e(TAG, "fileExist : " + fileName + " " + b);
        return b;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}

