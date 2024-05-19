package com.durocrete.root.durocretpunedriverapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.listeners.OnMaterialChangeListener;
import com.durocrete.root.durocretpunedriverapp.model.MaterialDetailModel;

import java.util.ArrayList;


public class MaterialNQuantityAdapter extends ArrayAdapter<MaterialDetailModel> {
    private String TAG = MaterialNQuantityAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<MaterialDetailModel> objects;
    private OnMaterialChangeListener mMaterialChangeListeners;

    public MaterialNQuantityAdapter(Context mContext, int textViewResourceId, ArrayList<MaterialDetailModel> objects , OnMaterialChangeListener onMaterialChangeListener) {
        super(mContext, textViewResourceId, objects);
        this.mContext = mContext;
        this.objects = objects;
        this.mMaterialChangeListeners = onMaterialChangeListener;
    }

    public void setArray(ArrayList<MaterialDetailModel> arrayList) {
        this.objects.clear();
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
        private TextView txtMaterialName;
        private TextView txtQuantity;
        private Button btnDelete;
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_material_selection, null);
            holder.txtMaterialName = (TextView) convertView.findViewById(R.id.txtMaterialName);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
            holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (objects != null) {
            holder.txtMaterialName.setText(objects.get(position).getMaterialName());
            holder.txtQuantity.setText(objects.get(position).getMaterialQuantity());
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG,"btnDelete is clicked : " + position);
                mMaterialChangeListeners.onChangeAdapterView(true , objects.get(position));
            }
        });

        return convertView;
    }
}
