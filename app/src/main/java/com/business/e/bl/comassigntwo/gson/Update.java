package com.business.e.bl.comassigntwo.gson;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Update {
    @SerializedName("loc")
    private String loc;

    @SerializedName("utc")
    public String utc;

    public static String getLocalTime() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(timeZone);
        return df.format(new Date());
    }
}
