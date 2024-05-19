package com.durocrete.root.durocretpunedriverapp.adapter;
import android.content.Context;
import android.os.Environment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.durocrete.root.durocretpunedriverapp.databinding.SingleImageViewLayoutBinding;
import com.durocrete.root.durocretpunedriverapp.listeners.IImageAdapterListener;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<String> imagesList;
    private IImageAdapterListener listener;

    public ImageAdapter(Context context, IImageAdapterListener imageAdapterListener) {
        mContext = context;
        imagesList = new ArrayList<>();
        listener = imageAdapterListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SingleImageViewLayoutBinding binding = SingleImageViewLayoutBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + File.separator + "DUROCRETE" + File.separator + imagesList.get(position)
        );

        Glide.with(mContext).load(file).into(holder.binding.ivImage);

    }

    @Override
    public int getItemCount() {
        return 1; // Only one image
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SingleImageViewLayoutBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = SingleImageViewLayoutBinding.bind(itemView);
            binding.imageDelete.setOnClickListener(this);
            binding.ivImage.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v == binding.imageDelete) {

                listener.deleteImage(getAdapterPosition());
            } else if (v == binding.ivImage) {
                listener.editImage(getAdapterPosition());
            }
        }
    }
}
