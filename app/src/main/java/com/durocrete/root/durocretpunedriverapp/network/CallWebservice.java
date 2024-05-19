package com.durocrete.root.durocretpunedriverapp.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CallWebservice {

    public static JSONArray jsonArray1 = null;

    public synchronized static <T> void getWebservice(final boolean progress, final Context context, int method,
                                                      String url, final HashMap<String, String> param,
                                                      final VolleyResponseListener listener, final Class<T[]> aClass) {

        if (Connectivity.isConnected(context)) {

            Log.d("url",url);

            if (progress == true) {
                CustomProgressDialog.showDialog(context, "Please Wait");
            }
            StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response",response);

                    if (progress == true) {
                        CustomProgressDialog.dismissDialog(context);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String key = jsonObject.getString(IConstants.RESPONSE_RESULT);
                        String message = jsonObject.getString(IConstants.RESPONSE_MESSAGE);
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        if (key.equalsIgnoreCase(IConstants.SUCCESS_KEY)) {
                            jsonArray1 = jsonObject.getJSONArray(IConstants.RESPONSE_ARRAY);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                                          Object[] object = gson.fromJson(String.valueOf(jsonArray1), aClass);
                            listener.onResponse(object);
                        } else if (key.equalsIgnoreCase(IConstants.ERROR_KEY)) {
                            if (progress == true) {
                                CustomProgressDialog.dismissDialog(context);
                                listener.onError(message.toString());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (progress == true) {
                            CustomProgressDialog.dismissDialog(context);
                            listener.onError("Something went wrong");
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progress == true) {
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
