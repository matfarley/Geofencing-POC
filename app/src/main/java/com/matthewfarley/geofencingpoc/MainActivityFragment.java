package com.matthewfarley.geofencingpoc;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matthewfarley.geofencingpoc.Events.GeofenceTransitionEnterEvent;
import com.matthewfarley.geofencingpoc.Events.GeofenceTransitionExitEvent;
import com.squareup.otto.Subscribe;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private TextView mTextView;
    private String TEXT_VIEW_KEY = "textView key";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        // Add injection with ButterKnife.
        mTextView = (TextView)view.findViewById(R.id.main_fragment_text_view);
        if (savedInstanceState != null){
            mTextView.setText(savedInstanceState.getString(TEXT_VIEW_KEY));
        }

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        // TODO: update this with injection
        BusProvider.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        // TODO: update this with injection
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_VIEW_KEY, mTextView.getText().toString());
    }

    @Subscribe
    public void onGeofenceTransitionEnter(GeofenceTransitionEnterEvent event){
        mTextView.setText(mTextView.getText() + "\n" + event.getTransitionSummary());
    }

    @Subscribe
    public void onGeofenceTransitionExit(GeofenceTransitionExitEvent event){
        mTextView.setText(mTextView.getText() + "\n" + event.getTransitionSummary());
    }
}
