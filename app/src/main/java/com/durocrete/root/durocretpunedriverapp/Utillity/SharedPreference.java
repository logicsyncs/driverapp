package com.durocrete.root.durocretpunedriverapp.Utillity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.durocrete.root.durocretpunedriverapp.BuildConfig;
import com.durocrete.root.durocretpunedriverapp.comman.Constants;
import com.durocrete.root.durocretpunedriverapp.model.SiteDetailModel;

import java.util.ArrayList;


public class SharedPreference {
    private static String TAG = SharedPreference.class.getSimpleName();
    private static final String USER_ID = "USERID";
    private static final String ROUTE_ID = "ROUTEID";
    private static final String CHECK_IN = "CHECKIN";
    private static final String REFERENCE_KEY = "REFERENCE";

    private String mUserID = "";
    private String mRouteID = "";
    private String mCheckIn = "";

    private SharedPreferences mPrefs;
    private Context mContext;
    SharedPreferences.Editor mEditor;
    private boolean mIsLogin = false;
    private static SharedPreference mSharedPreference;


    private SharedPreference(Context aContext) {
        mContext = aContext;
        mPrefs = mContext.getSharedPreferences(Constants.SH_SHARED_PREF ,
                Context.MODE_MULTI_PROCESS);
        mEditor = mPrefs.edit();
        loadData();
    }

    public static SharedPreference getInstanceProfileData(Context aContext) {

        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(aContext);
        }
        return mSharedPreference;

    }

    public void setReference(Context context, String referenceData) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SH_SHARED_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(REFERENCE_KEY, referenceData);
        editor.apply();
    }

    public String getReference(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SH_SHARED_PREF, Context.MODE_PRIVATE);
        return prefs.getString(REFERENCE_KEY, "");
    }

    public void loadData() {
        mUserID = mPrefs.getString(USER_ID, "");
        mRouteID = mPrefs.getString(ROUTE_ID, "");
        mCheckIn =mPrefs.getString(CHECK_IN,"");
        mSharedPreference = this;
    }
    public String getCheckIn() {
        Log.v("ABC" ,mCheckIn);
        return mCheckIn;
    }

    public void setCheckIn(String checkIn) {
        Log.v("ABC" ,checkIn);
        mEditor.putString(CHECK_IN, checkIn);
        mEditor.commit();
        loadData();
    }
    public String getUserId() {
        return mUserID;
    }

    public void setUserId(String userId){
        mEditor.putString(USER_ID, userId);
        mEditor.commit();
        loadData();
    }

    public String getRouteId() {
        return mRouteID;
    }

    public void setRouteId(String routeId){
        mRouteID = routeId;
        mEditor.putString(ROUTE_ID, routeId);
        mEditor.commit();
        loadData();
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
        loadData();
    }

    /**
     * For Storing the data into SharedPreference
     *
     * @param context
     * @param key
     * @param value
     */
    public void setSharedPreference(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SH_SHARED_PREF + BuildConfig.VERSION_CODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    /**
     * for getting the data which stored in the shared preference
     *
     * @param context
     * @param key
     * @return
     */
    public static String getSharedPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SH_SHARED_PREF + BuildConfig.VERSION_CODE, Context.MODE_PRIVATE);
        String prefData = prefs.getString(key, "");
        return prefData;
    }

    public void setSharedPrefPickUpPointData(Context context, String key, ArrayList<SiteDetailModel> listHomeMakeData) {
        Log.v(TAG," setSharedPrefPickUpPointData : " + listHomeMakeData.size());
        String value;
        try {
            value = ObjectSerializer.serialize(listHomeMakeData);
            Log.v(TAG," setSharedPrefHomeMakeData : " + value);
            setSharedPreference(context,key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<SiteDetailModel> getSharedPrefPickUpPointData(Context context, String key){
        try {
            String strSpMakeData = getSharedPreference(context,key);
            Log.v(TAG, "strSpPickUpPointData :" + strSpMakeData);
            if (strSpMakeData != null && strSpMakeData.trim().length() > 0) {
                ArrayList<SiteDetailModel> listHomeMakeData = (ArrayList<SiteDetailModel>) ObjectSerializer
                        .deserialize(strSpMakeData);
                Log.v(TAG, "strSpPickUpPointData 1:" + listHomeMakeData);
                return listHomeMakeData;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

