package com.business.e.bl.comassigntwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.business.e.bl.comassigntwo.materials.Dict;
import com.business.e.bl.comassigntwo.model.City;
import com.business.e.bl.comassigntwo.model.County;
import com.business.e.bl.comassigntwo.model.Province;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    public final String SPLITTER = "@@";

    //  geographic data
    public final String[] provinces = Dict.provinces;
    public final String[] cities = Dict.cities;
    public final String[] counties = Dict.counties;


    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    private final String CHINA = "CHINA";
    public StringBuilder sb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();

                } else if (currentLevel == LEVEL_COUNTY) {
                    String countyName = countyList.get(position).getCountyName();
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("countyName", countyName);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(countyName, activity.LOAD);
                    }



                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });

        queryProvinces();
    }

    private void queryProvinces() {
        titleText.setText(CHINA);
        backButton.setVisibility(View.GONE);

        provinceList = getProvinceList();

        dataList.clear();
        for (Province province : provinceList) {
            dataList.add(province.getProvinceName());
        }

        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_PROVINCE;

    }

    private List<Province> getProvinceList() {
        List<Province> rst = new ArrayList<>();
        for (String s : provinces) {
            Province province = new Province();
            province.setProvinceName(s);
            rst.add(province);
        }
        return rst;
    }

    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = getCityList();

        dataList.clear();
        for (City city : cityList) {
            dataList.add(city.getCityName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_CITY;

    }

    private List<City> getCityList() {
        List<City> rst = new ArrayList<>();
        String provinceName = selectedProvince.getProvinceName();

        for (String s : cities) {
            if (s.startsWith(provinceName)) {
                City city = new City();
                String name = s.split(SPLITTER)[1];

                city.setCityName(name);

                rst.add(city);
            }
        }
        return rst;
    }

    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = getCountyList();

        dataList.clear();
        for (County county : countyList) {
            dataList.add(county.getCountyName());
        }

        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_COUNTY;

//        Log.i("name", selectedCity.getCityName());

    }

    /**
     *  到此可以打印出日志，且可以正确返回所有county
     * @return
     */
    private List<County> getCountyList() {

        sb = new StringBuilder();

        String cityName = selectedCity.getCityName();
        List<County> rst = new ArrayList<>();
        for (String s : counties) {
            String[] sub = s.split(SPLITTER);

            if (sub[0].equals(cityName)) {
                County county = new County();
                String countyName = sub[1];
                county.setCountyName(countyName);
                rst.add(county);
            }

        }

        return rst;
    }

}
