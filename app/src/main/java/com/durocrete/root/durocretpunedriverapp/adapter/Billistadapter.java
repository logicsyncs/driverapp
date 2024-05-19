package com.durocrete.root.durocretpunedriverapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.bills.Bill;

import java.util.List;

public class Billistadapter extends RecyclerView.Adapter<Billistadapter.ViewHolder> {

    private Context context;
    private List<Bill> Allbilllist;

    public Billistadapter(Context context, List<Bill> alltestlist) {
        this.context = context;
        this.Allbilllist = alltestlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_list_single_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Bill bill = Allbilllist.get(position);
        holder.testname.setText(bill.getBills());
        holder.selectcheckbox.setChecked(bill.getIsChecked());

        holder.selectcheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                Allbilllist.get(position).setIschecked(checkBox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return Allbilllist.size();
    }

    public List<Bill> Alltestlist() {
        return this.Allbilllist;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView testname;
        CheckBox selectcheckbox;

        public ViewHolder(View itemView) {
            super(itemView);
            testname = itemView.findViewById(R.id.testname);
            selectcheckbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
