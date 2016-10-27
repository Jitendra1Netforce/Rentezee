package com.rentezee.helpers;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsApi;

public class LocationUtil {

    public final static int REQUEST_CHECK_SETTINGS = 0x1;
    public final static int REQUEST_CHECK_PERMISSION = 0x2;

    @SuppressWarnings("unchecked")
    public static void requestLocation(GoogleApiClient gac,
                                       LocationSettingsRequest lsr,
                                       ResultCallback rc) {
        SettingsApi api = LocationServices.SettingsApi;
        PendingResult<LocationSettingsResult> result = api
                .checkLocationSettings(gac, lsr);
        result.setResultCallback(rc);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void startLocationUpdates(Context context,
                                            GoogleApiClient gac,
                                            LocationRequest lr,
                                            LocationListener ll) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(gac, lr, ll);
    }

    public static void stopLocationUpdates(GoogleApiClient gac,
                                           LocationListener ll) {
        if (!gac.isConnected()) {
            return;
        }
        LocationServices.FusedLocationApi
                .removeLocationUpdates(gac, ll);
    }

    public static boolean checkSelfPermissionGranted(Context ctx, String permission) {
        return ActivityCompat.checkSelfPermission(ctx, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
