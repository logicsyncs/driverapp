package com.durocrete.root.durocretpunedriverapp;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class RejectionActivity  extends AppCompatActivity  {

        MaterialAutoCompleteTextView reasonsList;
        TextInputEditText remark;
        MaterialButton btnSubmit;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.rejection_reason);

            // Initialize views
            reasonsList = findViewById(R.id.reasonsList);
            remark = findViewById(R.id.remark);
            btnSubmit = findViewById(R.id.btnSubmit1);

            // Populate reasons list
            populateReasons();

            // Button click listener
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get selected reason and remark
                    String selectedReason = reasonsList.getText().toString();
                    String enteredRemark = remark.getText().toString();

                    // Do something with the selected reason and remark (e.g., submit data)
                    Toast.makeText(RejectionActivity.this, "Selected Reason: " + selectedReason + ", Remark: " + enteredRemark, Toast.LENGTH_SHORT).show();
                }
            });
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, reasons);
            reasonsList.setAdapter(adapter);
        }
    }

