package com.infinity.weather;

import com.infinity.weather.network.WeatherService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by m.mazurkevich on 06.08.15.
 */
public abstract class RoboSpiceActivity extends PlusClientActivity {
    private SpiceManager spiceManager = new SpiceManager(WeatherService.class);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
