package com.durocrete.root.durocretpunedriverapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.Utillity.SharedPreference;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity3 extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, RadioGroup.OnCheckedChangeListener {
    private static String TAG = MapActivity3.class.getSimpleName();
    private Activity mActivity;
    private GoogleMap gMap = null;
    private ArrayList<SiteDetailModel> points = null;
    private ArrayList<SiteDetailModel> placeDetailsArrayList = null;
    private TextView txtHeaderPickUpPointName, txtHeader;
    private static int METERS_100 = 100;
    private TextView dialogContractorsName, dialogSideName, dialogClientName, dialogSiteAddress = null;
    LatLng latLngOrigin = null;
    private Button dialogBtnSignIn = null;
    private Dialog dialogSignIn = null;
    private int checkOutSiteId = 0;
    private SupportMapFragment mapFragment;
    ProgressDialog progressDialog;
    public LinearLayout llHeaderLayout;
    public Button btnDriveNow;
    ArrayList<SiteDetailModel> latLngs;
    ArrayList<SiteDetailModel> routlist;
    Polyline polyline;
    String distance = "", duration = "";
    int i = 0;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LatLng latLng;
    double worklat = 0;
    double worklong = 0;
    LatLng position, mumbaiCity;
    ProgressDialog pd = null;
    ArrayList<LatLng> MarkerPoints;
    TextView tvLocation;
    Marker mCurrLocationMarker, previousMarker;
    ListView listView;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    LocationRequest mLocationRequest;
    String address;
    RadioGroup radioGroup;
    RadioButton nearButton, allButton;
    LatLng destination;
    private TextView tvduration;
    private TextView tvdistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = MapActivity3.this;
        setContentView(R.layout.fragment_map);
        routlist = new ArrayList<>();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.show();

        tvduration = (TextView) findViewById(R.id.tvduration);
        tvdistance = (TextView) findViewById(R.id.tvDistance);

        Intent intent = getIntent();
        if (intent != null) {
            Log.v(TAG, "selectedRouteId : " + Constants.SELECTED_ROUTE_ID);
            Log.v(TAG, " bundle is not null : " + intent.getIntExtra(Constants.CHECK_OUT_SITE_ID, 0));
            checkOutSiteId = intent.getIntExtra(Constants.CHECK_OUT_SITE_ID, 0);
        }
        initializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        llHeaderLayout.setVisibility(View.VISIBLE);
    }

    private void drawPolyLine(LatLng origin, LatLng destination) {
        try {
            if (!origin.equals(destination)) {
                if (polyline != null) {
                    polyline.remove();
                    polyline = null;
                }
                // Checks, whether start and end locations are captured
                if (!String.valueOf(origin.latitude).trim().equals("null") &&
                        !String.valueOf(origin.longitude).trim().equals("null")
                        && !String.valueOf(destination.latitude).trim().equals("null")
                        && !String.valueOf(destination.longitude).trim().equals("null")) {
                    DrawRoutePolyLine(latLng, destination);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //initialize google play services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                settingsrequest();
                gMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            settingsrequest();
            gMap.setMyLocationEnabled(true);
            // Call the method for getting division information here for devices less than Android M after turning on GPS
        }


        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                worklat= placeDetailsArrayList.get(Integer.parseInt(marker.getId().replace("m", ""))).getSiteLatitude();
//                worklong=placeDetailsArrayList.get(Integer.parseInt(marker.getId().replace("m", ""))).getSiteLongitude();
                LatLng destination = marker.getPosition();
                drawPolyLine(latLng, destination);

//
//                try
//                {
//                    if(Utility.isNetworkConnected(MapActivity3.this))
//                    {
////                        LatLng origin = mCurrLocationMarker.getPosition();
//                    LatLng destination = marker.getPosition();
//                        // Avoid the polyline to be redrawn if the same marker gets clicked
//                        if(previousMarker!=null)
//                        {
//                            if(!previousMarker.equals(marker))
//                            {
//                                drawPolyLine(latLng,destination);
//                            }
//                        }//  the polyline gets drawn for the first time
//                        else if(previousMarker==null)
//                        {
//                            drawPolyLine(latLng,destination);
//                        }
//                        previousMarker = marker;
//                    }
//                    else
//                    {
//                        Utility.noConnectionAlert(MapActivity3.this);
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
                return false;
            }
        });

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                //addConnectionCallbacks provides callbacks that are called when client connected or disconnected.
                .addConnectionCallbacks(this)
                //.addOnConnectionFailedListener covers scenarios of failed attempt of connect client to service.
                .addOnConnectionFailedListener(this)
                //.addApi adds the LocationServices API endpoint from Google Play Services.
                .addApi(LocationServices.API)
                .build();
        //mGoogleApiClient.connect(): A client must be connected before excecuting any operation.
        mGoogleApiClient.connect();
    }

    public void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapActivity3.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // FusedLocationProviderApi using requestLocationUpdates.
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }


    private void initializeUI() {
        /*related to dialogSignIn*/
        dialogSignIn = new Dialog(mActivity);
        dialogSignIn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSignIn.setContentView(R.layout.custome_sign_in_dialog);
        dialogContractorsName = (TextView) dialogSignIn.findViewById(R.id.txtContactPersonName);
        dialogSideName = (TextView) dialogSignIn.findViewById(R.id.txtSideName);
        dialogClientName = (TextView) dialogSignIn.findViewById(R.id.txtClientName);
        dialogSiteAddress = (TextView) dialogSignIn.findViewById(R.id.txtSiteAddress);
        dialogBtnSignIn = (Button) dialogSignIn.findViewById(R.id.btnSignIN);
        txtHeaderPickUpPointName = (TextView) findViewById(R.id.txtHeaderPickUpPointName);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        llHeaderLayout = (LinearLayout) findViewById(R.id.llHeaderLayout);
        btnDriveNow = (Button) findViewById(R.id.btnDriveNow);
        routlist = new ArrayList<>();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // Testing for Chembur Location  19.0583° N, 72.9055° E
        // latLng = new LatLng(19.0583, 72.9055);

        //place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        showCurrentLocationMarker(latLng);

        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        if (Utility.isNetworkConnected(MapActivity3.this)) {
            makeRequestForPickUpPoints();
        } else {
            Utility.noConnectionAlert(MapActivity3.this);
        }

    }

    private void showCurrentLocationMarker(LatLng latLng) {
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");

        gMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Current Position")
                .snippet("and snippet")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

    }


    private void DrawRoutePolyLine(LatLng origin, LatLng destination) {
        // Getting URL to the Google Directions API
        if (!origin.equals(destination)) {
            String url = getUrl(origin, destination);
            // Step 1
            MapActivity3.FetchUrl FetchUrl = new MapActivity3.FetchUrl();
            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
            //move map camera
            gMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to   routlist=new ArrayList<>(); the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" +
                "AIzaSyA8Umo0dCg3ok1RbI9HvCkud547X24Qjx0";

        return url;

    }


    private class FetchUrl extends AsyncTask<String, Void, String> {


        @Override
        protected  void onPreExecute()
        {
            Toast.makeText(MapActivity3.this,"Fetching route details, please wait.. ",Toast.LENGTH_SHORT).show();
        }


        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            MapActivity3.ParserTask parserTask = new MapActivity3.ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                RouteDataParser parser = new RouteDataParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                polyline = gMap.addPolyline(lineOptions);
                if (!distance.trim().isEmpty() || !duration.trim().isEmpty() || !distance.equals("null") || !duration.equals("null")) {
                    tvdistance.setText(distance);
                    tvduration.setText(duration);
                }
            } else {
            }
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            java.net.URL url = new URL(strUrl);

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


    private void drawPointsNRegionZoom(List<SiteDetailModel> list) {
    /* first create the reference LatLngBounds for zoom map according LatLng points*/


        if (points != null) {

            for (int i = 0; i < list.size(); i++) {
                Marker locationMarker = gMap.addMarker(new MarkerOptions().position(new LatLng(list.get(i).getSiteLatitude(), list.get(i).getSiteLongitude())));
                locationMarker.setTag(i);
                locationMarker.setTitle(list.get(i).getClientName());
                locationMarker.setSnippet(list.get(i).getSiteaddress());
                locationMarker.showInfoWindow();
            }
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
            gMap.moveCamera(center);
            gMap.animateCamera(zoom);


        }
    }


    private void makeRequestForPickUpPoints() {
        RequestHandler.makeWebservice(true, mActivity, Request.Method.GET, URLS.getInstance().getPickUPPoints(SharedPreference.getInstanceProfileData(mActivity).getUserId()), null, SiteDetailModel[].class, new VolleyResponseListener<SiteDetailModel>() {
            @Override
            public void onResponse(SiteDetailModel[] object) {
                points = new ArrayList<SiteDetailModel>();
                placeDetailsArrayList = new ArrayList<SiteDetailModel>();
                if (object[0] instanceof SiteDetailModel) {
                    for (SiteDetailModel sideObject : object) {
                        if (!(sideObject.getSiteLongitude() == 0) && !(sideObject.getSiteLatitude() == 0)) {
                            points.add(sideObject);
                            placeDetailsArrayList.add(sideObject);
                        }
                    }

                    drawPointsNRegionZoom(points);


                }
            }

            @Override
            public void onError(String message) {
                Utility.errorDialog(message, mActivity);

            }
        });

    }

}
