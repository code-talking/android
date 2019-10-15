package com.business.e.bl.comassigntwo.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherFuture {
    public Basic basic;

    public String status;

    public Now now;

    @SerializedName("daily_forecast")
    public List<DailyForecast> futures;
}
