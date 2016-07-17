package com.cloudprint.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.cloudprint.CloudPrint;
import com.cloudprint.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//
//        Location location = locationManager.getLastKnownLocation(locationManager
//                .getBestProvider(criteria, false));
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
        mMap = googleMap;
        LatLng sydney = new LatLng(-33.867, 151.206);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        Location location = googleMap.getMyLocation();
//        if (location != null) {
//
//            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
//            String message = String.format("latitude = %f longitude = %f", location.getLatitude(), location.getLongitude());
//            Log.i("Location", message);
////            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
//        } else {
//            Log.i("location", "Its null!!!!!!!!!!!!!");
//        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3404372, -121.8976136))
                .title("Vila Torino")
                .snippet("this shows desciption of place"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.343329, -121.8915832))
                .title("Mi Pueblo")
                .snippet("this shows desciption of place"));
        Log.v("Marker Error", "Set near Marker");
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //mapBelowLinearLayout.setVisibility(LinearLayout.VISIBLE);
                CloudPrint.showToast("This is marker toast");
                return false;
            }
        });
    }
}
