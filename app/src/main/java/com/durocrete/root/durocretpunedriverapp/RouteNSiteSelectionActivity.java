package com.durocrete.root.durocretpunedriverapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.SharedPreference;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.adapter.RouteSelectionAdapter;
import com.durocrete.root.durocretpunedriverapp.adapter.SitesSelectionAdapter;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.RouteDetailModel;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

import org.json.JSONObject;

public class RouteNSiteSelectionActivity extends AppCompatActivity {
    private static String TAG = RouteNSiteSelectionActivity.class.getSimpleName();
    private Activity mActivity;
    private Spinner spinnerRouteSelection;
    private RouteSelectionAdapter routeSelectionAdapter = null;
    private ArrayList<RouteDetailModel> routeObjectArrayList = null;
    private SitesSelectionAdapter sitesSelectionAdapter = null;
    private RecyclerView recyclerSideview = null;
    private Button btnSubmit = null;
    private LinearLayout llHeaderLayout;
    private AutoCompleteTextView searchAutoComplete = null;
    protected LocationManager locationManager;
    private boolean isGPSEnable = false;
    private boolean isNetworkEnable = false;
    private RelativeLayout rlParentLayout;
    private String selectedRouteId = "";
    private boolean isFirstGPSEnable = true;
    ArrayList<SiteDetailModel> sideObjectArrayList;
    private Button history,logout;
    MyPreferenceManager sharedPref;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");
        setContentView(R.layout.activity_route_site_selection);
        this.mActivity = RouteNSiteSelectionActivity.this;

        sharedPref = new MyPreferenceManager(this);
        logout=(Button)findViewById(R.id.logoutbutton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.clearSharedPreference();
                Intent intent = new Intent(RouteNSiteSelectionActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        history = (Button) findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteNSiteSelectionActivity.this, SummaryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        initializeUI();

//        setGPSSetting();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.track_driver) {
            Intent intent = new Intent(this, TrackActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.sign_out)
        {
            sharedPref.clearSharedPreference();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= M)
        {
            checkLocationPermission();
        }else {
            makeSideRequest();
        }
    }

    private void initializeUI() {
        rlParentLayout = (RelativeLayout) findViewById(R.id.rlParentLayout);
        routeSelectionAdapter = new RouteSelectionAdapter(this, R.layout.row_select_route, new ArrayList<RouteDetailModel>());
        recyclerSideview = (RecyclerView) findViewById(R.id.recyclerSideview);
//        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        sitesSelectionAdapter = new SitesSelectionAdapter(this, new ArrayList<SiteDetailModel>());
//        ((BaseActivity) mActivity).hideHeaderLayout();
        Button fabButton = (Button) findViewById(R.id.fabFoo);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                if (!selectedRouteId.equals("")) {
                  /*  Bundle bundle = new Bundle();
                    MapActivity mapFragment = new MapActivity();
                    Log.v(TAG, "selectedRouteId : " + selectedRouteId);
                    bundle.putString(Constants.SELECTED_ROUTE_ID, selectedRouteId);
                    mapFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).addToBackStack(TAG).commit();*/
                startActivity(new Intent(mActivity, MapActivity3.class));

//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              } else {
//                    Utility.errorDialog("Select Route First.. ", mActivity);
//                }
            }
        });

    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(RouteNSiteSelectionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //Request Location Permission
                checkALLPermission();
            }
            else
            {
                makeSideRequest();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkALLPermission() {

        if (ContextCompat.checkSelfPermission(RouteNSiteSelectionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RouteNSiteSelectionActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new androidx.appcompat.app.AlertDialog.Builder(RouteNSiteSelectionActivity.this)
                        .setTitle("Permission Needed")
                        .setMessage("This app needs the Permissions for proper functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.v("AAA 99 ", "permission granted");
                                //Prompt the user once explanation has been shown
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                            }
                        })
                        .create()
                        .show();

            /*requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    REQUEST_ID_MULTIPLE_PERMISSIONS);*/

            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

        }
    }

    private void makeSideRequest() {

        sideObjectArrayList = new ArrayList<>();
        sitesSelectionAdapter.clearAdapter();
        RequestHandler.makeWebservice(true, mActivity, Request.Method.GET, URLS.getInstance().getPickUPPoints(SharedPreference.getInstanceProfileData(mActivity).getUserId()), null, SiteDetailModel[].class, new VolleyResponseListener<SiteDetailModel>() {


            @Override
            public void onResponse(SiteDetailModel[] object) {
                System.out.println("Response :"+object);
                if (object[0] instanceof SiteDetailModel) {
                    for (SiteDetailModel sideObject : object) {
                        Log.d("tag", "" + sideObject.getStatus());
                        if (sideObject.getStatus() == null || sideObject.getStatus() == "" || !(sideObject.getStatus().equalsIgnoreCase("Collected"))) {
                            sideObjectArrayList.add(sideObject);
                        }
                    }
                    Log.v(TAG, " sideObjectArrayList : " + sideObjectArrayList.size());
                }

                if (sideObjectArrayList.size() > 0) {


//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerSideview.setLayoutManager(new LinearLayoutManager(mActivity));
                    // specify an adapter (see also next example)
                    sitesSelectionAdapter.setArray(sideObjectArrayList);
                    recyclerSideview.setAdapter(sitesSelectionAdapter);
                } else {

                    Log.v(TAG, " sideObjectArrayList : " + sideObjectArrayList.size());
                    Toast.makeText(mActivity, "No Site Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                Utility.errorDialog(message, mActivity);

            }
        });
    }


}
