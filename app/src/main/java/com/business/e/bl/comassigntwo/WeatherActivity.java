package com.business.e.bl.comassigntwo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.e.bl.comassigntwo.gson.DailyForecast;
import com.business.e.bl.comassigntwo.gson.Suggestion;
import com.business.e.bl.comassigntwo.gson.WeatherFuture;
import com.business.e.bl.comassigntwo.gson.WeatherNow;
import com.business.e.bl.comassigntwo.gson.WeatherSuggestions;
import com.business.e.bl.comassigntwo.materials.Dict;
import com.business.e.bl.comassigntwo.util.HttpUtil;
import com.business.e.bl.comassigntwo.util.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String COUNTY_NAME = "countyName";

    private static final String WEATHER_FUTURE_BASE = Dict.WEATHER_FUTURE_BASE;

    private static final String WEATHER_NOW_BASE = Dict.WEATHER_NOW_BASE;

    private static final String HEWEATHER_KEY = Dict.HEWEATHER_KEY;

    private static final int DEFAULT_DAILY_FORECAST_NUMBER = 3;
    private static final int REFRESH = 1;
    protected static final int LOAD = 0;

    private static final String OK = "ok";

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView feelingTemperature;

    private TextView humidity;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    public DrawerLayout drawerLayout;

    private Button navButton;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipte_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);

        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);

        /**
         *  详情布局
         */
        feelingTemperature = (TextView) findViewById(R.id.feeling_temp);
        humidity = (TextView) findViewById(R.id.hum_text);

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);

        loadPic();

        final String countyName = getIntent().getStringExtra(COUNTY_NAME);
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(countyName, LOAD);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(countyName, REFRESH);

                loadPic();
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                loadPic();
            }
        });
    }

    public void requestWeather(String countyName, int source) {
        requestWeatherNow(countyName);
        requestWeatherFuture(countyName);


        if (source == REFRESH) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WeatherActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 目前只尝试了请求weatherNow这个类
     * 成功
     *
     * @param countyName
     */
    public void requestWeatherNow(String countyName) {
        String url = WEATHER_NOW_BASE + countyName + HEWEATHER_KEY;
        HttpUtil.sendOkHttpRequest(url, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "Loading data failed...", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherNowResponseText = response.body().string();
                final WeatherNow weatherNow = Utility.handleWeatherNowResponse(weatherNowResponseText);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeatherNowInfo(weatherNow);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * @param countyName
     */
    public void requestWeatherFuture(String countyName) {
        String url = WEATHER_FUTURE_BASE + countyName + HEWEATHER_KEY;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "Loading forecast failure...", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherFutureResponseText = response.body().string();
                final WeatherFuture weatherFuture = Utility.handleWeatherFeatureResponse(weatherFutureResponseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherFuture != null && OK.equals(weatherFuture.status)) {
                            showWeatherFutureInfo(weatherFuture);
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    private void showWeatherFutureInfo(WeatherFuture weatherFuture) {
        List<DailyForecast> lst = weatherFuture.futures;
        forecastLayout.removeAllViews();

        for (int i = lst.size() - DEFAULT_DAILY_FORECAST_NUMBER; i < lst.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxTemp = (TextView) view.findViewById(R.id.max_text);
            TextView minTemp = (TextView) view.findViewById(R.id.min_text);

            dateText.setText(lst.get(i).date);
            infoText.setText(lst.get(i).condDay);
            maxTemp.setText(lst.get(i).temperatureMax + "°C");
            minTemp.setText(lst.get(i).temperatureMin + "°C");

            forecastLayout.addView(view);
        }

    }


    private void showWeatherNowInfo(WeatherNow weatherNow) {
        String cityName = weatherNow.basic.cityName;
        String temp = weatherNow.update.getLocalTime();
        String updateTime = temp.substring(0, temp.lastIndexOf(":"));
        String degree = weatherNow.now.temperature + "°C";
        String feelingTemp = weatherNow.now.feelingTemperature;
        String humid = weatherNow.now.humidity;
        String weatherInfo = weatherNow.now.contTxt;

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        feelingTemperature.setText(feelingTemp);
        humidity.setText(humid);
        weatherInfoText.setText(weatherInfo);

        weatherLayout.setVisibility(View.VISIBLE);

    }

    private void loadPic() {
//        String serverURL = Config.getRequestURL();
        String picUrl = Config.getUnSplashPic();

        HttpUtil.sendOkHttpRequest(picUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String url = response.toString();

//                final String s = getURL(url);
//                final String s = "https://images.unsplash.com/photo-1568849454420-6b573584a5ed?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max";

                final String s = getUnsplashUrl(url);
//                /**
//                 *  用最笨的办法解析出来了url
//                 */
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("url = " + url);
                System.out.println("s = " + s);
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                final String image = "https://picjumbo.com/wp-content/uploads/northernlights-reflection-on-a-lake-1080x1620.jpg";

                System.out.println("image : " + image);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Glide.with(WeatherActivity.this).load(image).into(bingPicImg);
                        Glide.with(WeatherActivity.this).load(s).into(bingPicImg);
                    }
                });
            }
        });
    }

    private String getUnsplashUrl(String s) {
        int idx = s.indexOf("url");
        String rst = s.substring(idx + 4, s.length() - 1);
        return rst;
    }

    private String getURL(String s) {
        int idx = s.indexOf(",");
        int last = s.length() - 2;
        String rst = s.substring(idx + 12, last);
        return rst;
    }
}
