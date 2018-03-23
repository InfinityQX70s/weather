package com.infinity.weather.network;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by m.mazurkevich on 05.06.15.
 */
public class WeatherService extends RetrofitGsonSpiceService {

    private final static String END_POINT = "https://query.yahooapis.com/v1";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(WeatherAPI.class);
    }

    @Override
    protected String getServerUrl() {
        return END_POINT;
    }
}
