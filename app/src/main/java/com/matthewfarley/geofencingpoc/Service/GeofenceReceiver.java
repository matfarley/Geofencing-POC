package com.matthewfarley.geofencingpoc.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.matthewfarley.geofencingpoc.BusProvider;
import com.matthewfarley.geofencingpoc.Events.GeofenceTransitionEnterEvent;
import com.matthewfarley.geofencingpoc.Events.GeofenceTransitionExitEvent;
import com.matthewfarley.geofencingpoc.MainActivity;
import com.matthewfarley.geofencingpoc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewfarley on 6/11/15.
 */
public class GeofenceReceiver extends BroadcastReceiver {

    String TAG = GeofenceReceiver.class.getSimpleName();
    Context context;
    Intent broadcastIntent = new Intent();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()){
            handleGeofencingError(geofencingEvent);
        }else{
            handleGeofencingEvent(geofencingEvent);
        }
    }

    void handleGeofencingError(GeofencingEvent geofencingEvent){
        Log.d(TAG, getErrorString(geofencingEvent.getErrorCode()));
    }

    void handleGeofencingEvent(GeofencingEvent geofencingEvent){
        // Get transition Type
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
        String infoString = getTriggeringGeofencesString(geofenceTransition, triggeringGeofences);

        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            // handle enter
            Log.i(TAG, infoString);
            handleEntryTransition(infoString);
        } else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            // handle exit
            Log.i(TAG, infoString);
            handleExitTransition(infoString);
        }else{
            //Error
            Log.e(TAG, infoString);
        }
    }

    // TODO: Handle these with Otto!
    private void handleEntryTransition(String notificationString){
        sendNotification(notificationString);
        // Inject Bus to class
        BusProvider.getInstance().post(new GeofenceTransitionEnterEvent(notificationString));
    }

    private void handleExitTransition(String notificationString){
        sendNotification(notificationString);
        // Inject Bus to class
        BusProvider.getInstance().post(new GeofenceTransitionExitEvent(notificationString));
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails){
        // Used to star the main activity
        Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class);

        // Build a task stack and add the intent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        // Define Notification
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Click notification to return to app")
                .setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, builder.build());
    }

    private String getTriggeringGeofencesString(int transition, List<Geofence> triggeringGeofencesList){
        String geofenceTransitionString = getTransitionString(transition);
        ArrayList triggeringGeofenceIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofencesList){
            triggeringGeofenceIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofenceIdsList);
        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }


    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Geofence Transition Enter";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Geofence Transition Exit";
            default:
                return "Unknown Geofence Transition";
        }
    }

    // TODO: Move this somewhere more appropriate
    public static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "Geofence Not Available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error";
        }
    }
}
