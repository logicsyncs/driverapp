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
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

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
    public void setOnItemClickListener(ReferenceAdapter.OnItemClickListener listener) {
        this.itemClickListener = itemClickListener;
    }


    // Constructor to initialize the adapter with references
    public BillAdapter(ArrayList<String> references) {
        this.references = references;
    }

    // Other methods of the RecyclerView.Adapter implementation...

    public BillAdapter(Context context, ArrayList<String> references) {
        this.context = context;
        this.references = references;
        this.selectedItems = new SparseBooleanArray();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item, parent, false);
        return new BillViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        String referenceNumber = references.get(position);
        holder.bind(referenceNumber);

        holder.textViewBill.setText(referenceNumber);
        holder.checkBox.setChecked(selectedItems.get(position, false));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedItems.put(position, isChecked);
            if (listener != null) {
                listener.onSelected(referenceNumber, isChecked);
            }
        });

        holder.checkBox.setChecked(selectedItems.get(position, false));
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

    public ArrayList<String> getSelectedBills() {
        ArrayList<String> selected = new ArrayList<>();
        for (int i = 0; i < references.size(); i++) {
            if (selectedItems.get(i, false)) {
                selected.add(references.get(i));
            }
        }
        return selected;
    }

    public void selectAll() {
        for (int i = 0; i < references.size(); i++) {
            selectedItems.put(i, true);
        }
        notifyDataSetChanged();
    }

    public void deselectAll() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void removeBill(String bill) {
        int index = references.indexOf(bill);
        if (index != -1) {
            references.remove(index);
            updateSharedPreferences1(context, references);
            notifyDataSetChanged();
        }
    }
    private void updateSharedPreferences1(Context context, ArrayList<String> bills) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyBills", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        for (int i = 0; i < bills.size(); i++) {
            editor.putString("bill_" + i, bills.get(i));
        }
        editor.apply();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewBill;
        private MaterialCheckBox checkBox;
        private OnItemClickListener itemClickListener;

        public BillViewHolder(@NonNull View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            textViewBill = itemView.findViewById(R.id.cbCheckItem);
            checkBox = itemView.findViewById(R.id.cbCheckItem);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(position);
                }
            }
        }

        public void bind(String billNumber) {
            textViewBill.setText(billNumber);
        }
    }
}

