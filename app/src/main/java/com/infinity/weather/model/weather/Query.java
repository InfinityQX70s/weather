package com.infinity.weather.model.weather;


public class Query {

    private Integer count;
    private String created;
    private String lang;
    private Results results;

    public Integer getCount() {
        return count;
    }

    public String getCreated() {
        return created;
    }

    public String getLang() {
        return lang;
    }

    public Results getResults() {
        return results;
    }

}
