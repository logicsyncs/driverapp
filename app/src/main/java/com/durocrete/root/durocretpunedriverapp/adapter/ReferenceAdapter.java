package com.durocrete.root.durocretpunedriverapp.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.durocrete.root.durocretpunedriverapp.R;

import java.util.ArrayList;

import com.google.android.material.checkbox.MaterialCheckBox;

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.ReferenceViewHolder> {

    private Context context;
    private ArrayList<String> references;
    private SparseBooleanArray selectedItems;
    private boolean isAllSelected = false;
    private OnReferenceSelectedListener listener;
    private OnItemClickListener itemClickListener;

    public interface OnReferenceSelectedListener {
        void onSelected(String reference, boolean isSelected);
    }
    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    //private OnItemClickListener listener;

    // Method to set the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = itemClickListener;
    }


    // Constructor to initialize the adapter with references
    public ReferenceAdapter(ArrayList<String> references) {
        this.references = references;
    }

    // Other methods of the RecyclerView.Adapter implementation...

    public ReferenceAdapter(Context context, ArrayList<String> references) {
        this.context = context;
        this.references = references;
        this.selectedItems = new SparseBooleanArray();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ReferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item, parent, false);
        return new ReferenceViewHolder(itemView,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceViewHolder holder, int position) {
        String referenceNumber = references.get(position);
        holder.bind(referenceNumber);

        String reference = references.get(position);
        holder.textViewReference.setText(reference);
        holder.checkBox.setChecked(selectedItems.get(position, false));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedItems.put(position, isChecked);
            if (listener != null) {
                listener.onSelected(reference, isChecked);
            }
        });

        // Set the state of the checkbox based on the selection status
        holder.checkBox.setChecked(selectedItems.get(position, false));

    }

    //remove report
    public void removeReport(String reference) {
        // Find the index of the reference to be removed
        int index = references.indexOf(reference);
        if (index != -1) { // Check if the reference exists in the list
            // Remove the reference from the data source
            references.remove(index);

            // Update SharedPreferences with the updated references
            updateSharedPreferences(context, references);

            // Notify the adapter that the data set has changed
            notifyDataSetChanged();
        }
    }

    private void updateSharedPreferences(Context context, ArrayList<String> references) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyReferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear the existing references
        editor.clear().apply();

        // Store the updated references
        for (int i = 0; i < references.size(); i++) {
            editor.putString("reference_" + i, references.get(i));
        }
        editor.apply();
    }


    @Override
    public int getItemCount() {
        return references.size();
    }
    public String getItem(int position) {
        if (position >= 0 && position < references.size()) {
            return references.get(position);
        } else {
            return null;
        }
    }


    // Method to retrieve selected references of report
    public ArrayList<String> getSelectedReferences() {
        ArrayList<String> selected = new ArrayList<>();
        for (int i = 0; i < references.size(); i++) {
            if (selectedItems.get(i, false)) {
                selected.add(references.get(i));
            }
        }
        return selected;
    }

    public void selectAll() {
        // Iterate through all items in the dataset
        for (int i = 0; i < references.size(); i++) {
            // Mark each item as selected in the SparseBooleanArray
            selectedItems.put(i, true);
        }
        // Notify the adapter that the dataset has changed
        notifyDataSetChanged();
    }

    public void deselectAll() {
        // Clear the selection status for all items in the dataset
        selectedItems.clear();
        // Notify the adapter that the dataset has changed
        notifyDataSetChanged();
    }

public class ReferenceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textViewReference;
    private MaterialCheckBox checkBox;
    private OnItemClickListener itemClickListener;

    public ReferenceViewHolder(@NonNull View itemView, OnItemClickListener itemClickListener) {
        super(itemView);
        textViewReference = itemView.findViewById(R.id.cbCheckItem);
        checkBox = itemView.findViewById(R.id.cbCheckItem);
        this.itemClickListener = itemClickListener;

        // Set click listener for the item view
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Call onItemClick method of the listener when an item is clicked
        if (itemClickListener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(position);
            }
        }
    }

    public void bind(String referenceNumber) {
        textViewReference.setText(referenceNumber);
    }
}

}

