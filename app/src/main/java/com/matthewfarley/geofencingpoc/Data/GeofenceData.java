package com.matthewfarley.geofencingpoc.Data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matthewfarley on 9/11/15.
 */
public class GeofenceData {
    private LatLng latLng;
    private float radius;
    private String locationName;

    public GeofenceData(LatLng latLng, float radius, String locationName) {
        super();
        this.latLng = latLng;
        this.radius = radius;
        this.locationName = locationName;

    }

    public LatLng getLatLng() {
        return latLng;
    }

    public float getRadius() {
        return radius;
    }

    public String getLocationName() {
        return locationName;
    }
}
