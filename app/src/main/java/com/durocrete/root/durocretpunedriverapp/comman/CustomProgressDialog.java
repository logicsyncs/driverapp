package com.durocrete.root.durocretpunedriverapp.comman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import com.durocrete.root.durocretpunedriverapp.R;


/**
 * Created by root on 6/24/16.
 */
public class CustomProgressDialog {

    static ProgressDialog dialog;

    public static void showDialog(Context context, String message) {
        dialog = new ProgressDialog(context);
        if (!dialog.isShowing()) {
            dialog.setMessage(message);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.circular_progress_dialog));
            dialog.setCancelable(false);
            dialog.show();
        }

    }

    public static void dismissDialog(Context context) {
        if (dialog.isShowing()) {
//           dialog.hide();
            dialog.dismiss();
        }

    }


    public static void showAlertDialogMessage(Context context, String title, String dialogContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(dialogContent);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
