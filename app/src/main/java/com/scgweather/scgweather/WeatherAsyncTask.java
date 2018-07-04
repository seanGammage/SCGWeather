package com.scgweather.scgweather;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.scgweather.scgweather.Models.Weather;

//this loads on a seperate thread the weather and forecast data.
// the uses the fetchData method from the httphelper class to return the weather
public class WeatherAsyncTask extends AsyncTaskLoader<Weather>
{
    private static final String TAG = WeatherAsyncTask.class.getName();

    private String mUrl;

    public WeatherAsyncTask(Context context, String mUrl)
    {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public Weather loadInBackground()
    {
        if (mUrl == null)
        {
            return null;
        }

        // Perform the network request, parse the response, and extract current weather and forecast.
        return HttpHandler.fetchData(mUrl);
    }



























}
