package com.durocrete.root.durocretpunedriverapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.fragment.Fragmentcheckin;
import com.durocrete.root.durocretpunedriverapp.fragment.SelectionFragment;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;

public class MainActivity extends AppCompatActivity {

    MyPreferenceManager sharedPref;
    int siteId=0;
    SiteDetailModel siteObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = new MyPreferenceManager(this);
// get data from adapter list(13 july 2019)
       Intent intent= getIntent();
       if(intent!=null){
           siteObject = (SiteDetailModel) intent.getSerializableExtra("siteDetailModel");
           siteId=siteObject.getSiteId();
        }

// end code
        Fragment fragment = null;

        if(sharedPref.getBooleanPreferences(MyPreferenceManager.done_bit))
        {
            fragment = new SelectionFragment();
            Bundle bundle=new Bundle();
            bundle.putString(Constants.SITEID, String.valueOf(siteId));
            fragment.setArguments(bundle);
        }
        else
        {
            fragment = new Fragmentcheckin();

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
