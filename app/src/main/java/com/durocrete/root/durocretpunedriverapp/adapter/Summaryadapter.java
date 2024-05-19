package com.durocrete.root.durocretpunedriverapp.adapter;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.model.Summary;

import java.util.ArrayList;
import java.util.List;

public class Summaryadapter extends RecyclerView.Adapter<Summaryadapter.MyViewHolder> {

    private List<Summary> summarylist;
    private Context context;
    private int Alltotal = 0;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvclientname;
        private TextView tvintime;
        private TextView tvouttime;
        private TextView tvstatus;
        private TextView txtduration;


        public MyViewHolder(View view) {
            super(view);
            tvclientname = (TextView) view.findViewById(R.id.txtClientName);
            tvintime = (TextView) view.findViewById(R.id.txtInTime);
            tvouttime = (TextView) view.findViewById(R.id.txtOutTime);
            tvstatus = (TextView) view.findViewById(R.id.txtStatus);
            txtduration=(TextView)view.findViewById(R.id.txtduration);
        }


    }

    public Summaryadapter(FragmentActivity activity, ArrayList<Summary> testlist) {
        this.summarylist = testlist;
        this.context = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_task_summary, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        Summary summary = summarylist.get(position);

        holder.tvclientname.setText(summary.getClientName());
        holder.tvintime.setText(summary.getIn_time());
        holder.tvouttime.setText(summary.getOut_time());
        holder.tvstatus.setText(summary.getStatus());
        holder.txtduration.setText(summary.getAvailable_time());
    }

    @Override
    public int getItemCount() {
        return summarylist.size();

    }


}
