package com.durocrete.root.durocretpunedriverapp.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WebserviceHandler {
    public static JSONArray detailsJsonArray = null;

    public synchronized static <T> void makeWebservice1(final boolean flag, final Context context, int method, String url,
                                                        final HashMap<String, Object> param, final Class<T[]> aClass,
                                                        final VolleyResponseListener listener) {
        if (!Connectivity.isConnected(context)) {
            CustomProgressDialog.showAlertDialogMessage(context, "Alert", "Check Internet Connection");
            return; // Stop further execution
        }

        Log.d("WebserviceHandler", "URL: " + url);

        if (flag) {
            CustomProgressDialog.showDialog(context, "Please Wait");
        }

        JSONObject jsonParam = new JSONObject(param);

        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (flag) {
                    CustomProgressDialog.dismissDialog(context);
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int key = jsonObject.optInt(Constants.RESPONSE_KEY);
                    String message = jsonObject.optString(Constants.RESPONSE_MESSAGE);

                    Log.d("response", ": " + response);
                    if (key == 2/*Constants.RESPONSE_SUCCESS*/) {
//                        detailsJsonArray = jsonObject.optJSONArray(Constants.RESPONSE_INFO);
//                        GsonBuilder gsonBuilder = new GsonBuilder();
//                        Gson gson = gsonBuilder.create();
//                        Object[] object = gson.fromJson(String.valueOf(detailsJsonArray), aClass);
//                        listener.onResponse(object);

                        JSONObject jsonObject1 = new JSONObject(response);
                        int result = jsonObject1.getInt("result");
                        String message1 = jsonObject1.getString("message");

                        JSONArray jsonArray = new JSONArray();

                        // Add the JSONObject to the JSONArray
                        jsonArray.put(jsonObject1);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Object[] object = gson.fromJson(String.valueOf(jsonArray), aClass);
                        listener.onResponse(object);

                  }else if (key == 0/*Constants.RESPONSE_SUCCESS*/) { //rejection
                        // add rejection code here
                        JSONObject jsonObject1 = new JSONObject(response);
                        int result = jsonObject1.getInt("result");
                        String message1 = jsonObject1.getString("message");

                        JSONArray jsonArray = new JSONArray();

                        // Add the JSONObject to the JSONArray
                        jsonArray.put(jsonObject1);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Object[] object = gson.fromJson(String.valueOf(jsonArray), aClass);
                        listener.onResponse(object);
                    }

                   else if (key == Constants.RESPONSE_ERROR) {
                        listener.onError(message);
                    }
                } catch (JSONException e) {
                    if (flag) {
                        CustomProgressDialog.dismissDialog(context);
                    }
                    e.printStackTrace();
                    listener.onError("JSON parsing error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (flag) {
                    CustomProgressDialog.dismissDialog(context);
                }
                listener.onError("Volley error: " + error.getMessage());
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonParam.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
