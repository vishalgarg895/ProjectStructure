package com.app.projectstructure.gpstracking;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectstructure.apicallbacks.NetworkCallbackClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

/**
 * Created by betasoft on 12/8/16.
 *
 * Add this dependency in gradle file compile "com.google.android.gms:play-services:8.3.0"
 */
public class FuseLocationService extends Service implements LocationListener, NetworkCallbackClient.ApiRequestCompleteCallback {
    // Google client to interact with Google API
    private static String TAG = "FuseLocationService.class";
    private long SERVICE_REPEAT_TIME_MILLISECONDS = 1000 * 5;
    private GoogleApiClient mGoogleApiClient;
    Context mContext;
    private LocationRequest mLocationRequest;
    private TextView        latitude, longitude;

    public static double fusedLatitude = 0.0;
    public static double fusedLongitude = 0.0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (checkPlayServices()) {
            ////Log.e(TAG, "Google play service available!");
            startFusedLocation();
            registerRequestUpdate(this);
        }
        return START_STICKY;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    // check if google play services is installed on the device
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(),
                        "This device is supported. Please download google play services", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                //finish();
            }
            return false;
        }
        return true;
    }


    public void startFusedLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnectionSuspended(int cause) {
                            ////Log.e(TAG, "Connection is suspending!");

                        }

                        @Override
                        public void onConnected(Bundle connectionHint) {
                            ////Log.e(TAG, "Connection is Connected!");

                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                    mGoogleApiClient);
                            if (mLastLocation != null) {

                               // Toast.makeText(mContext, "Latitude:" + mLastLocation.getLatitude() + ", Longitude:" + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();

                            }

                        }
                    }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {

                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            ////Log.e(TAG, "Connection is Failed!");

                        }
                    }).build();
            mGoogleApiClient.connect();
        } else {
            mGoogleApiClient.connect();
        }
    }
    public void stopFusedLocation() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
    public void registerRequestUpdate(final LocationListener listener) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(SERVICE_REPEAT_TIME_MILLISECONDS); // every second

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, listener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!isGoogleApiClientConnected()) {
                        mGoogleApiClient.connect();
                    }
                    registerRequestUpdate(listener);
                }
            }
        }, 1000);
    }

    public boolean isGoogleApiClientConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = 0, lng = 0;
        lat = location.getLatitude();
        lng = location.getLongitude();

        Log.e(TAG,lat+"");
        Log.e(TAG,lng+"");

//        AppController.UserLatitude=lat;
//        AppController.UserLongitude=lng;

        setFusedLatitude(lat);
        setFusedLongitude(lng);
        if (lat != 0 && lng != 0) {
            callUpdateLatLongAPI(lat + "," + lng);
            ////Log.e(TAG, "New Location Received");
        } else {
            ////Log.e(TAG, "Location Not Received");
        }
        Log.i(TAG, "Latitude=" + lat);
        Log.i(TAG, "Longitude=" + lng);

    }

    private void callUpdateLatLongAPI(String latlong) {

       /* AppSharedPreference apppref = new AppSharedPreference(mContext);
        if (isConnectingToInternet()) {
            NetworkCallbackClient callbackClient = new NetworkCallbackClient(mContext, FuseLocationService.this, NetworkCallbackClient.APITAG_LOGIN);
            HashMap<String, String> param = new HashMap<>();
            param.put(AppConstants.PARAM_TOKEN, AppConstants.APP_API_TOKEN);
            param.put(AppConstants.PARAM_USER_ID, apppref.getSharedData(AppConstants.KEY_PREF_SERVER_USERID));
            param.put("latlng", latlong);
            callbackClient.callUpdateLatLongAPI(param);
        } else {

//            ////Log.e(TAG, "Location cordinate not update because Internet is not available.");

        }*/
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void setFusedLatitude(double lat) {
        fusedLatitude = lat;
    }

    public void setFusedLongitude(double lon) {
        fusedLongitude = lon;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onResponse(Object response, String requestType, boolean request_state, int statusCode) {
        // dismissProgressBar();
        if (statusCode == 200) {
           /* UpdateLatLongModel logoutModel = ((UpdateLatLongModel) response);
            if (logoutModel.getStatus().matches("1")) {
                //Log.e(TAG, "Successfully get update location..." + logoutModel.getMessage());
            } else {
                //Log.e(TAG, logoutModel.getMessage());
            }*/
        } else {
            //Log.e(TAG, "Failed to update location..");
        }
    }
}
