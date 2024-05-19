package com.durocrete.root.durocretpunedriverapp;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.adapter.Summaryadapter;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.Summary;
import com.durocrete.root.durocretpunedriverapp.network.CallWebservice;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class TrackActivity extends AppCompatActivity {

    ArrayList<Summary> summarylist;
    ArrayList<Driverdetails> driverdetaillist;
    ArrayList<String> useridlist;
    RecyclerView summaryrecycler;
    Summaryadapter summaryadapter;
    MyPreferenceManager sharedPref;
    Spinner spselectdriver;
    String userid;
    private ArrayAdapter<String> driverlistadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        summaryrecycler = (RecyclerView) findViewById(R.id.summaryrecycle);
        spselectdriver = (Spinner) findViewById(R.id.spselectdriver);
        sharedPref = new MyPreferenceManager(this);

        getdriverlist();

        spselectdriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    String driverid = useridlist.get(i).toString();
                    setsummary(driverid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





    }

    private void setsummary(String driverid) {

        summarylist = new ArrayList<>();
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("driverId", driverid);

        RequestHandler.makeWebservice(true, TrackActivity.this, Request.Method.POST, URLS.getInstance().pick_up, hashMap1, Summary[].class, new VolleyResponseListener<Summary>() {
            @Override
            public void onResponse(Summary[] object) {
                if (object[0] instanceof Summary) {
                    for (Summary routeObject : object) {
                        summarylist.add(routeObject);
                    }
                }

                summaryadapter = new Summaryadapter(TrackActivity.this, summarylist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrackActivity.this);
                summaryrecycler.setLayoutManager(mLayoutManager);
                summaryrecycler.setItemAnimator(new DefaultItemAnimator());
                summaryrecycler.setAdapter(summaryadapter);

            }


            @Override
            public void onError(String message) {
                Utility.errorDialog(message, TrackActivity.this);
                summarylist=new ArrayList<>();
                summaryadapter = new Summaryadapter(TrackActivity.this, summarylist);
                summaryrecycler.setAdapter(summaryadapter);
            }
        });


    }

    private void getdriverlist() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("", "");

        driverdetaillist = new ArrayList<>();
        useridlist = new ArrayList<>();

        useridlist.add("Select Driver Id");

        CallWebservice.getWebservice(true, this, Request.Method.GET, URLS.getInstance().driver_list, hashMap, new VolleyResponseListener<Driverdetails>() {
            @Override
            public void onResponse(Driverdetails[] object) {

                if (object[0] instanceof Driverdetails) {
                    for (Driverdetails materialobject : object) {
                        driverdetaillist.add(materialobject);
                        useridlist.add(materialobject.getUSER_Id());
                    }
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(TrackActivity.this, R.layout.row_select_route, useridlist);
                spselectdriver.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(String message) {
                Log.v("tag", message.toString());

            }
        }, Driverdetails[].class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            Intent intent = new Intent(this, RouteNSiteSelectionActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();
        Intent intent = new Intent(this, RouteNSiteSelectionActivity.class);
        startActivity(intent);
        finish();
    }

}
