package com.durocrete.root.durocretpunedriverapp.listeners;

import com.durocrete.root.durocretpunedriverapp.model.BillRejectModel;

import org.json.JSONObject;

/**
 * Created by root on 16/7/16.
 */
public interface VolleyResponseListener<T> {
    void onResponse(T[] object);
    void onError(String message);
}
