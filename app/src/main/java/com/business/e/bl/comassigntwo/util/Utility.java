package com.business.e.bl.comassigntwo.util;

import com.business.e.bl.comassigntwo.gson.WeatherFuture;
import com.business.e.bl.comassigntwo.gson.WeatherNow;
import com.business.e.bl.comassigntwo.gson.WeatherSuggestions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utility {

    public static final String TOP_KEY = "HeWeather6";

    /**
     *  解析实时天气
     * @param response
     * @return
     */
    public static WeatherNow handleWeatherNowResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(TOP_KEY);
            String nowContent = jsonArray.getJSONObject(0).toString();
            WeatherNow weatherNow = new Gson().fromJson(nowContent, WeatherNow.class);
            return weatherNow;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *  解析未来7天天气
     * @param response
     * @return
     */
    public static WeatherFuture handleWeatherFeatureResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(TOP_KEY);
            String featureContent = jsonArray.getJSONObject(0).toString();
            WeatherFuture weatherFuture = new Gson().fromJson(featureContent, WeatherFuture.class);
            return weatherFuture;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  解析生活建议
     * @param response
     * @return
     */
    public static WeatherSuggestions handleWeatherSuggestionsResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(TOP_KEY);
            String suggestionContent = jsonArray.getJSONObject(0).toString();
            WeatherSuggestions weatherSuggestions = new Gson().fromJson(suggestionContent, WeatherSuggestions.class);
            return weatherSuggestions;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
