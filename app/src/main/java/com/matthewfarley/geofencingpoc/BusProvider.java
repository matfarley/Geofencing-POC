package com.matthewfarley.geofencingpoc;

import com.squareup.otto.Bus;

/**
 * Created by matthewfarley on 6/11/15.
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }

}
