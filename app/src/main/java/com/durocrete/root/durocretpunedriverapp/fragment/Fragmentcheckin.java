package com.durocrete.root.durocretpunedriverapp.fragment;

import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.durocrete.root.durocretpunedriverapp.R;
import com.durocrete.root.durocretpunedriverapp.Utillity.MyPreferenceManager;
import com.durocrete.root.durocretpunedriverapp.Utillity.SharedPreference;
import com.durocrete.root.durocretpunedriverapp.Utillity.Utility;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.comman.GPSTracker;
import com.durocrete.root.durocretpunedriverapp.comman.URLS;
import com.durocrete.root.durocretpunedriverapp.listeners.VolleyResponseListener;
import com.durocrete.root.durocretpunedriverapp.model.CheckInOUTModel;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;
import com.durocrete.root.durocretpunedriverapp.network.RequestHandler;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by root on 1/7/17.
 */

public class Fragmentcheckin extends Fragment {


    private TextView contactPersonName, sideName, clientName,
            siteAddress, contactnumber, txtpaymentmode, txtquantity, txtmaterial,txtsitepersonName,txtsitepersonConatct;
    private Button btnSignIn;
    private SiteDetailModel siteDetailModel = null;
    private GPSTracker tracker;
    private Double latitude = 0.0;
    Double longitude = 0.0;
    MyPreferenceManager sharedpref;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentcheckin, container, false);

        if (getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL) != null) {
            siteDetailModel = (SiteDetailModel) getActivity().getIntent().getSerializableExtra(Constants.SITEDETAILMODEL);
        }
        initializeUI(view);
        getLatNLong();

        return view;

    }

    private void initializeUI(View view) {
        contactPersonName = (TextView) view.findViewById(R.id.txtContactPersonName);
        sideName = (TextView) view.findViewById(R.id.txtSideName);
        clientName = (TextView) view.findViewById(R.id.txtClientName);
        siteAddress = (TextView) view.findViewById(R.id.txtSiteAddress);
        btnSignIn = (Button) view.findViewById(R.id.btnSignIN);
        txtpaymentmode = (TextView) view.findViewById(R.id.txtpaymentmode);
        txtquantity = (TextView) view.findViewById(R.id.txtquantity);
        txtmaterial = (TextView) view.findViewById(R.id.txtmaterial);
        contactnumber = (TextView) view.findViewById(R.id.txtcontactnumber);
        txtsitepersonName=view.findViewById(R.id.txtsitepersonName);
        txtsitepersonConatct=view.findViewById(R.id.txtsitepersonContact);
        sharedpref = new MyPreferenceManager(getActivity());

        if (siteDetailModel != null) {
            clientName.setText(siteDetailModel.getClientName());
            sharedpref.setStringPreferences(MyPreferenceManager.Client_name, siteDetailModel.getClientName());
            sharedpref.setStringPreferences(MyPreferenceManager.Site_name, siteDetailModel.getSiteName());
            sideName.setText(siteDetailModel.getSiteName());
            contactPersonName.setText(siteDetailModel.getSiteContact());
            siteAddress.setText(siteDetailModel.getSiteaddress());
            contactnumber.setText("" + siteDetailModel.getSiteMobileNo());
            sharedpref.setIntPreferences(MyPreferenceManager.Siteid, siteDetailModel.getSiteId());
            txtpaymentmode.setText(siteDetailModel.getPayment_mode());
            txtquantity.setText(siteDetailModel.getQuantity());
            txtmaterial.setText(siteDetailModel.getMaterialName());
            txtsitepersonName.setText(siteDetailModel.getContactPersonForCollection());
            txtsitepersonConatct.setText(siteDetailModel.getContactNoForCollection());
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("a", String.valueOf(sharedpref.getBooleanPreferences(MyPreferenceManager.check_out_bit)));
                SharedPreference.getInstanceProfileData(getActivity().getApplication()).setCheckIn("1");
                makeCheckInRequest();

            }
        });
    }

    private void getLatNLong() {
        tracker = new GPSTracker(getActivity());
        if (tracker.canGetLocation()) {
            Location currentLocation = tracker.getLocation();

            if (currentLocation != null && siteDetailModel.getSiteLongitude() == 0 && siteDetailModel.getSiteLongitude() == 0) {
                latitude = currentLocation.getLatitude();
                longitude = currentLocation.getLongitude();

            }
        }
    }


    private void makeCheckInRequest() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("driverId", SharedPreference.getInstanceProfileData(getActivity()).getUserId());
        hashMap.put("siteId", "" + siteDetailModel.getSiteId());
        hashMap.put("enquiry_id", "" + siteDetailModel.getEnquiry_id());
        if (latitude != 0 && longitude != 0) {
            hashMap.put("lat", latitude.toString());
            hashMap.put("long", longitude.toString());
        } else {
            hashMap.put("lat", "");
            hashMap.put("long", "");

    }
        RequestHandler.makeWebservice(true, getActivity(),
                Request.Method.POST, URLS.getInstance().getCheckIn, hashMap,
                CheckInOUTModel[].class, new VolleyResponseListener<CheckInOUTModel>() {
            @Override
            public void onResponse(CheckInOUTModel[] object) {
                if (object[0] instanceof CheckInOUTModel) {
                    CheckInOUTModel checkInOUTModel = (CheckInOUTModel) object[0];
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.SITEID, siteDetailModel.getSiteId());
                    sharedpref.setBooleanPreferences(MyPreferenceManager.check_out_bit, true);
                    sharedpref.setBooleanPreferences(MyPreferenceManager.done_bit, true);
                    sharedpref.setStringPreferences(MyPreferenceManager.check_In_Id, checkInOUTModel.getResult());
                    Fragment selectMaterialFragment = new SelectionFragment();
                    selectMaterialFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().
                            beginTransaction().replace(R.id.content_frame, selectMaterialFragment).addToBackStack(null).commit();
                }
            }

            @Override
            public void onError(String message) {
                Utility.errorDialog(message, getActivity());

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Check In");
    }


}
