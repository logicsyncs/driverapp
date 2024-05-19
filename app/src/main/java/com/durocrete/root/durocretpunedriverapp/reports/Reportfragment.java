package com.durocrete.root.durocretpunedriverapp.reports;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import static com.durocrete.root.durocretpunedriverapp.Utillity.Utility.sanitizeFilename;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.view.LayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.network.WebserviceHandler;
import com.google.gson.Gson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.BuildConfig;
import com.durocrete.root.durocretpunedriverapp.MainActivity;

import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.adapter.ImageAdapter;
import com.durocrete.root.durocretpunedriverapp.bills.Bill;
import com.durocrete.root.durocretpunedriverapp.bills.Billform;
import com.durocrete.root.durocretpunedriverapp.bills.Billistadapter;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.comman.FileUtils;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.databinding.CustomCameraOptionLayoutBinding;
import com.durocrete.root.durocretpunedriverapp.fragment.Fragmentbills;
import com.durocrete.root.durocretpunedriverapp.fragment.Fragmentreports;
import com.durocrete.root.durocretpunedriverapp.listeners.IImageAdapterListener;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.CheckInOUTModel;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;
import com.durocrete.root.durocretpunedriverapp.photoEditor.PhotoEditorActivity;
import com.durocrete.root.durocretpunedriverapp.model.Report;
import com.google.android.material.textfield.TextInputLayout;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by root on 1/7/17.
 */

public class Reportfragment extends Fragment implements IImageAdapterListener {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private boolean isSignatureAdded = false;
    private static final String TAG = "Reportfragment";
    //    private ImageAdapter imageAdapter;
    private List<Report> Allreportlist;
    ArrayList<String> imagesList = new ArrayList<>();
    MyPreferenceManager Sharedpref;
    MainActivity mainActivity;
    private com.durocrete.root.durocretpunedriverapp.adapter.Reportlistadapter reportlistadapter;
    //    private RecyclerView materiallistview;
    private EditText etxclientname;
    private Billistadapter billistadapter;
    private TextView tvsitename;
    private Long Siteselction;
    private String selectedsitename;
    private Button btn_bill_submit, btn_report_submit;
    private Reportform reportform = new Reportform();
    ArrayList<Reportform> reportformlist = new ArrayList<>();
    private SignatureView signatureView;
    //private String emptysign = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAHCAkgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+/iiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooA/9k=";
    private Button btnclear, btnsign;
    private EditText received_by, contact_name, contact_no, e_date;
    private TextInputLayout designation;
    // private Button signature;
    private String value = "";
    Button btnreports, btnbills;
    private Button capturebtn;
    private int optionSelected = -1;
    Boolean isImageForEdit = false;
    Boolean isApproveOrNot = false;
    private int editImagePos = -1;
    private RecyclerView rvImagesList;
    private AlertDialog dialogForGallery;
    public static int imageCount = 0;
    private Uri photoURI;
    private File file_profile;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private Reportfragment binding;
    private ImageAdapter imageAdapter;
    private IImageAdapterListener IImageAdapterListener;

    private Billform billform = new Billform();
    ArrayList<Billform> billformlist = new ArrayList<>();
    private List<Bill> Allbillist;
    private TextView client_Name, enquiry_id, tvx_sitename;
    private ImageView imageViewSignature;

    private RecyclerView materiallistview;

    private EditText etDate;
    // String currentDate = getCurrentDate();

    private ArrayList<String> submittedReferencesForReport = new ArrayList<>();
    private ArrayList<String> submittedReferencesForBill = new ArrayList<>();

