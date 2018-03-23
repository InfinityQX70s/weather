package com.infinity.weather.network;

import com.infinity.weather.model.search.Search;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m.mazurkevich on 06.08.15.
 */
public class SearchRequest extends RetrofitSpiceRequest<Search,WeatherAPI> {

    public SearchRequest() {
        super(Search.class, WeatherAPI.class);
    }

    @Override
    public Search loadDataFromNetwork() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", "select * from geo.places where text="+"\"Орел\"");
        params.put("format", "json");
        return getService().getCityList(params);
    }
}
