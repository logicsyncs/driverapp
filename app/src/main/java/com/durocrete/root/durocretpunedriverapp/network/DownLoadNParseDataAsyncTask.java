package com.durocrete.root.durocretpunedriverapp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.durocrete.root.durocretpunedriverapp.fragment.DirectionsJSONParser;
import com.durocrete.root.durocretpunedriverapp.fragment.GMapV2Direction;
import com.durocrete.root.durocretpunedriverapp.listeners.OnMapUIUpdateListener;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;


/**
 * Created by root on 16/5/17.
 */
public class DownLoadNParseDataAsyncTask extends AsyncTask<LatLng, Void, List<List<HashMap<String, String>>>> {
    private String TAG = DownLoadNParseDataAsyncTask.class.getSimpleName();
    private GMapV2Direction md = new GMapV2Direction();
    private Document doc = null;
    private Context mContext;
    private SiteDetailModel mSiteDetailModel;
    private LatLng mLatLngDestination = null;
    private int mColorCode = 0;
    private OnMapUIUpdateListener mOnMapUIUpdateListener = null;

    public DownLoadNParseDataAsyncTask(Context context , OnMapUIUpdateListener onMapUIUpdateListener , int colorCode , SiteDetailModel siteDetailModel){
       this.mContext = context;
        this.mOnMapUIUpdateListener = onMapUIUpdateListener;
        this.mColorCode = colorCode;
        this.mSiteDetailModel = siteDetailModel;
    }

    @Override
    protected void onPreExecute() {
//        CustomProgressDialog.showDialog(mContext , "Please Wait");
        Log.v("AAAA" , "Show PRogress Indicater");

//        mOnMapUIUpdateListener.onPreExecuteUpdateUI();
        super.onPreExecute();
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(LatLng... url) {
        Log.v(TAG, "doInBackground() ");

        // For storing data from web service
        String data = "";
        JSONObject jsonObject = null;
        List<List<HashMap<String, String>>> routes = null;

        try {
            // Fetching the data from web service
            data = downloadUrl(url[0],url[1]);

            /*prsing Data*/
            jsonObject = new JSONObject(data);
            routes = new DirectionsJSONParser().parse(jsonObject);

        } catch (Exception e) {
            Log.v(TAG + "doInBackground() ", e.toString());
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
        super.onPostExecute(lists);
        if(lists != null){
            CustomProgressDialog.dismissDialog(mContext);
            Log.v("AAAA" , "dismis PRogress Indicater");
            mOnMapUIUpdateListener.onTaskCompleted(lists , mColorCode , mSiteDetailModel);
        }
    }

    private String downloadUrl(LatLng startLatLng , LatLng endLatLng) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            String requestURL = "https://maps.googleapis.com/maps/api/directions/" +"json" +
                    "?" +"origin=" + startLatLng.latitude +"," + startLatLng.longitude+
                    "&" + "destination=" + endLatLng.latitude + "," + endLatLng.longitude +
                    "&" + "sensor=false&mode=driving";
            URL url = new URL(requestURL);

            Log.v(TAG , requestURL);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