    private String filename="";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reportfragment1, container, false);

        if (getArguments() != null) {
            submittedReferencesForReport = getArguments().getStringArrayList("submittedReferencesForReport");
            System.out.println(submittedReferencesForReport);
        }

        if (getArguments() != null) {
            submittedReferencesForBill = getArguments().getStringArrayList("submittedReferencesForBill");
            System.out.println(submittedReferencesForBill);
        }

        // Assuming the RecyclerView ID is recycler_view_images
        Initview(view);
        String currentDate = getCurrentDate();
        etDate.setText(currentDate);
        client_Name.setText(Sharedpref.getStringPreferences(MyPreferenceManager.Client_name));
        enquiry_id.setText(Sharedpref.getStringPreferences(MyPreferenceManager.Enquiry_id));
        tvx_sitename.setText(Sharedpref.getStringPreferences(MyPreferenceManager.Site_name));


        btnbills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment reportFragment = new Fragmentbills();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, reportFragment).addToBackStack(null).commit();
                btn_bill_submit.setVisibility(View.GONE);
                btn_report_submit.setVisibility(View.GONE);
            }
        });

        btnreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment reportFragment = new Fragmentreports();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, reportFragment).addToBackStack(null).commit();
                btn_bill_submit.setVisibility(View.GONE);
                btn_report_submit.setVisibility(View.VISIBLE);
            }
        });

        btn_report_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if either submittedReferencesForReport or submittedReferencesForBill has data
                if ((submittedReferencesForReport != null && !submittedReferencesForReport.isEmpty()) ||
                        (submittedReferencesForBill != null && !submittedReferencesForBill.isEmpty())) {
                    // At least one of the lists has data

                    // Check if the value is empty
                    if (value == null || value.isEmpty()) {
                        // Value is empty, show a message to the user to sign
                        Toast.makeText(getActivity(), "Please provide a signature", Toast.LENGTH_SHORT).show();
                    } else {
                        // Value is not empty, proceed with sending report data
                        sendreportdata();
                    }
                } else {
                    // None of the lists have data, show a message to the user
                    Toast.makeText(getActivity(), "Please select either a Report or a Bill", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_bill_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignaturePopup("abc");
            }
        });
        capturebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // Permission already granted, start camera intent
                    captureImageOption();
                } else {
                    // Request permission
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });

        return view;
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    private void SignaturePopup(final String sigper_name) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.signaturepopup);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signatureView = (SignatureView) dialog.findViewById(R.id.witnessSignaturePad);
        Button save = (Button) dialog.findViewById(R.id.btWitnessSave);
        Button Clear = (Button) dialog.findViewById(R.id.btWitnessClear);


        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
                value = "";
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the base64 encoded image string from the bitmap
                value = Utility.getEncoded64ImageStringFromBitmap1(signatureView.getSignatureBitmap());
                // Check if the value is not null or empty
                if (value != null && !value.isEmpty()) {
                    // Send the image to the server

                    // Dismiss the dialog
                    dialog.dismiss();

                    Log.d("tag", value);

                    // Get the contact name from the text field
                    String contactName = contact_name.getText().toString(); // Assuming contact_name is the EditText where the user types the contact name

                    // Ensure the contact name is not empty
                    if (!contactName.isEmpty()) {
                        // Get the filename from the bitmap
                         filename = Utility.getEncoded64ImageStringFromBitmap(signatureView.getSignatureBitmap(), contactName);
                        //sendSignatureImage();
                        sendImage();
                        // Get SharedPreferences instance
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                     // Edit the SharedPreferences using SharedPreferences.Editor
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                     // Put the filename data into SharedPreferences
                        editor.putString("filename", filename);

                    // Apply the changes
                        editor.apply();


                        // Log the filename
                        Log.d("tag", "Filename: " + filename);

                        // Dismiss the dialog
                        dialog.dismiss();
                        // setSignatureAdded(true);
                    } else {
                        // Inform the user that the contact name is required
                        Toast.makeText(getActivity(), "Please enter a contact name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Inform the user that a signature is required
                    Toast.makeText(getActivity(), "Please provide a signature", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void sendreportdata() {

        // Assuming reasonsList is a MaterialAutoCompleteTextView and remark is a TextInputEditText
        String Received_by = received_by.getText().toString();
        String Signature = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("filename", null);
        String Designation = designation.getEditText().getText().toString();
        String contact_Name = contact_name.getText().toString();
        String contact_No = contact_no.getText().toString();

        String status = "2";

        // Basic validation

        if (Received_by.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Received By.", Toast.LENGTH_SHORT).show();
            return;
        } else if (Designation.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Designation.", Toast.LENGTH_SHORT).show();
            return;
        } else if (contact_Name.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Contact Name.", Toast.LENGTH_SHORT).show();
            return;
        } else if (contact_No.isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Contact Number.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!contact_No.matches("[0-9]{10}")) {
            Toast.makeText(getActivity(), "Contact Number should be 10 digits and contain only numbers.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (value.isEmpty()) {
            Toast.makeText(getActivity(), "Plz Sign", Toast.LENGTH_SHORT).show();
        }

        // Create a JSON object for the data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("recieved_by", Received_by);
            jsonObject.put("designation", Designation);
            jsonObject.put("contact_name", contact_Name);
            jsonObject.put("contact_number", contact_No);
            // jsonObject.put("E_date", E_date);
            jsonObject.put("signature", Signature);
           // jsonObject.put("Capture_Img", captureImage.replace("/", "\\/"));
            jsonObject.put("status", status);
            // Add reference numbers based on availability
            if (submittedReferencesForReport != null && !submittedReferencesForReport.isEmpty()) {
                jsonObject.put("refrence_no", TextUtils.join(",", submittedReferencesForReport));
            } else if (submittedReferencesForBill != null && !submittedReferencesForBill.isEmpty()) {
                jsonObject.put("refrence_no", TextUtils.join(",", submittedReferencesForBill));
           }

            if (imagesList != null && imagesList.size() >= 2) {
                jsonObject.put("Capture_Img", imagesList.get(0));
               // jsonObject.put("Base64", getImageBase64(imagesList.get(1)));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a HashMap to store the JSON object
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("data", jsonObject);

        // Log the JSON data for debugging
        Log.d("sendSubmitData", "Sending JSON data: " + jsonObject);

        // Example POST request (details depend on your HTTP client and server API)
        // Assuming RequestHandler is your utility class for handling requests
        WebserviceHandler.makeWebservice1(false, getActivity(), Request.Method.POST, URLS.getInstance().submit_report, hashMap, CheckInOUTModel[].class, new VolleyResponseListener<CheckInOUTModel>() {
                    @Override
                    public void onResponse(CheckInOUTModel[] object) {
                        // Handle successful submission
                       // Toast.makeText(getActivity(), "Data Submitted Successfully.", Toast.LENGTH_SHORT).show();
                        if (object.length > 0) {
                            String responseMessage = object[0].getMessage(); // Assuming there's a getMessage() method in CheckInOUTModel
                            showResponseDialog(responseMessage);
                        }
                        CheckInOUTModel checkInOUTModel = (CheckInOUTModel) object[0];
                        if (checkInOUTModel != null) {
                            if (checkInOUTModel.getResult().equalsIgnoreCase("2")) {
                                sendImage();
                                //sendSignatureImage(value,"");
                            }
                        }
                        // Optionally, navigate back
                    }

                    @Override
                    public void onError(String message) {
                        // Handle errors
                        Utility.errorDialog(message, getActivity());
                    }
                }
        );

    }
    private void showResponseDialog(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Response")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the OK button click if needed
                    }
                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void Initview(View view) {
        file_profile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "profile.jpg");
        Sharedpref = new MyPreferenceManager(getActivity());
        mainActivity = (MainActivity) getActivity();
        //materiallistview = (RecyclerView) view.findViewById(R.id.rvCheckList);
        btn_bill_submit = (Button) view.findViewById(R.id.btn_bill_submit);
        btn_report_submit = (Button) view.findViewById(R.id.btn_report_submit);
        etxclientname = (EditText) view.findViewById(R.id.etxclientname);
        btnsign = (Button) view.findViewById(R.id.btnsign);
        btnreports = (Button) view.findViewById(R.id.btnreports);
        btnbills = (Button) view.findViewById(R.id.btnbills);
        tvx_sitename = (TextView) view.findViewById(R.id.tv_sitename);

        client_Name = (TextView) view.findViewById(R.id.client_Name);
        enquiry_id = (TextView) view.findViewById(R.id.tvx_enquiry_id);
        capturebtn = (Button) view.findViewById(R.id.btn_capture_image);
        rvImagesList = (RecyclerView) view.findViewById(R.id.rvImagesList);
        etDate = (EditText) view.findViewById(R.id.etDate);
        imageViewSignature = (ImageView) view.findViewById(R.id.imViewSignatuare);
        received_by = (EditText) view.findViewById(R.id.autoReceivedBy1);
        designation = (TextInputLayout) view.findViewById(R.id.spDesignation1);

        contact_name = (EditText) view.findViewById(R.id.autoContactName);
        contact_no = (EditText) view.findViewById(R.id.autoContactNo1);
        e_date = (EditText) view.findViewById(R.id.etDate);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvImagesList.setLayoutManager(gridLayoutManager);
        rvImagesList.setAdapter(imageAdapter);
        rvImagesList.setHasFixedSize(true);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Reports & Bills");

    }

    @SuppressLint("SuspiciousIndentation")
    public void onClick(View v) {
        if (v == binding.btn_report_submit) {
            if (isValidData()) {
                com.durocrete.root.durocretpunedriverapp.model.Report model = new com.durocrete.root.durocretpunedriverapp.model.Report();
                model.setImagesList(TextUtils.join(",", imagesList));
            }
        }
        if (v == binding.capturebtn) {
            if (Constants.imageCount < Constants.totalImageCount)
                captureImageOption();
            else
                // showToast("Only 2 Images you can upload.", MDToast.TYPE_INFO);
                Toast.makeText(mainActivity, "Only 2 Images you can upload.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidData() {
        if (imagesList.size() < 2) {
            // showToast("Please capture two images", MDToast.TYPE_INFO);
            Toast.makeText(mainActivity, "Please capture two images", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void captureImageOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();

        CustomCameraOptionLayoutBinding dialogLayout = DataBindingUtil.inflate(inflater, R.layout.custom_camera_option_layout, null, false);

        dialogLayout.llCamera.setOnClickListener(v -> {
            optionSelected = 0;
            onCameraClick();
            Toast.makeText(mainActivity, "Rotate your camera to click landscape photo.", Toast.LENGTH_SHORT).show();

            dialogForGallery.dismiss();
        });

        dialogLayout.llGallery.setOnClickListener(v -> {
            optionSelected = 1;
            onGalleryClick();
            //Toast.makeText(this, "Working on this functionality.", Toast.LENGTH_SHORT).show();
            dialogForGallery.dismiss();
        });

        builder.setView(dialogLayout.getRoot());

        dialogForGallery = builder.show();
        dialogForGallery.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogForGallery.create();

    }

    public void onGalleryClick() {
        if (checkPermission()) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file_profile));
            startActivityForResult(galleryIntent, Constants.GALLERY_REQUEST);
        } else {
            requestPermission();
        }

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(
                        Uri.parse(
                                String.format(
                                        "package:%s",
                                        getActivity().getApplicationContext().getPackageName()
                                )
                        )
                );
                startActivity(intent);

            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                //                os11ResultLauncher.launch(intent)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA}, Constants.PERMISSION_REQUEST_CODE);
            }
        }
    }


    private void onCameraClick() {
        if (checkPermission()) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", file_profile);

                // Add this line to grant permission to the camera app to write to the URI
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, Constants.CAMERA_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                // This block should only execute if there's a severe error preventing the camera from opening
                Toast.makeText(getActivity(), "Error opening camera. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED &&
                    result1 == PackageManager.PERMISSION_GRANTED &&
                    result2 == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String[] per, @NonNull int[] PResult) {
        super.onRequestPermissionsResult(RC, per, PResult);
        switch (RC) {
            case Constants.PERMISSION_REQUEST_CODE:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    if (optionSelected == 0) {
                        onCameraClick();
                    } else {
                        onGalleryClick();
                    }
                    Log.e("Permission Granted", "Permission Granted, Now your application can access CAMERA.");
                } else {
                    Toast.makeText(mainActivity.getApplicationContext(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Reportfragment fragment = new Reportfragment();

            if (requestCode == Constants.CAMERA_REQUEST) { //From Camera
                isImageForEdit = false;
                Uri selectedImage = Uri.fromFile(file_profile);
                passImageForEdit(selectedImage);
            } else if (requestCode == Constants.GALLERY_REQUEST) {//From Gallery
                Uri selectedImage = data.getData();
                isImageForEdit = false;
                passImageForEdit(selectedImage);
            } else if (requestCode == Constants.IMAGE_EDIT) {
                rvImagesList.setVisibility(View.VISIBLE);
                String imagePath = data.getStringExtra("savedImagePath");
                String imageAbPath = data.getStringExtra("imageAbPath");
                setImageCount();
                Log.e(TAG, "onActivityResult: " + imagePath);

                if (!isImageForEdit) {
                    //showSnackbar("Image Saved Successfully");
                    if (imagesList != null) {
                        imagesList.clear();
                    }
                    imagesList.add(imagePath);
                    imagesList.add(imageAbPath);

                    displaySignatureImage(imagePath);
                    // imageAdapter.addData(imagePath);
                    // imageAdapter.notifyDataSetChanged();
                } else {
                    //showSnackbar("Image Updated Successfully");
                    //imageAdapter.notifyDataSetChanged();
                    imageAdapter.notifyItemChanged(editImagePos);

                }

            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Canceled by user", Toast.LENGTH_SHORT).show();
        }

    }

    private void displaySignatureImage(String imagePath) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + File.separator + "DUROCRETE" + File.separator + imagePath
        );

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageViewSignature.setVisibility(View.VISIBLE);
        imageViewSignature.setImageBitmap(bitmap);

        // getActivity().getSupportFragmentManager().popBackStack("unique_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Reportfragment fragment = new Reportfragment();
        Bundle args = new Bundle();
        args.putString("imagePath", imagePath);
        fragment.setArguments(args);

    }

    private void setImageCount() {
        imageCount++;
        String imageCount = "Captured Images(" + Constants.imageCount + "/" + Constants.totalImageCount + ")";
        capturebtn.setText(imageCount);

    }

    private void passImageForEdit(Uri selectedImage) {

        Intent i = new Intent(getActivity(), PhotoEditorActivity.class);
        i.putExtra("photoEdit", selectedImage.toString());
        i.putExtra("isImageForEdit", isImageForEdit);
        startActivityForResult(i, Constants.IMAGE_EDIT);
    }

    @Override
    public void editImage(int pos) {
        editImagePos = pos;
        isImageForEdit = true;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + "DUROCRETE" + File.separator + imagesList.get(pos));

        Uri selectedImage = Uri.fromFile(file);
        passImageForEdit(selectedImage);
    }

    @Override
    public void deleteImage(int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete Image");
        builder.setMessage("Are you sure want to delete this Image?");
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {

            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + File.separator + "DUROCRETE" + File.separator + imagesList.get(pos);

            if (FileUtils.fileExist(fileName)) {
                boolean isDeleted = FileUtils.deleteFile(fileName);
                if (isDeleted) {
                    //showToast("Image Deleted", MDToast.TYPE_INFO);
                    Toast.makeText(getActivity(), "Canceled by user", Toast.LENGTH_SHORT).show();
                    imagesList.remove(pos);
                    //imageAdapter.removeData(pos);
                    Constants.imageCount--;
                    setImageCount();
                    if (imagesList.size() == 0) {
                        rvImagesList.setVisibility(View.GONE);
                    }

                } else {
                    //showToast("Image not Deleted.Something is wrong", MDToast.TYPE_WARNING);
                    Toast.makeText(getActivity(), "Image not Deleted.Something is wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                //showToast("Image does not Exist.", MDToast.TYPE_WARNING);
                Toast.makeText(getActivity(), "Image does not Exist.", Toast.LENGTH_SHORT).show();
            }

        });

        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> {

        });

        AlertDialog dialog = builder.show();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Report And Bills")
                .setMessage("Do you want to exit from Report And Bills")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public MainActivity getMyActivity() {
        return (MainActivity) getActivity();
    }


    public String getImageBase64(String imageUrl) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(imageUrl); // You can get an inputStream using any I/O API
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encodedString;

    }


    public void sendImage()
    {
        final JSONArray jsonArray = new JSONArray();
        try {

            JSONObject jsonObject = new JSONObject();
            if (imagesList != null && imagesList.size() >= 2) {
                jsonObject.put("name", imagesList.get(0));
                jsonObject.put("Base64", getImageBase64(imagesList.get(1)));
                jsonArray.put(jsonObject);
            }
            else if (filename != null) {
                jsonObject.put("name", filename);
                jsonObject.put("Base64", value);
                jsonArray.put(jsonObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // IMAGE_URL = "http://logicsync.in/PuneDuro/upload_image_test.php";


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("upload_image", jsonArray.toString());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://logicsync.in/PuneDuro/upload_image_test2.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Check if the response is not null and has a length greater than 0
                        if (response != null && response.length() > 0) {
                            try {
                                // Parse the response as JSON
                                JSONObject jsonResponse = new JSONObject(response);
                                // Extract the message from the JSON response
                                String responseMessage = jsonResponse.getString("message"); // Adjust the key if needed
                                // Show the response message in a dialog
                                showResponseDialog(responseMessage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle the JSON parsing error
                                showResponseDialog("Response parsing error");
                            }
                        } else {
                            // Handle the case where the response is null or empty
                            showResponseDialog("No response received");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null && error.getMessage() != null) {
                    Log.e("VolleyError", error.getMessage());
                } else {
                    Log.e("VolleyError", "Unknown error occurred.");
                }
                // Handle error
                //Log.d("Image Upload error: ",error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Pass your data here
                return hashMap;
            }
        };

        queue.add(stringRequest);
    }

}
