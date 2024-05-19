package com.durocrete.root.durocretpunedriverapp.fragment;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.MainActivity;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class reportFragment extends Fragment {

    private List<Report> Allreportlist;
//    private ArrayAdapter<Report> reportlistadapter;
    MyPreferenceManager Sharedpref;
    private ListView materiallistview;
    private TextView Clientname;
    private TextView sitename;
    private EditText contact_name;
    private EditText contact_number;
    private Button btnplaceorder;
    private Spinner spAllsiteslist;
    private Long Siteselction;
    private String selectedsitename;
    private RecyclerView reportrecycleview;
    private Button btnsubmit;
    private Reportform reportform = new Reportform();
    ArrayList<Reportform> reportformlist = new ArrayList<>();
    private EditText etxclientname,etxclientno;
    String value = "";
    private Reportlistadapter reportlistadapter;
    MainActivity mainActivity;

//    private TextView  tvx_enquiry_id;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

       // tvx_enquiry_id.setText(Sharedpref.getStringPreferences(MyPreferenceManager.Enquiry_id));

        Initview(view);

        Fetchreportlist();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //senddata();
            }
        });

        return view;
    }

//    private void senddata() {
//
//        reportformlist.clear();
//
//        ArrayList<Report> selectedItems = new ArrayList<>();
//        for (int i = 0; i < Allreportlist.size(); i++) {
//            if (reportlistadapter.Alltestlist().get(i).ischecked(true)) {
//                selectedItems.add(reportlistadapter.Alltestlist().get(i));
//
//            }
//        }
//
//        if (etxclientname.getText().toString().trim().length() == 0) {
//            Toast.makeText(getActivity(), "Please Enter Contact Person name", Toast.LENGTH_SHORT).show();
//        }else if (etxclientno.getText().toString().trim().length() == 0) {
//            Toast.makeText(getActivity(), "Please Enter Contact Person number", Toast.LENGTH_SHORT).show();
//        }
//        else if (value.equalsIgnoreCase("null")) {
//            Toast.makeText(getActivity(), "Plz Enter Sign", Toast.LENGTH_SHORT).show();
//        } else if (Allreportlist.size() == 0) {
//            Toast.makeText(getActivity(), "Reports Not available", Toast.LENGTH_SHORT).show();
//        } else if (selectedItems.size() == 0) {
//            Toast.makeText(mainActivity, "Please select Reports.", Toast.LENGTH_SHORT).show();
//        } else {
//
//            reportform.setUser_id(Sharedpref.getStringPreferences(MyPreferenceManager.Username));
//            reportform.setReports(selectedItems);
//            reportform.setContact_person(etxclientname.getText().toString().trim());
//            reportform.setSignature(value);
//            reportform.setContact_person_no(etxclientno.getText().toString().trim());
//            reportformlist.add(reportform);
//
//
//            Gson gson = new Gson();
//            JsonElement element = gson.toJsonTree(reportformlist, new TypeToken<List<Reportform>>() {
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
//                CallWebservice.getWebservice(true, mainActivity, Request.Method.POST, IUrls.submit_report, hashMap, new VolleyResponseListener<Login>() {
//
//                    @Override
//                    public void onResponse(Report[] object) {
//                        Toast.makeText(mainActivity, "Successfully submitted Reports.", Toast.LENGTH_SHORT).show();
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

    private void Fetchreportlist() {
        HashMap<String, String> param = new HashMap<>();
        param.put("", "");

        Allreportlist = new ArrayList<>();
        RequestHandler.makeWebservice(true, getActivity(), Request.Method.POST, URLS.getInstance().get_reports, param, Report[].class, new VolleyResponseListener<Report>() {
                    @Override
                    public void onResponse(Report[] object) {
                        if (object[0] instanceof Report) {
                            for (Report materialobject : object) {
                                Allreportlist.add(materialobject);
                            }
                        }
                    }


                    @Override
                    public void onError(String message) {
                        Utility.errorDialog(message, getActivity());

                    }
                }
        );

    }


    private void Initview(View view) {
        Sharedpref = new MyPreferenceManager(getActivity());
       // reportrecycleview = (RecyclerView) view.findViewById(R.id.lvbilllist);
        btnsubmit = (Button) view.findViewById(R.id.btn_submit);
       // tvx_enquiry_id = (TextView) view.findViewById(R.id.tvx_enquiry_id);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Report  ");
    }


}

