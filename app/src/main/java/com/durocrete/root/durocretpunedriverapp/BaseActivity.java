package com.durocrete.root.durocretpunedriverapp;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import static android.graphics.Typeface.BOLD;

import com.google.android.material.snackbar.Snackbar;



public abstract class BaseActivity extends AppCompatActivity {


    public AlertDialog progressDialog;

    //ProgressDialogUtils progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    public void spannableString(int colorId, TextView tv, int startCount, int endCount, String text) {
        //String textTurfOwnerRegister = "Turf Owners Register";
        Spannable spannableTurfOwner = new SpannableString(text);

        spannableTurfOwner.setSpan(
                new ForegroundColorSpan(colorId),
                startCount, endCount,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTurfOwner.setSpan(
                new StyleSpan(BOLD),
                startCount, endCount,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableTurfOwner);

    }

    protected void showSnackbar(@NonNull String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}

