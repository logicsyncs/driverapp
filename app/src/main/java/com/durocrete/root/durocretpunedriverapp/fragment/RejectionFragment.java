package com.durocrete.root.durocretpunedriverapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.BillRejectModel;
import com.durocrete.root.durocretpunedriverapp.model.CheckInOUTModel;
import com.durocrete.root.durocretpunedriverapp.network.WebserviceHandler;
import com.durocrete.root.durocretpunedriverapp.reports.Reportfragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RejectionFragment extends Fragment {

    MaterialAutoCompleteTextView reasonsList;
    TextInputEditText remark;
    MaterialButton btnSubmit;
    private ProgressDialog working_dialog;
    private ArrayList<String> rejectedReferencesForReport=new ArrayList<>();;
    private ArrayList<String> rejectedReferencesForBill=new ArrayList<>();;


    public RejectionFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rejection_reason, container, false);
        if (getArguments() != null) {
            rejectedReferencesForReport = getArguments().getStringArrayList("rejectedReferencesForReport");
            System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            System.out.println(rejectedReferencesForReport);
        }

        if (getArguments() != null) {
            rejectedReferencesForBill = getArguments().getStringArrayList("rejectedReferencesForBill");
            System.out.println("heloooooooooooooooooooooooooooooooooo");
            System.out.println(rejectedReferencesForBill);
        }


        // Initialize views
        reasonsList = view.findViewById(R.id.reasonsList);
        remark = view.findViewById(R.id.remark);
        btnSubmit = view.findViewById(R.id.btnSubmit1);

        // Populate reasons list
        populateReasons();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRejectionData();

                // Do something with the selected reason and remark (e.g., submit data)
                // Toast.makeText(getContext(), "Selected Reason: " + selectedReason + ", Remark: " + enteredRemark, Toast.LENGTH_SHORT).show();
//                @SuppressLint("SetTextI18n")
//                Dialog progressDialog = new Dialog(getContext());
//                progressDialog.setContentView(R.layout.new_file);
//                ProgressBar progressBar = progressDialog.findViewById(R.id.progressBar);
//                TextView textview = progressDialog.findViewById(R.id.textViewMessage);
//                progressBar.setIndeterminate(true);
//                textview.setText("Uploading please wait...");
//                progressDialog.show();
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getContext(), " Data uploaded", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                }, 5000);
//                if (working_dialog != null) {
//                    working_dialog.dismiss();
//                    if (getActivity() != null) {
//                        Toast.makeText(getActivity(), "Data uploading.....", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                Fragment reportFragment = new Reportfragment();
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList("rejectedReferencesforreport", rejectedReferencesForReport);
//                reportFragment.setArguments(bundle);
//
//
//                bundle.putStringArrayList("rejectedReferencesforbill", rejectedReferencesforbill);
//
//                reportFragment.setArguments(bundle);
//                reportFragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, reportFragment)
//                        .addToBackStack(null)
//                        .commit();
            }
        });

        return view;
    }

    private void sendRejectionData() {
        // Assuming reasonsList is a MaterialAutoCompleteTextView and remark is a TextInputEditText
        String selectedReason = reasonsList.getText().toString();
        String remarkText = remark.getText().toString();
        String status="0";

        // Basic validation
        if (selectedReason.isEmpty()) {
            Toast.makeText(getActivity(), "Please select a reason.", Toast.LENGTH_SHORT).show();
            return;
        } else if (remarkText.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a remark.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here, add the logic to create your data object to be sent to the server.
        // This example creates a simple HashMap, but you might need a more complex object or JSON.
       // String finalRefIds=android.text.TextUtils.join(",",rejectedReferencesForReport);
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("rejectionReason", selectedReason);
//        hashMap.put("remark", remarkText);
//       // hashMap.put("refrence_no", finalRefIds);
//        hashMap.put("status", status);
//        // Add reference numbers based on availability
//        if (rejectedReferencesForReport != null && !rejectedReferencesForReport.isEmpty()) {
//            hashMap.put("refrence_no", TextUtils.join(",", rejectedReferencesForReport));
//        } else if (rejectedReferencesForBill != null && !rejectedReferencesForBill.isEmpty()) {
//            hashMap.put("refrence_no", TextUtils.join(",", rejectedReferencesForBill));
//        }

       // Log.d("rejected reports",finalRefIds);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rejectionReason", selectedReason);
            jsonObject.put("remark", remarkText);
            jsonObject.put("status", status);
            //jsonObject.put("status", status);
            // Add reference numbers based on availability
            if (rejectedReferencesForReport != null && !rejectedReferencesForReport.isEmpty()) {
                jsonObject.put("refrence_no", TextUtils.join(",", rejectedReferencesForReport));
            } else if (rejectedReferencesForBill != null && !rejectedReferencesForBill.isEmpty()) {
                jsonObject.put("refrence_no", TextUtils.join(",", rejectedReferencesForBill));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // String jsonData = jsonObject.toString();
        //Log.d("sendRejectionData", "Sending data: " + jsonData);
        // Create a HashMap to store the JSON object
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("data", jsonObject);

        Log.d("sendRejectionData", "Sending data: " + hashMap);

        // Example POST request (details depend on your HTTP client and server API)
        // Assuming RequestHandler is your utility class for handling requests
        WebserviceHandler.makeWebservice1(false, getActivity(), Request.Method.POST,
                URLS.getInstance().submit_report, hashMap, BillRejectModel[].class, new VolleyResponseListener<BillRejectModel>() {
                    @Override
                    public void onResponse(BillRejectModel[] response) {
                        // Handle successful submission
                       // Toast.makeText(getActivity(), "Report Rejected successfully submitted successfully.", Toast.LENGTH_SHORT).show();
                        if (response.length > 0) {
                            String responseMessage = response[0].getMessage(); // Assuming there's a getMessage() method in BillRejectModel
                            showResponseDialog(responseMessage);
                        }

                        // Optionally, navigate back or clear the form
                        Fragment reportFragment = new Reportfragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, reportFragment).addToBackStack(null)
                                .commit();
                    }
                    @Override
                    public void onError(String message) {
                        // Handle errors
                        Utility.errorDialog(message, getActivity());
                        Toast.makeText(getActivity(), "ERROR.", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }
    private void showResponseDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Response")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the OK button click if needed
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void populateReasons() {
        // Dummy reasons list
        List<String> reasons = new ArrayList<>();
        reasons.add(" Correction/Modification required");
        reasons.add("Authorised person not available ");
        reasons.add("Reports awaited for scheduled test");
        reasons.add("Agreed discount not provided");
        reasons.add("Tax invoice not generated as per work order");
        reasons.add("Request for current date tax invoice");

        // ArrayAdapter for reasons dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, reasons);
        reasonsList.setAdapter(adapter);
    }
}

