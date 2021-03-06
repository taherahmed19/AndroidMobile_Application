package com.example.myapplication.Models.CurrentLocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Interfaces.CurrentLocationListener.CurrentLocationListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class CurrentLocation {

    Activity context;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    CurrentLocationListener currentLocationListener;

    public CurrentLocation(Activity context, CurrentLocationListener currentLocationListener) {
        this.context = context;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        this.currentLocationListener = currentLocationListener;
    }

    public CurrentLocation(Context context, CurrentLocationListener currentLocationListener) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        this.currentLocationListener = currentLocationListener;
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            currentLocationListener.updateUserLocation(locationResult.getLastLocation());
        }
    };

    public LatLng accessGeolocation() {
        return new LatLng(52.500426, -1.969457);
    }

    public void startLocationUpdates() {
        if(context != null){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                    } else {
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                    }

//                    fusedLocationProviderClient.getLastLocation()
//                            .addOnSuccessListener(new OnSuccessListener<Location>() {
//
//                                @Override
//                                public void onSuccess(Location location) {
//
//                                    Log.d("Location",currentLocation.toString());
//
//                                }
//                            });

              //  }
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
