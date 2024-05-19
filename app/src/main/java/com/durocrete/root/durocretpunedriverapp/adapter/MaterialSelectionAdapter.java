package com.durocrete.root.durocretpunedriverapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.model.MaterialDetailModel;

import java.util.ArrayList;


/**
 * Created by root on 24/5/17.
 */
public class MaterialSelectionAdapter extends ArrayAdapter<MaterialDetailModel> {
    private String TAG = MaterialSelectionAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<MaterialDetailModel> objects;

    public MaterialSelectionAdapter(Context mContext, int textViewResourceId, ArrayList<MaterialDetailModel> objects) {
        super(mContext, textViewResourceId, objects);
        this.mContext = mContext;
        this.objects = objects;
    }

    public void setArray(ArrayList<MaterialDetailModel> arrayList) {
        objects.addAll(arrayList);
        this.notifyDataSetChanged();
    }

    public void clearArray() {
        this.objects.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }

    class ViewHolder {
        private TextView spinnerTextView;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_select_route, null);
            holder.spinnerTextView = (TextView) convertView.findViewById(R.id.spinner_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (objects != null) {
            holder.spinnerTextView.setText(objects.get(position).getMaterialName());
        }

        return convertView;
    }
}
