package com.matthewfarley.geofencingpoc.Events;

/**
 * Created by matthewfarley on 6/11/15.
 */
public class GeofenceTransitionEnterEvent {
    private String transitionSummary;

    public GeofenceTransitionEnterEvent(String transitionSummary){
        this.transitionSummary = transitionSummary;
    }

    public String getTransitionSummary() {
        return transitionSummary;
    }
}
