package com.durocrete.root.durocretpunedriverapp.bills;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.MainActivity;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.RejectionActivity;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.RecyclerViewHeightClass;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.Login;
import com.durocrete.root.durocretpunedriverapp.model.Report;
import com.durocrete.root.durocretpunedriverapp.network.CallWebservice;
import com.durocrete.root.durocretpunedriverapp.network.IUrls;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;
import com.durocrete.root.durocretpunedriverapp.reports.Reportform;
import com.durocrete.root.durocretpunedriverapp.reports.Reportlistadapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 1/7/17.
 */

public class Fragmentbills extends Fragment {

    private List<Bill> Allbillist;

    MyPreferenceManager Sharedpref;
    MainActivity mainActivity;
    private Reportlistadapter reportlistadapter;
    private Billistadapter billistadapter;
    private EditText etxclientname,etxclientno;
    private RecyclerView materiallistview;
    private TextView tvsitename;
    private Long Siteselction;
    private String selectedsitename;
    private Button btnsubmit;
    private Billform billform = new Billform();
    ArrayList<Billform> billformlist = new ArrayList<>();
    private SignatureView signatureView;
    private String emptysign = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAHCAkgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+/iiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooA/9k=";
    private Button btnclear, btnsign,btnReject;
    String value = "";
    private String signaturevalue = "";
    private EditText dateEditText;
    String currentDate = getCurrentDate();




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        Initview(view);
        //FetchMateriallist();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // senddata();
            }
        });

//        btnsign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SignaturePopup("abc");
//            }
//        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start RejectionActivity
                Intent intent = new Intent(getActivity(), RejectionActivity.class);

                startActivity(intent);
            }
        });


        return view;
    }

//    private void SignaturePopup(String abc) {
//
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.signaturepopup);
//        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        signatureView = (SignatureView) dialog.findViewById(R.id.witnessSignaturePad);
//        Button save = (Button) dialog.findViewById(R.id.btWitnessSave);
//        Button Clear = (Button) dialog.findViewById(R.id.btWitnessClear);
//
//
//        Clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signatureView.clearCanvas();
//                value = "";
//            }
//        });
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                value = Utility.getEncoded64ImageStringFromBitmap(signatureView.getSignatureBitmap());
//                Log.d("tag", value);
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setCancelable(false);
//        dialog.show();
//
//    }
private String getCurrentDate() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    return dateFormat.format(calendar.getTime());
}

//    private void senddata() {
//
//        billformlist.clear();
//
//        ArrayList<Bill> selectedItems = new ArrayList<Bill>();
//        for (int i = 0; i < Allbillist.size(); i++) {
//            if (billistadapter.Alltestlist().get(i).ischecked(true)) {
//                selectedItems.add(billistadapter.Alltestlist().get(i));
//
//            }
//        }
//
//        if (etxclientname.getText().toString().trim().length() == 0) {
//            Toast.makeText(getActivity(), "Please Enter Contact Person name", Toast.LENGTH_SHORT).show();
//        }
//        else if (etxclientno.getText().toString().trim().length() == 0) {
//            Toast.makeText(getActivity(), "Please Enter Contact Person number", Toast.LENGTH_SHORT).show();
//        }
//        else if (value.equalsIgnoreCase("null")) {
//            Toast.makeText(getActivity(), "Plz Enter Sign", Toast.LENGTH_SHORT).show();
//        } else if (Allbillist.size() == 0) {
//            Toast.makeText(getActivity(), "Bills Not available", Toast.LENGTH_SHORT).show();
//        } else if (selectedItems.size() == 0) {
//            Toast.makeText(mainActivity, "Please select Bills.", Toast.LENGTH_SHORT).show();
//        } else {
//
//            billform.setUser_id(Sharedpref.getStringPreferences(MyPreferenceManager.Username));
//            billform.setBill(selectedItems);
//            billform.setContact_person(etxclientname.getText().toString().trim());
//            billform.setContact_person_no(etxclientno.getText().toString().trim());
//            billform.setSignature(value);
//            dateEditText.setText(currentDate);
//
//
//            billformlist.add(billform);
//
//
//            Gson gson = new Gson();
//            JsonElement element = gson.toJsonTree(billformlist, new TypeToken<List<Reportform>>() {
//            }.getType());
//
//            JsonArray jsonArray = element.getAsJsonArray();
//            Log.d("tag1", jsonArray.toString());
//
//            if (jsonArray != null) {
//                Log.d("tag1", jsonArray.toString());
//                HashMap<String, String> hashMap = new HashMap<>();
//
//                hashMap.put("data", String.valueOf(jsonArray));
//
//                CallWebservice.getWebservice(true, mainActivity, Request.Method.POST, IUrls.submit_bill, hashMap, new VolleyResponseListener<Login>() {
//
//                    @Override
//                    public void onResponse(JSONObject object) {
//                        Toast.makeText(mainActivity, "Successfully submitted Bills.", Toast.LENGTH_SHORT).show();
//                        getActivity().getSupportFragmentManager().popBackStack();
//                    }
//
//                    @Override
//                    public void onError(String message) {
//                        Log.v("tag", message.toString());
//
//                    }
//                }, Login[].class);
//            } else {
//
//            }
//
//        }
//    }


//    private void FetchMateriallist() {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("", "");
//
//        Allbillist = new ArrayList<>();
//
//
//        RequestHandler.makeWebservice(true, getActivity(), Request.Method.GET, URLS.getInstance().get_bills, hashMap, Report[].class, new VolleyResponseListener<Bill>()  {
//            @Override
//            public void onResponse(JSONObject object) {
//
//                if (object[0] instanceof Bill) {
//                    for (Bill materialobject : object) {
//                        if (materialobject.getPresent().equalsIgnoreCase("yes")) {
//                            Allbillist.add(materialobject);
//                        }
//                    }
//                }
//
//                //changed listview as a recyclerview
//                billistadapter = new Billistadapter(getActivity(), Allbillist);
//                // materiallistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//                materiallistview.setAdapter(billistadapter);
//                RecyclerViewHeightClass.setRecyclerViewHeightBasedOnChildren(materiallistview);
//
//            }
//
//            @Override
//            public void onError(String message) {
//                Log.v("tag", message.toString());
//
//            }
//        });
//    }

    private void Initview(View view) {
        Sharedpref = new MyPreferenceManager(getActivity());
        mainActivity = (MainActivity) getActivity();
       // materiallistview = (RecyclerView) view.findViewById(R.id.);
        btnsubmit = (Button) view.findViewById(R.id.btn_submit1);
        etxclientname = (EditText) view.findViewById(R.id.etxclientname);
//        btnsign=(Button)view.findViewById(R.id.btnsign);
        btnReject = (Button)view.findViewById(R.id.btn_reject);
        etxclientno = (EditText) view.findViewById(R.id.etxclientno);
        dateEditText = view.findViewById(R.id.etDate);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Bills");
    }


    public MainActivity getMyActivity() {
        return (MainActivity) getActivity();
    }
}
