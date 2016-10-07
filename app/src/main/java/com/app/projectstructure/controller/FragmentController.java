package com.app.projectstructure.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.app.projectstructure.R;
import com.app.projectstructure.constant.AppConstant;
import com.app.projectstructure.gpstracking.GPSTracker;

/**
 * Created by vishal on 14/9/16.
 */
public class FragmentController extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    public    Context         mContext;
    protected LocationManager locationManager;
    ProgressDialog pDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void startProgressBar() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("loading....");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismissProgressBar() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void alertForExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.msg_ask_to_exit_from_app)
                .setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //clear userid here
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ((Activity) mContext).finishAffinity();
                        } else {

                            ((Activity) mContext).finish();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean checkInternetConnection() {
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


    public void alertWIthPositiveNegativeBtn(String alertTitle, String alertMsg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle(alertTitle);
        builder1.setMessage(alertMsg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void alertConfirmationWithIntent(String alertTitle, String alertMsg, final Intent intent) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle(alertTitle);
        builder1.setMessage(alertMsg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity) mContext).finish();
                        mContext.startActivity(intent);
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void successAlarmWithPositiveBtn(String alertMsg) {
        String              alertTitle = getString(R.string.tag_message);
        AlertDialog.Builder builder1   = new AlertDialog.Builder(mContext);
        builder1.setTitle(alertTitle);
        builder1.setMessage(alertMsg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ((Activity) mContext).finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void alertWithPositiveBtn(String alertMsg) {
        String              alertTitle = getString(R.string.tag_message);
        AlertDialog.Builder builder1   = new AlertDialog.Builder(mContext);
        builder1.setTitle(alertTitle);
        builder1.setMessage(alertMsg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public boolean isGPSEnabled() {
        boolean gps_enabled = false, network_enabled = false;
        boolean status      = false;
        if (locationManager == null)
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            status = false;

        } else {
            status = true;
        }
        return status;
    }

    public boolean hasDeviceTokenPermission() {
        boolean permissionStatus           = false;
        int     hasWriteContactsPermission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            permissionStatus = false;
           /* ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;*/
        } else {
            permissionStatus = true;

        }
        return permissionStatus;
    }

    public void askToEnableGPS() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage("Please activate your GPS to get nearest deals");
        dialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();
    }

    public void getCurrentLatLong() {
        if (isGPSEnabled()) {
            int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCorseLocationPermission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED && hasCorseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                                  AppConstant.MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                return;
            } else {

                GPSTracker gps = new GPSTracker(mContext);
                if (gps.canGetLocation()) {
                    double lat = gps.getLatitude(); // returns latitude
                    double lng = gps.getLongitude(); // returns longitude
                    Log.e(TAG, "lat=" + lat + "  long=" + lng);
                }
            }
        } else {
            askToEnableGPS();
        }

    }

}
