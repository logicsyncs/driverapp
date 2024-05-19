package com.durocrete.root.durocretpunedriverapp.listeners;

public interface IImageAdapterListener {
    void deleteImage(int pos);
    void editImage(int adapterPosition);

    void onBackPressed();
}

