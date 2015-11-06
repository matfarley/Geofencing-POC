package com.matthewfarley.geofencingpoc.Data;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by matthewfarley on 6/11/15.
 */
public class LocationConstants {

    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 100; // 100m

    public static final HashMap<String, LatLng>CLIENT_CINEMAS = new HashMap<String, LatLng>();
    public static final HashMap<String, LatLng>COMPETITOR_CINEMAS = new HashMap<String, LatLng>();
    public static final HashMap<String, LatLng>CLIENT_BILLBOARDS = new HashMap<String, LatLng>();

    public static final HashMap<String, LatLng>DOG_FOOD_LOCATIONS = new HashMap<String, LatLng>();


    // initializer
    static{
        createClientCinemasMap();
        createCompetitionCinemasMap();
        createClientBillboardsMap();
        createDogFoodMap();
    }

    private static void createClientCinemasMap(){
        CLIENT_CINEMAS.put("EVENT_QUEEN_ST", new LatLng(-36.8513906, 174.7638448));
        CLIENT_CINEMAS.put("EVENT_SYMONDS_ST", new LatLng(-36.8565631, 174.7638378));
        CLIENT_CINEMAS.put("EVENT_NEWMARKET", new LatLng(-36.8658641, 174.7763913));
    }

    private static void createCompetitionCinemasMap(){
        COMPETITOR_CINEMAS.put("HOYTS_WAIRAU_PARK", new LatLng(-36.765158, 174.7381793));

    }

    private static void createClientBillboardsMap(){
        CLIENT_BILLBOARDS.put("BUS_SHELTER_44_KYBER_PASS", new LatLng(-36.864147, 174.762055));
    }

    private static void createDogFoodMap(){
        DOG_FOOD_LOCATIONS.put("KYBER_PASS", new LatLng(-36.863833, 174.761024));
        DOG_FOOD_LOCATIONS.put("NEWTON_RD_AND_UPPER_QUEEN", new LatLng(-36.863236, 174.758563));
        DOG_FOOD_LOCATIONS.put("NEWTON_RD_BRIDGE", new LatLng(-36.861826, 174.754164));
        DOG_FOOD_LOCATIONS.put("NEWTON_RD_N_K_ROAD", new LatLng(-36.859721, 174.752734));
        DOG_FOOD_LOCATIONS.put("WESTERN_PARK", new LatLng(-36.858112, 174.749813));
        DOG_FOOD_LOCATIONS.put("FRANKLIN_RD", new LatLng(-36.853463, 174.745465));
        DOG_FOOD_LOCATIONS.put("HOME", new LatLng(-36.842886, 174.745715));



    }

}
