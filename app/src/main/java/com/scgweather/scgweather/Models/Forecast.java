package com.scgweather.scgweather.Models;


public class Forecast
{

    private String day;
    private int temp;
    private String currentConditionIV;

    public Forecast(String day, int temp, String currentConditionIV)
    {
        this.currentConditionIV = currentConditionIV;
        this.temp=temp;
        this.day=day;
    }

    public String getDay()
    {
        return day;
    }

    public int getTemp()
    {
        return temp;
    }

    public String getCurrentConditionIV()
    {
        return currentConditionIV;
    }




































}
