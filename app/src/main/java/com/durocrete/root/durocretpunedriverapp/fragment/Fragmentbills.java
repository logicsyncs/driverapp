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

import com.durocrete.root.durocretpunedriverapp.adapter.BillAdapter;
import com.durocrete.root.durocretpunedriverapp.adapter.Billistadapter;
import com.durocrete.root.durocretpunedriverapp.adapter.ReferenceAdapter;
import com.durocrete.root.durocretpunedriverapp.bills.Bill;
import com.durocrete.root.durocretpunedriverapp.bills.Billform;
//import com.durocrete.root.durocretpunedriverapp.bills.Billistadapter;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.BillRejectModel;
import com.durocrete.root.durocretpunedriverapp.model.CheckInOUTModel;
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

public class Fragmentbills extends Fragment {

    private List<Bill> Allbillist;
    MyPreferenceManager Sharedpref;
    MainActivity mainActivity;
    private BillAdapter adapter;
    private Billistadapter billistadapter;
    private EditText etxclientname;
    private EditText etxclientno;
    private RecyclerView materiallistview;
    private TextView Clientname;
    private TextView sitename;
    private EditText contact_name,contact_number,email_id;
    private Button sendqoutation,btnplaceorder;
 ;
    private Button btnsubmit1,btnreject1;
    private Billform billform = new Billform();
    ArrayList<Billform> billformlist = new ArrayList<>();
    private SignatureView signatureView;
    private String emptysign = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAHCAkgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+/iiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooA/9k=";
    private Button btnclear,btnbill;
    private SiteDetailModel siteDetailModel=null;
    private TextView enquiryno;
    private MaterialCheckBox checkSelectAll;

    private ArrayList<String> submittedReferencesForBill=new ArrayList<>();
    private ArrayList<String> rejectedReferencesForBill=new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        Initview(view);
        FetchBills();

        if (getArguments() != null) {
            rejectedReferencesForBill = getArguments().getStringArrayList("rejectedReferencesForBill");
            System.out.println(rejectedReferencesForBill);
        }

        if (getArguments() != null) {
            submittedReferencesForBill = getArguments().getStringArrayList("submittedReferencesForBill");
            System.out.println(submittedReferencesForBill);
        }
        if (getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL) != null) {
            siteDetailModel = (SiteDetailModel) getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL);
        }

        btnbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve saved references from SharedPreferences
                ArrayList<String> references = getReferencesFromSharedPreferencesBill(getActivity());



                // Create an instance of the adapter with the retrieved references
                adapter = new BillAdapter(getActivity(), references);


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
                            adapter.removeBill(reference);
                        }
                    }
                });

            }

        });

        btnreject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) { // Check if adapter is initialized
                    ArrayList<String> selectedReferences = adapter.getSelectedBills();

                    // Check if at least one reference is selected
                    if(selectedReferences.isEmpty()) {
                        // If no reference is selected, show a message to the user
                        Toast.makeText(getContext(), "Please select at least one Bill", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Check if only one report is selected
                    if (selectedReferences.size() != 1) {
                        // Display a message to the user
                        Toast.makeText(getContext(), "Only One Bill Can Reject.", Toast.LENGTH_SHORT).show();
                        return; // Exit the onClick method
                    }

                    // Print the selected references or any other relevant information
                    for (String reference : selectedReferences) {
                        Log.d("Rejection", "Bill Rejected: " + reference);
                        rejectedReferencesForBill.add(reference);
                        // Remove the selected report from the adapter's data source
                        adapter.removeBill(reference);
                    }

                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged(); // Update the UI

                    // Replace the current fragment with RejectionFragment
                    RejectionFragment rejectionFragment = new RejectionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("rejectedReferencesForBill", rejectedReferencesForBill);
                    rejectionFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, rejectionFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Log.e("tag", "Adapter is null");
                }
            }
        });


        btnsubmit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) { // Check if adapter is initialized
                    ArrayList<String> selectedReferences = adapter.getSelectedBills();
                    // Handle submission logic here

                    // Check if at least one reference is selected
                    if(selectedReferences.isEmpty()) {
                        // If no reference is selected, show a message to the user
                        Toast.makeText(getContext(), "Please select at least one Bill", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Print the selected references or any other relevant information
                    for (String reference : selectedReferences) {
                        Log.d("Submission", "Bill submitted: " + reference);
                        submittedReferencesForBill.add(reference);
                        adapter.removeBill(reference);

                    }

                    adapter.notifyDataSetChanged(); // Update the UI
                    Fragment reportFragment = new Reportfragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("submittedReferencesForBill", submittedReferencesForBill);
                    System.out.println(submittedReferencesForBill);
                    reportFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, reportFragment)
                            .addToBackStack(null).commit();
                }
            }
        });

        return view;
    }

    //webservice for getting bill list
    private void FetchBills() {
        CustomProgressDialog.dismissDialog(getActivity());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", "100");

        Allbillist = new ArrayList<>();


        RequestHandler.makeWebservice(true, getActivity(), Request.Method.GET,
                URLS.getInstance().get_bills, hashMap, Bill[].class,
                new VolleyResponseListener<Bill>() {
                    @Override
                    public void onResponse(Bill[] object) {
                        Gson gson = new Gson();
                        String billsJson = gson.toJson(object);
                        saveReferenceToSharedPreferencesBill(getActivity(), billsJson);
                        System.out.println(billsJson);
                        if (object[0] instanceof Bill) {
                            for (Bill materialobject : object) {
                                if (materialobject.getPresent().equalsIgnoreCase("yes")) {
                                    Allbillist.add(materialobject);
                                }
                            }
                        }
                        //changed listview as a recyclerview
                        billistadapter = new Billistadapter(getActivity(), Allbillist);
                        CustomProgressDialog.dismissDialog(getActivity());

                    }

                    @Override
            public void onError(String message) {
                Allbillist = new ArrayList<>();
                billistadapter = new Billistadapter(getActivity(), Allbillist);
                Utility.errorDialog("No Bill Found.", getActivity());

                // Dismiss the progress dialog in case of error
                CustomProgressDialog.dismissDialog(getActivity());
            }
                }

        );
    }

    private void saveReferenceToSharedPreferencesBill(Context context, String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyReferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Extract and store only the "reference" values
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String reference = jsonObject.getString("reference");
                editor.putString("reference_" + i, reference);

            }
            editor.apply();
            // Call the method to delete the data after storing
           // deleteReferencesFromSharedPreferences(context, jsonArray.length());

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

    private ArrayList<String> getReferencesFromSharedPreferencesBill(Context context) {
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
        materiallistview = (RecyclerView) view.findViewById(R.id.lvreportlist);
        checkSelectAll= (MaterialCheckBox) view.findViewById(R.id.checkSelectAll);
        btnbill = view.findViewById(R.id.btn_billss);
        btnsubmit1 = (Button) view.findViewById(R.id.btn_submit1);
//        signatureView = (SignatureView) view.findViewById(R.id.signatureView);
        btnclear = (Button) view.findViewById(R.id.btnclear);
        etxclientname = (EditText) view.findViewById(R.id.etxclientname);
        etxclientno = (EditText) view.findViewById(R.id.etxclientno);
        btnreject1 = (Button) view.findViewById(R.id.btn_reject_for_Bill);
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
