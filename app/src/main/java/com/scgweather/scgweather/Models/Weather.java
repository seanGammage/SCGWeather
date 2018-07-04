package com.scgweather.scgweather.Models;



import com.scgweather.scgweather.Models.Forecast;

import java.util.HashMap;


public class Weather
{

    private String currentCity;
    private String currentCountry;
    private int currentTemp;
    private HashMap<Integer, Forecast> forecastHashmap;
    private String currentConditionTV;
    private String currentConditionIV;
    private int currentHumidity;
    private double currentWindMph;
    private String currentWindDirection;
    private double currentPressureMb;
    private double currentVisibilityM;

    public Weather(String currentCity, String currentCountry, int currentTemp, String currentConditionIV, String currentConditionTV, int currentHumidity, double currentWindMph, String currentWindDirection, double currentPressureMb, double currentVisibilityM, HashMap<Integer, Forecast> forecastHashmap)
    {
        this.currentCity = currentCity;
        this.currentCountry = currentCountry;
        this.currentTemp = currentTemp;
        this.forecastHashmap = forecastHashmap;
        this.currentConditionIV = currentConditionIV;
        this.currentConditionTV = currentConditionTV;
        this.currentHumidity = currentHumidity;
        this. currentWindMph = currentWindMph;
        this.currentPressureMb = currentPressureMb;
        this.currentVisibilityM = currentVisibilityM;
        this.currentWindDirection = currentWindDirection;
    }


    public String getCurrentCity()
    {
        return currentCity;
    }

    public String getCurrentCountry()
    {
        return currentCountry;
    }

    public int getCurrentTemp()
    {
        return currentTemp;
    }

    public String getCurrentWindDirection() {
        return currentWindDirection;
    }


    public HashMap<Integer, Forecast> getForecastHashmap()
    {
        return forecastHashmap;
    }

    public String getCurrentConditionTV()
    {
        return currentConditionTV;
    }

    public String getCurrentConditionIV()
    {
        return currentConditionIV;
    }

    public int getCurrentHumidity() {
        return currentHumidity;
    }

    public double getCurrentWindMph() {
        return currentWindMph;
    }

    public double getCurrentPressureMb() {
        return currentPressureMb;
    }

    public double getCurrentVisibilityM() {
        return currentVisibilityM;
    }






































}

