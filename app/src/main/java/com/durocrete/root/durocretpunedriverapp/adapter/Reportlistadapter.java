package com.durocrete.root.durocretpunedriverapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.Report;

import java.util.ArrayList;
import java.util.List;

public class Reportlistadapter extends RecyclerView.Adapter<Reportlistadapter.ViewHolder> {

    private Context context;
    private List<Report> Allreportlist;

    public Reportlistadapter(Context context, List<Report> alltestlist) {
        this.context = context;
        this.Allreportlist = alltestlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_single_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = Allreportlist.get(position);
        holder.testname.setText(report.getReport());
        holder.selectcheckbox.setChecked(report.getIsChecked());

        holder.selectcheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                Allreportlist.get(position).setIschecked(checkBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return Allreportlist.size();
    }

    public List<Report> Alltestlist() {
        return this.Allreportlist;
    }


//    public void selectAllItems(boolean isChecked) {
//        for (Report report : Allreportlist) {
//            report.setSelected(true); // Assuming you have a setSelected() method in your Report class
//        }
//        notifyDataSetChanged();
//    }
//
//    public void unselectAllItems() {
//        for (Report report : Allreportlist) {
//            report.setSelected(false); // Assuming you have a setSelected() method in your Report class
//        }
//        notifyDataSetChanged();
//    }

//    private List<Report> reportList;
//
//    // Constructor and other methods
//
//    public List<Report> getSelectedReports() {
//        List<Report> selectedReports = new ArrayList<>();
//        for (Report report : reportList) {
//            if (report.isSelected()) {
//                selectedReports.add(report);
//            }
//        }
//        return selectedReports;
//    }

    public void setOnItemClickListener(VolleyResponseListener<Report> reportVolleyResponseListener) {
    }

//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }





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
