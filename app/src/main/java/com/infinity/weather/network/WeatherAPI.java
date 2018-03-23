package com.infinity.weather.network;

import com.infinity.weather.model.latlong.LatLong;
import com.infinity.weather.model.search.Search;
import com.infinity.weather.model.weather.Weather;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by m.mazurkevich on 05.06.15.
 */
public interface WeatherAPI {

    @GET("/public/yql")
    public Weather getWeatherInform(@QueryMap Map<String, String> parameters);

    @GET("/public/yql")
    public Search getCityList(@QueryMap Map<String, String> parameters);

    @GET("/public/yql")
    public LatLong getWoeidByLatLong(@QueryMap Map<String, String> parameters);

}
