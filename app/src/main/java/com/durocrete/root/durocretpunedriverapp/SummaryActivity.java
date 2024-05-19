package com.durocrete.root.durocretpunedriverapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.SharedPreference;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.adapter.Summaryadapter;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.Summary;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SummaryActivity extends AppCompatActivity {

    ArrayList<Summary> summarylist;
    RecyclerView summaryrecycler;
    Summaryadapter summaryadapter;
    MyPreferenceManager sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        summaryrecycler=(RecyclerView)findViewById(R.id.summaryrecycle) ;

        sharedPref = new MyPreferenceManager(this);

        summarylist = new ArrayList<>();


        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("driverId", SharedPreference.getInstanceProfileData(this).getUserId());

        RequestHandler.makeWebservice(true, SummaryActivity.this, Request.Method.POST, URLS.getInstance().pick_up, hashMap1, Summary[].class, new VolleyResponseListener<Summary>() {
            @Override
            public void onResponse(Summary[] object) {
                if (object[0] instanceof Summary) {
                    for (Summary routeObject : object) {
                        summarylist.add(routeObject);
                    }
                }
                summaryadapter = new Summaryadapter(SummaryActivity.this, summarylist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SummaryActivity.this);
                summaryrecycler.setLayoutManager(mLayoutManager);
                summaryrecycler.setItemAnimator(new DefaultItemAnimator());
                summaryrecycler.setAdapter(summaryadapter);

            }

            @Override
            public void onError(String message) {
                Utility.errorDialog(message, SummaryActivity.this);
            }
        });


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


         if(id==android.R.id.home)
        {
            Intent intent=new Intent(this,RouteNSiteSelectionActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();
        Intent intent=new Intent(this,RouteNSiteSelectionActivity.class);
        startActivity(intent);
        finish();
    }

}
