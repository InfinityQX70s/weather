
package com.infinity.weather.model.search;

import com.infinity.weather.model.search.Admin1;
import com.infinity.weather.model.search.Country;
import com.infinity.weather.model.search.PlaceTypeName;
import com.infinity.weather.model.search.Postal;
import com.infinity.weather.model.search.Timezone;

public class Place {

    private String woeid;
    private PlaceTypeName placeTypeName;
    private String name;
    private Country country;
    private Admin1 admin1;
    private Postal postal;
    private String areaRank;
    private String popRank;
    private Timezone timezone;


    public String getWoeid() {
        return woeid;
    }

    public PlaceTypeName getPlaceTypeName() {
        return placeTypeName;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }

    public Admin1 getAdmin1() {
        return admin1;
    }

    public Postal getPostal() {
        return postal;
    }

    public String getAreaRank() {
        return areaRank;
    }

    public String getPopRank() {
        return popRank;
    }

    public Timezone getTimezone() {
        return timezone;
    }

}
