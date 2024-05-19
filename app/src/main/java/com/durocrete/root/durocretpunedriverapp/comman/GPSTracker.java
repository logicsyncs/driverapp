package com.durocrete.root.durocretpunedriverapp.comman;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.durocrete.root.durocretpunedriverapp.R;


public class GPSTracker extends Service implements LocationListener {
    private String TAG = GPSTracker.class.getSimpleName();
    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private boolean isGPSEnable = false;
    private boolean isNetworkEnable = false;
    String mprovider;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Log.v(TAG, " Both are not available :" + " isGPSEnable : " + isGPSEnable + " isNetworkEnable : " + isNetworkEnable);
            new AlertDialog.Builder(mContext)
                    .setTitle("GPS Connection Not Available")
                    .setMessage(
                            "Please Go to Setting And unable Gps location on High accuracy mode.")
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {


                                }
                            })

                    .setIcon(R.drawable.warning).show();
        } else {
            if (isNetworkEnable) {
                location = null;
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, (LocationListener) this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                } catch (SecurityException e) {
                }

                if (location != null) {
                    this.canGetLocation = true;
                    Log.v(TAG, "latitude1 : " + location.getLatitude() + "");
                    Log.v(TAG, "longitude1: " + location.getLongitude() + "");

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                return location;
            }


            if (isGPSEnable) {
                location = null;
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                } catch (SecurityException e) {
                }
                if (location != null) {
                    this.canGetLocation = true;
                    Log.v("latitude2 : ", location.getLatitude() + "");
                    Log.v("longitude2 : ", location.getLongitude() + "");
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                return location;
            }
        }
        return null;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(GPSTracker.this);
            } catch (SecurityException e) {
                Log.v(TAG, e.toString());
            }
        }

    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
     /*try {
            locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);
			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.v(TAG , "isGPSEnabled : " + isGPSEnabled);

			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				this.canGetLocation = true;
				if (location == null) {
					try {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}

					} catch (SecurityException e) {

					}

				}
			}else {
				new AlertDialog.Builder(mContext)
						.setTitle("GPS Connection Not Available")
						.setMessage(
								"Please Go to Setting And unable Gps location on High accuracy mode.")
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {


									}
								})

						.setIcon(R.drawable.warning).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/

}
