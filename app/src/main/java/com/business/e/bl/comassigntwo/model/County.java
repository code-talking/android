package com.business.e.bl.comassigntwo.model;

public class County {

    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;



    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setWeatherId(String weatherId) {

        this.weatherId = weatherId;
    }

    public void setCountyName(String countyName) {

        this.countyName = countyName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public String getCountyName() {
        return countyName;
    }

    public int getId() {
        return id;
    }
}
