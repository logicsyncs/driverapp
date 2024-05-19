package com.durocrete.root.durocretpunedriverapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;

import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.SharedPreference;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.CheckInOUTModel;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = LoginActivity.class.getSimpleName();
    private Activity mActivity;
    private EditText edtPassword, etDriverLogin;
    private ImageView imgShowPassword;
    private Button btnLogin;
    MyPreferenceManager sharedPref;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = LoginActivity.this;
        sharedPref = new MyPreferenceManager(this);


        if (sharedPref.getBooleanPreferences(MyPreferenceManager.Loggedin)) {
            startActivity(new Intent(mActivity, SiteSelectionActivity.class));
            mActivity.finish();
        }

//
//        if (!SharedPreference.getInstanceProfileData(mActivity).getUserId().equals("")) {
//            Log.v(TAG, "User are login before");
//            startActivity(new Intent(mActivity, RouteNSiteSelectionActivity.class));
//            finish();
//        }

        setContentView(R.layout.loginscreen);
        initializeUI();
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");

      *//*  if (!SharedPreference.getInstanceProfileData(getActivity()).getUserId().equals("")) {
            Log.v(TAG, "User are login before");
            getActivity().startActivity(new Intent(getActivity(), RouteNSiteSelectionActivity.class));
            return null;
        } else {*//*
            View view = inflater.inflate(R.layout.activity_login, container, false);

            etDriverLogin = (EditText) view.findViewById(R.id.et_Driver_login);

            edtPassword = (EditText) view.findViewById(R.id.edt_password);

            imgShowPassword = (ImageView) view.findViewById(R.id.img_show_password);

            btnLogin = (Button) view.findViewById(R.id.btn_login);

            imgShowPassword.setOnClickListener(this);
            btnLogin.setOnClickListener(this);
            *//*return view;
        }*//*
    }*/

    private void initializeUI() {
        etDriverLogin = (TextInputEditText) findViewById(R.id.etxuserID);
        edtPassword = (TextInputEditText) findViewById(R.id.tiedPassword);

        btnLogin = (Button) findViewById(R.id.btnlogin);

        btnLogin.setOnClickListener(this);
    }

    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.btnlogin:
//                if (etDriverLogin.getText().toString().equals("")) {
//                    Utility.errorDialog(getResources().getString(R.string.enter_driver_id), mActivity);
//                }
//                else
//                    loginUser(etDriverLogin.getText().toString().trim());
//                break;
//
//        }
//
//    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnlogin:
                String loginId = etDriverLogin.getText().toString().trim();
                String password = edtPassword.getText().toString().trim(); // Retrieve password from EditText field

                if (loginId.isEmpty()) {
                    Utility.errorDialog(getResources().getString(R.string.enter_driver_id), mActivity);
                } else if (password.isEmpty()) {
                    Utility.errorDialog(getResources().getString(R.string.enter_driver_password), mActivity);
                } else {
                    loginUser(loginId, password); // Pass both loginId and password to loginUser method
                }
                break;
        }
    }




    /* Used for loginUser Action */
    private void loginUser(final String loginId,final String password) {

        if (loginId == null || password== null) {
            Toast.makeText(this, "Invalid Driver ID or password", Toast.LENGTH_SHORT).show();
        } else {
            Log.v(TAG, " URLS.getInstance().loginDriveURL : " + URLS.getInstance().loginDriveURL + loginId + password);
            RequestHandler.makeWebservice(true, mActivity, Request.Method.GET, URLS.getInstance().loginDriveURL + loginId+"&password="+ password, null, CheckInOUTModel[].class, new VolleyResponseListener<CheckInOUTModel>() {
                @Override
                public void onResponse(CheckInOUTModel[] object) {
                    if (object[0] instanceof CheckInOUTModel) {
                        Log.v(TAG, "onResponse : " + object[0].getResult());

                        SharedPreference.getInstanceProfileData(mActivity).setUserId(object[0].getResult());
                        startActivity(new Intent(mActivity, SiteSelectionActivity.class));
                        sharedPref.setStringPreferences(MyPreferenceManager.UserId,object[0].getResult());
                        sharedPref.setStringPreferences(MyPreferenceManager.Username,loginId);
                        sharedPref.setBooleanPreferences(MyPreferenceManager.Loggedin,true);
                        mActivity.finish();
                    }
                }

                @Override
                public void onError(String message) {
                    Utility.errorDialog(message, mActivity);
                }
            });

        }
    }

    /*show text for some time */
    private void showPassword() {
        edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }, 3000);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            mActivity.finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(LoginActivity.this, "Double click to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }
}
