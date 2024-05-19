package com.durocrete.root.durocretpunedriverapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.MainActivity;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.adapter.ReferenceAdapter;
import com.durocrete.root.durocretpunedriverapp.adapter.Reportlistadapter;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.CheckInOUTModel;
import com.durocrete.root.durocretpunedriverapp.model.Report;
import com.durocrete.root.durocretpunedriverapp.model.Reportform;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;

import com.durocrete.root.durocretpunedriverapp.network.CustomProgressDialog;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;
import com.durocrete.root.durocretpunedriverapp.reports.Reportfragment;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 1/7/17.
 */

public class Fragmentreports extends Fragment {

    private List<Report> Allreportlist;
    MyPreferenceManager Sharedpref;
    MainActivity mainActivity;
    private Reportlistadapter reportlistadapter;
    private RecyclerView materiallistview;
    private EditText etxclientname;
    private Button btnreport;
    private EditText etxclientno;
    private Button btnsubmit,btnreject;
    private MaterialCheckBox checkSelectAll;
    private Reportform reportform = new Reportform();
    ArrayList<Reportform> reportformlist = new ArrayList<>();
    private SignatureView signatureView;
    private String emptysign = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAHCAkgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+/iiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooA/9k=";
    private Button btnclear;
    private SiteDetailModel siteDetailModel=null;
    private TextView enquiryno;
    // Define adapter as a class-level variable
    private ReferenceAdapter adapter;
    String value = "example";
    private boolean isCheckedList = false;

