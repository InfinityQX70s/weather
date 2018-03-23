package com.infinity.weather.network;

import com.infinity.weather.model.weather.Weather;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by m.mazurkevich on 05.06.15.
 */
public class WeatherRequest extends RetrofitSpiceRequest<Weather,WeatherAPI> {

    private String mWoeid;

    public WeatherRequest(String woeid) {
        super(Weather.class, WeatherAPI.class);
        mWoeid = woeid;
    }

    @Override
    public Weather loadDataFromNetwork() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", "select * from weather.forecast where woeid ="+mWoeid);
        params.put("format", "json");
        return getService().getWeatherInform(params);
    }
}
