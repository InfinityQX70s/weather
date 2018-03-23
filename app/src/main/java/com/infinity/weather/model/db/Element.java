package com.infinity.weather.model.db;

/**
 * Created by m.mazurkevich on 17.08.15.
 */
public class Element {

    private int id;
    private String name;
    private String woeid;

    public Element() {
    }

    public Element(String woeid, String name) {
        this.name = name;
        this.woeid = woeid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

}
