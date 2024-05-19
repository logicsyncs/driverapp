package com.durocrete.root.durocretpunedriverapp.network;

import android.content.Context;

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

import java.util.HashMap;
import java.util.Map;


public class RequestHandler {
    public static JSONArray detailsJsonArray = null;


    /**
     * used it while u get whole data in response  not only id
     *
     * @param context
     * @param method
     * @param url
     * @param param
     * @param listener
     * @param aClass
     * @param <T>
     */
    public synchronized static <T> void makeWebservice(final boolean flag, final Context context, int method, String url,
                                                       final HashMap<String, String> param, final Class<T[]> aClass,
                                                       final VolleyResponseListener listener) {
        if (Connectivity.isConnected(context)) {
            if(flag==true) {
                CustomProgressDialog.showDialog(context, "Please Wait");
            }


            StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(flag==true) {
                        CustomProgressDialog.dismissDialog(context);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int key = jsonObject.optInt(Constants.RESPONSE_KEY);
                        String message = jsonObject.optString(Constants.RESPONSE_MESSAGE);

                        if (key == Constants.RESPONSE_SUCCESS) {
                            detailsJsonArray = jsonObject.optJSONArray(Constants.RESPONSE_INFO);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            Object[] object = gson.fromJson(String.valueOf(detailsJsonArray), aClass);
                            listener.onResponse(object);
                        } else if (key == Constants.RESPONSE_ERROR) {
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            listener.onError(message.toString());
                        }
                    } catch (JSONException e) {
                        if(flag==true) {
                            CustomProgressDialog.dismissDialog(context);
                        }
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(flag==true) {
                        CustomProgressDialog.dismissDialog(context);
                    }
                    listener.onError(error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap = param;
                    return hashMap;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            CustomProgressDialog.showAlertDialogMessage(context, "Alert", "Check Internet Connection");
        }
    }



}
