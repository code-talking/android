package com.business.e.bl.comassigntwo.model;

import java.util.List;

public class Province {
    private int id;
    private String provinceName;
    private int provinceCode;
    private List<City> citiesInProvince;
    private final String SPLITOR = "@@";



    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {

        this.provinceName = provinceName;
    }

    public int getProvinceCode() {

        return provinceCode;
    }

    public String getProvinceName() {

        return provinceName;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getId() {

        return id;
    }
}