    private ArrayList<String> submittedReferencesForReport=new ArrayList<>();;
    private ArrayList<String> rejectedReferencesForReport=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        if (getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL) != null) {
            siteDetailModel = (SiteDetailModel) getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL);
        }
        Initview(view);
        FetchReports();

        if (getArguments() != null) {
            rejectedReferencesForReport = getArguments().getStringArrayList("rejectedReferencesforreport");
            System.out.println(rejectedReferencesForReport);
        }

        if (getArguments() != null) {
            submittedReferencesForReport = getArguments().getStringArrayList("SubmittedReferencesForReport");
            System.out.println(submittedReferencesForReport);
        }

        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve saved references from SharedPreferences
                ArrayList<String> references = getReferencesFromSharedPreferencesReport(getActivity());

                // Create an instance of the adapter with the retrieved references
                adapter = new ReferenceAdapter(getActivity(), references);

                // Set layout manager to RecyclerView (Assuming LinearLayoutManager)
                materiallistview.setLayoutManager(new LinearLayoutManager(getActivity()));

                // Set the adapter to the RecyclerView
                materiallistview.setAdapter(adapter);

                // Call selectAll() method after initialization
                checkSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // Check all checkboxes in the adapter when "Select All" checkbox is checked
                        if (isChecked) {
                            adapter.selectAll();
                        } else {
                            // Optionally, you might want to provide a way to deselect all items
                            adapter.deselectAll();
                        }
                    }
                });

                // Set item click listener for the adapter
                adapter.setOnItemClickListener(new ReferenceAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Get the reference at the clicked position
                        String reference = adapter.getItem(position);
                        if (reference != null) {
                            // Remove the selected item from the adapter and SharedPreferences
                            adapter.removeReport(reference);
                        }
                    }
                });

            }
        });


        btnreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) { // Check if adapter is initialized
                    ArrayList<String> selectedReferences = adapter.getSelectedReferences();

                    // Check if at least one reference is selected
                    if(selectedReferences.isEmpty()) {
                        // If no reference is selected, show a message to the user
                        Toast.makeText(getContext(), "Please select at least one Report", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Check if only one report is selected
                    if (selectedReferences.size() != 1) {
                        // Display a message to the user
                        Toast.makeText(getContext(), "Only One Report Can Reject.", Toast.LENGTH_SHORT).show();
                        return; // Exit the onClick method
                    }

                    // Handle rejection logic here

                    // Print the selected references or any other relevant information
                    for (String reference : selectedReferences) {
                        Log.d("Rejection", "Report rejected: " + reference);
                        rejectedReferencesForReport.add(reference);
                        // Remove the selected report from the adapter's data source and SharedPreferences
                        adapter.removeReport(reference);
                    }

                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged(); // Update the UI
                    Fragment reportFragment = new RejectionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("rejectedReferencesForReport", rejectedReferencesForReport);
                    System.out.println(rejectedReferencesForReport);
                    reportFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, reportFragment).addToBackStack(null)
                            .commit();
                }
            }
        });



        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) { // Check if adapter is initialized
                    ArrayList<String> selectedReferences = adapter.getSelectedReferences();

                    // Check if at least one reference is selected
                    if(selectedReferences.isEmpty()) {
                        // If no reference is selected, show a message to the user
                        Toast.makeText(getContext(), "Please select at least one Report", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Handle submission logic here

                    // Print the selected references or any other relevant information
                    for (String reference : selectedReferences) {
                        Log.d("Submission", "Report submitted: " + reference);
                        submittedReferencesForReport.add(reference);
                        adapter.removeReport(reference);
                    }

                    adapter.notifyDataSetChanged(); // Update the UI
                    Fragment reportFragment = new Reportfragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("submittedReferencesForReport", submittedReferencesForReport);
                    System.out.println(submittedReferencesForReport);
                    reportFragment.setArguments(bundle);


                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, reportFragment)
                            .addToBackStack(null).commit();
                }
            }
        });

        return view;
    }

    private void FetchReports() {
        // Show the progress dialog before making the network request
        CustomProgressDialog.dismissDialog(getActivity());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", "100");

        Allreportlist = new ArrayList<>();

        //webservice for getting reports list
        RequestHandler.makeWebservice(true, getActivity(), Request.Method.GET, URLS.getInstance().get_reports, hashMap, Report[].class, new VolleyResponseListener<Report>() {
            @Override
            public void onResponse(Report[] object) {
               // Allreportlist.clear();
                Allreportlist.clear();
                if (object[0] instanceof Report) {
                    Gson gson = new Gson();
                    String reportsJson = gson.toJson(object);
                    saveReferenceToSharedPreferencesReport(getActivity(), reportsJson);
                    //getReferencesFromSharedPreferences(getActivity());
                    System.out.println(reportsJson);
                    for (Report materialobject : object) {
                        if (materialobject.getPresent().equalsIgnoreCase("yes")) {
                            Allreportlist.add(materialobject);

                        }
                    }
                }
               // saveReferenceToSharedPreferences(getActivity(), reportsJson);

                reportlistadapter = new Reportlistadapter(getActivity(), Allreportlist);

                // Notify adapter about the data change
                reportlistadapter.notifyDataSetChanged();

                // Dismiss the progress dialog after data loading is complete
                CustomProgressDialog.dismissDialog(getActivity());
            }

            @Override
            public void onError(String message) {
                Allreportlist = new ArrayList<>();
                reportlistadapter = new com.durocrete.root.durocretpunedriverapp.adapter.Reportlistadapter(getActivity(), Allreportlist);
                Utility.errorDialog("No reports Found.", getActivity());

                // Dismiss the progress dialog in case of error
                CustomProgressDialog.dismissDialog(getActivity());
            }
        });
    }


   private void saveReferenceToSharedPreferencesReport(Context context, String jsonString) {
    try {
        JSONArray jsonArray = new JSONArray(jsonString);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyReferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
       // if (materialobject.getPresent().equalsIgnoreCase("yes")) {
        // Extract and store only the "reference" values
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String reference = jsonObject.getString("reference");
            //String status = jsonObject.getString("present");
                editor.putString("reference_" + i, reference);

        }
        editor.apply();
      //  deleteReferencesFromSharedPreferences(context, jsonArray.length());

    } catch (JSONException e) {
        e.printStackTrace();
    }
}
    private void deleteReferencesFromSharedPreferences(Context context, int length) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyReferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Remove all stored references
        for (int i = 0; i < length; i++) {
            editor.remove("reference_" + i);
        }
        editor.apply();
    }


    private ArrayList<String> getReferencesFromSharedPreferencesReport(Context context) {
        ArrayList<String> references = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyReferences", Context.MODE_PRIVATE);

        // Initialize index variable
        int i = 0;

        // Retrieve "reference" values from shared preferences
        while (sharedPreferences.contains("reference_" + i)) {
            String reference = sharedPreferences.getString("reference_" + i, "");
            references.add(reference);
            System.out.println(references);

            // Increment index
            i++;
        }
        // Call the method to delete the retrieved data
        deleteReferencesFromSharedPreferences(context, i);
        return references;
    }

    private void Initview(View view) {
        Sharedpref = new MyPreferenceManager(getActivity());
        mainActivity = (MainActivity) getActivity();
        btnreport = view.findViewById(R.id.btn_reportss);
        materiallistview = (RecyclerView) view.findViewById(R.id.lvreportlist);
        btnsubmit = (Button) view.findViewById(R.id.btn_submit);
        etxclientname = (EditText) view.findViewById(R.id.etxclientname);
        checkSelectAll= (MaterialCheckBox) view.findViewById(R.id.checkSelectAll);
        etxclientno= (EditText) view.findViewById(R.id.etxclientno);
       // enquiryno=(TextView)view.findViewById(R.id.enquiryno);
        btnreject = (Button) view.findViewById(R.id.btn_reject);
        System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Reports");
    }


    public MainActivity getMyActivity() {
        return (MainActivity) getActivity();
    }
}
