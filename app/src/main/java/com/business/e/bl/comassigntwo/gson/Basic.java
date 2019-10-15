package com.business.e.bl.comassigntwo.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("location")
    public String countyName;

    @SerializedName("parent_city")
    public String cityName;

    @SerializedName("admin_area")
    public String provinceName;
}
