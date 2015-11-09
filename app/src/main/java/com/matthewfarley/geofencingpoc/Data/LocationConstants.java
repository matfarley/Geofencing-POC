package com.matthewfarley.geofencingpoc.Data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by matthewfarley on 6/11/15.
 */
public class LocationConstants {

    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float CINEMA_RADIUS_IN_METERS = 100; // 100m
    public static final float SIGNAGE_RADIUS_IN_METERS = 25; // 25m


    public static final HashMap<String, GeofenceData>CLIENT_CINEMAS = new HashMap<String, GeofenceData>();
    public static final HashMap<String, GeofenceData>COMPETITOR_CINEMAS = new HashMap<String, GeofenceData>();
    public static final HashMap<String, GeofenceData>CLIENT_BILLBOARDS = new HashMap<String, GeofenceData>();

    // initializer
    static{
        createClientCinemasMap();
        createCompetitionCinemasMap();
        createClientBillboardsMap();
    }

    private static void createClientCinemasMap(){
        ArrayList<GeofenceData>geofencedata = new ArrayList<>();
        geofencedata.add(new GeofenceData(new LatLng(-36.8513906, 174.7638448), CINEMA_RADIUS_IN_METERS, "EVENT_QUEEN_ST"));
        geofencedata.add(new GeofenceData(new LatLng(-36.8565631, 174.7638378), CINEMA_RADIUS_IN_METERS, "EVENT_SYMONDS_ST"));
        geofencedata.add(new GeofenceData( new LatLng(-36.8658641, 174.7763913), CINEMA_RADIUS_IN_METERS, "EVENT_NEWMARKET"));

        for (GeofenceData data:geofencedata ) {
            CLIENT_CINEMAS.put(data.getLocationName(), data);
        }
    }

    private static void createCompetitionCinemasMap(){
        ArrayList<GeofenceData>geofencedata = new ArrayList<>();

        geofencedata.add(new GeofenceData(new LatLng(-36.8516979,174.7653933), CINEMA_RADIUS_IN_METERS, "ACADEMY_CINEMAS"));

        for (GeofenceData data:geofencedata ) {
            COMPETITOR_CINEMAS.put(data.getLocationName(), data);
        }
    }

    private static void createClientBillboardsMap(){
        ArrayList<GeofenceData>geofencedata = new ArrayList<>();

        geofencedata.add(new GeofenceData( new LatLng(-36.864147, 174.762055), SIGNAGE_RADIUS_IN_METERS,"BUS_SHELTER_44_KYBER_PASS"));
        geofencedata.add(new GeofenceData( new LatLng(-36.8428513, 174.745582), SIGNAGE_RADIUS_IN_METERS,"BUS_SHELTER_66_KYBER_PASS"));
        geofencedata.add(new GeofenceData( new LatLng(-36.8619528, 174.7630136), SIGNAGE_RADIUS_IN_METERS,"BUS_SHELTER_157_SYMONDS_ST"));
        geofencedata.add(new GeofenceData( new LatLng(-36.8616917,174.7623002), SIGNAGE_RADIUS_IN_METERS,"BUS_SHELTER_SYMONDS ST OVERBRIDGE"));
        geofencedata.add(new GeofenceData( new LatLng(-36.8544543,174.7640275), SIGNAGE_RADIUS_IN_METERS,"BUS_SHELTER_380_QUEEN_ST"));
        geofencedata.add(new GeofenceData( new LatLng(-36.8488826,174.7663449), SIGNAGE_RADIUS_IN_METERS,"BUS_SHELTER_175_QUEEN_ST"));
        geofencedata.add(new GeofenceData( new LatLng(-36.8441862,174.7676217), SIGNAGE_RADIUS_IN_METERS,"BUS_SHELTER_BRITOMART"));

        for (GeofenceData data:geofencedata ) {
            CLIENT_BILLBOARDS.put(data.getLocationName(), data);
        }
    }
}
