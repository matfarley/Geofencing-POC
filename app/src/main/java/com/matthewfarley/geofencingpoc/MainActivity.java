package com.matthewfarley.geofencingpoc;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matthewfarley.geofencingpoc.Data.GeofenceData;
import com.matthewfarley.geofencingpoc.Data.LocationConstants;
import com.matthewfarley.geofencingpoc.Service.GeofenceReceiver;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    static final String TAG = MainActivity.class.getSimpleName();
    protected ArrayList<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mGeofencingPendingIntent;
    private Fragment mContent;
    private SupportMapFragment mMapFragment;
    private String FRAGMENT_CONTENT_TAG = "fragment_content_tag";
    private CameraPosition initialMapCameraPosition = new CameraPosition.Builder()
            .target(new LatLng(-36.8643942, 174.7619292))
            .zoom(13.0f).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState != null){
//            mContent = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_CONTENT_TAG);
//        }else {
//            mContent = new MainActivityFragment();
//        }
//
        mMapFragment = SupportMapFragment.newInstance();
        mMapFragment.getMapAsync(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.add(R.id.container, mContent);
        transaction.add(R.id.container, mMapFragment);
        transaction.commit();


        mGeofenceList = new ArrayList<>();

        populateGeofenceList(LocationConstants.CLIENT_CINEMAS);
        populateGeofenceList(LocationConstants.CLIENT_BILLBOARDS);


        buildGoogleApiClient();


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Google Play Services API Callbacks
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Google Play Services Connected");
        Log.d(TAG, "Adding Geofences");
        addGeofences();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Google Play Services Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Google Play Services Connection Failed");

    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    private void populateGeofenceList(Map<String, GeofenceData> data){

             for (Map.Entry<String, GeofenceData> entry : data.entrySet()){
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(entry.getKey())
                        .setCircularRegion(
                                entry.getValue().getLatLng().latitude,
                                entry.getValue().getLatLng().longitude,
                                entry.getValue().getRadius()
                        )
                        .setExpirationDuration(LocationConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());
        }
    }

    public void addGeofences(){
        if(!mGoogleApiClient.isConnected()){
            Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            );
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    // TODO: Do we need to remove fences?

    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        // Trigger to fire, if user is inside the fence when request is added.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(){
        if (mGeofencingPendingIntent == null){
//            Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
//            mGeofencingPendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intent = new Intent(this, GeofenceReceiver.class);
            mGeofencingPendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return mGeofencingPendingIntent;
    }

    // TODO: Handle restoring fragments properly

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        getSupportFragmentManager().putFragment(outState, FRAGMENT_CONTENT_TAG, mContent);
//        super.onSaveInstanceState(outState);
//        Log.i(TAG, "onSaveInstanceState Called");
//    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy Called");
    }

    // Used to visualise the Geofences.
    @Override
    public void onMapReady(GoogleMap googleMap) {

        addMarkers(googleMap, LocationConstants.CLIENT_CINEMAS);
        addCircle(googleMap, LocationConstants.CLIENT_CINEMAS, Color.parseColor("#80008000"));

        addMarkers(googleMap, LocationConstants.CLIENT_BILLBOARDS);
        addCircle(googleMap, LocationConstants.CLIENT_BILLBOARDS, Color.parseColor("#800000FF"));

        addMarkers(googleMap, LocationConstants.COMPETITOR_CINEMAS);
        addCircle(googleMap, LocationConstants.COMPETITOR_CINEMAS, Color.parseColor("#80FF0000"));

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(initialMapCameraPosition));
    }

    private void addMarkers(GoogleMap googleMap, Map<String, GeofenceData>data){

        for (Map.Entry<String, GeofenceData> entry : data.entrySet()) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(entry.getValue().getLatLng())
                    .title(entry.getKey()));
            marker.showInfoWindow();
        }
    }

    private void addCircle(GoogleMap googleMap, Map<String, GeofenceData>data, int fillColor){

        for (Map.Entry<String, GeofenceData> entry : data.entrySet()) {
            googleMap.addCircle(new CircleOptions()
                    .center(entry.getValue().getLatLng())
                    .radius(entry.getValue().getRadius())
                    .fillColor(fillColor)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(2));
        }
    }

}
