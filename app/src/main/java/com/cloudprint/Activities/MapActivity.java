package com.cloudprint.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudprint.CloudPrint;
import com.cloudprint.Markers;
import com.cloudprint.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private SupportMapFragment mapFragment;
    private DrawerLayout drawer;
    private GoogleMap mMap;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private PlaceAutocompleteFragment autocompleteFragment;
    private String TAG = "Cloud Print";
    private LocationManager locationManager;
    private Criteria criteria;
    private Location location;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initViews();
        setSupportActionBar(toolbar);
        CloudPrint.isNetworkOK(MapActivity.this);
        mapFragment.getMapAsync(this);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,  drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(actionBarDrawerToggle);
        drawer.openDrawer(GravityCompat.START);
        navigationView.bringToFront();
        drawer.requestLayout();
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        fab.requestLayout();
        fab.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View view) {
                   Intent print = new Intent(MapActivity.this, MapListView.class);
                   startActivity(print);
                   }
            });


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());

                mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getAddress().toString()))
                        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13));
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void initViews() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
    }

    public void populateData(GoogleMap gMap) {
        final GoogleMap mMap = gMap;
        final List<String[]> data = CloudPrint.readCsv(getApplicationContext());
        MapActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 20; i++) {


                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(data.get(i)[26]), Double.parseDouble(data.get(i)[25])))
                            .title(data.get(i)[4])
                            .snippet(data.get(i)[5]+","+data.get(i)[6]));


                }
            }
        });
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap = googleMap;
            if (CloudPrint.isGPSEnabled()) {
                View locationButton = mapFragment.getView().findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                rlp.setMargins(0, 850, 180, 0);
                googleMap.setMyLocationEnabled(true);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();
                location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                if (location != null) {

                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
                } else {
                    Log.d(TAG, "Its NULL");
                }

                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                googleMap.getUiSettings().setScrollGesturesEnabled(true);
                populateData(mMap);

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.3404372, -121.8976136))
                        .title("Villa Torino")
                        .snippet("this shows desciption of place"));

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.343329, -121.8915832))
                        .title("Mi Pueblo")
                        .snippet("this shows desciption of place"));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        
                        Intent print = new Intent(MapActivity.this, PrintActivity.class);
                        startActivity(print);
                        return true;
                    }
                });
            } else {
                CloudPrint.showSnackBar(drawer, "Enable GPS");
            }
        }
        else {
            CloudPrint.showSnackBar(drawer, "Need Location Permission");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mapFragment.getMapAsync(this);
        CloudPrint.isNetworkOK(MapActivity.this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }  else if (id == R.id.nav_share) {
            String sharingMessage = "Welcome to Cloud Print application!";
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharingMessage);
            sendIntent.setType("text/plain");
            String shareTitle = "Cloud Print";
            startActivity(Intent.createChooser(sendIntent, shareTitle));

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
