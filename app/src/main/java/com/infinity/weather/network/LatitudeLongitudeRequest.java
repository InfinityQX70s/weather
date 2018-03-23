package com.infinity.weather.network;

import com.infinity.weather.model.latlong.LatLong;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m.mazurkevich on 06.08.15.
 */
public class LatitudeLongitudeRequest extends RetrofitSpiceRequest<LatLong,WeatherAPI> {

    private String mLat;
    private String mLong;

    public LatitudeLongitudeRequest(double lat, double longe) {
        super(LatLong.class, WeatherAPI.class);
        mLat = String.valueOf(lat);
        mLong = String.valueOf(longe);
    }

    @Override
    public LatLong loadDataFromNetwork() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", "select*from geo.placefinder where text=\"" + mLat + "," + mLong + "\" and gflags=\"R\"");
        params.put("format", "json");
        return getService().getWoeidByLatLong(params);
    }
}
