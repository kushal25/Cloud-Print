package com.cloudprint.Activities;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.cloudprint.CloudPrint;
import com.cloudprint.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if(CloudPrint.isGPSEnabled())
            {
                googleMap.setMyLocationEnabled(true);
                Location location = googleMap.getMyLocation();
                if (location != null) {

                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                    String message = String.format("latitude = %f longitude = %f", location.getLatitude(), location.getLongitude());
//                    Log.i("Location", message);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
                } else {
                    Log.i("location", "Its NULL");
                }

                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                googleMap.getUiSettings().setScrollGesturesEnabled(true);

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.3404372, -121.8976136))
                        .title("Vila Torino")
                        .snippet("this shows desciption of place"));

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.343329, -121.8915832))
                        .title("Mi Pueblo")
                        .snippet("this shows desciption of place"));
            }
            else
            {
                CloudPrint.showToast("Enable GPS");
            }
        }
        else
        {
            CloudPrint.showToast("Need Location Permission");
        }
    }
}
