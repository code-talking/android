package com.business.e.bl.comassigntwo.gson;

import com.google.gson.annotations.SerializedName;

public class DailyForecast {

    @SerializedName("cond_code_d")
    public String condCode;

    @SerializedName("cond_txt_d")
    public String condDay;

    @SerializedName("cond_txt_n")
    public String condNight;

    @SerializedName("tmp_max")
    public String temperatureMax;

    @SerializedName("tmp_min")
    public String temperatureMin;

    @SerializedName("date")
    public String date;
}
