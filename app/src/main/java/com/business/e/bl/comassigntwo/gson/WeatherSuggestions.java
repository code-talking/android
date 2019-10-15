package com.business.e.bl.comassigntwo.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherSuggestions {

    public Basic basic;

    public String status;

    public Update update;

    /**
     *  必须把key写上，才能解析出对应的value
     */
    @SerializedName("lifestyle")
    public List<Suggestion> suggestions;
}
