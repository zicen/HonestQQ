package com.example.lizhenquan.honestqq.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lizhenquan on 2016/12/22.
 */

public class Weather {
    public  String status;
    public Basic basic;
    public  AQI aqi;
    public Now now;
    public  Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
