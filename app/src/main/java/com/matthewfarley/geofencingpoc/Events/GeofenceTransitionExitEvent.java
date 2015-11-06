package com.matthewfarley.geofencingpoc.Events;

/**
 * Created by matthewfarley on 6/11/15.
 */
public class GeofenceTransitionExitEvent {

    private String transitionSummary;

    public GeofenceTransitionExitEvent(String transitionSummary){
        this.transitionSummary = transitionSummary;
    }

    public String getTransitionSummary() {
        return transitionSummary;
    }
}
