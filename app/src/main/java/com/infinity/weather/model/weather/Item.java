
package com.infinity.weather.model.weather;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private String title;
    private String lat;
    @SerializedName("long")
    private String _long;
    private String link;
    private String pubDate;
    private Condition condition;
    private String description;
    private List<Forecast> forecast = new ArrayList<Forecast>();
    private Guid guid;

    public String getTitle() {
        return title;
    }

    public String getLat() {
        return lat;
    }

    public String getLong() {
        return _long;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Condition getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public Guid getGuid() {
        return guid;
    }

}
