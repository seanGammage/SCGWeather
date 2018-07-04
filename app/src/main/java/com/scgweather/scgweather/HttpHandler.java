package com.scgweather.scgweather;
import android.text.TextUtils;
import android.util.Log;

import com.scgweather.scgweather.Models.Forecast;
import com.scgweather.scgweather.Models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

//request and receive weather data
public class HttpHandler
{
    private static final String TAG = HttpHandler.class.getSimpleName();

    private HttpHandler()
    {
    }

    public static Weather fetchData(String requestUrl)
    {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e)
        {
            Log.e(TAG, "Could not make the HTTP request", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    //create the url
    private static URL createUrl(String stringUrl)
    {
        URL url;
        try
        {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception)
        {
            Log.e(TAG, "Could not create the URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        if (url == null)
        {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(80000);
            urlConnection.setConnectTimeout(80000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else if (urlConnection.getResponseCode() == 400)
            {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());

            } else
                {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e)
        {
            Log.e(TAG, "Could not retreive the JSON results", e);

        } finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static Weather extractFeatureFromJson(String weatherJSON)
    {
        if (TextUtils.isEmpty(weatherJSON))
        {
            return null;
        }


        try
        {
            JSONObject locationJsonResponse = new JSONObject(weatherJSON);

            JSONObject location = locationJsonResponse.getJSONObject("location");
            String cityName = location.getString("name");
            String country = location.getString("country");

            JSONObject currentJsonResponse = new JSONObject(weatherJSON);
            JSONObject current = currentJsonResponse.getJSONObject("current");
            int tempF = (int) Math.round(current.getDouble("temp_f"));
            int currentHumidity = current.getInt("humidity");
            double currentWindMph = current.getDouble("wind_mph");
            double currentPressureMb = current.getDouble("pressure_mb");
            double currentVisibilityM = current.getDouble("vis_miles");
            String currentWindDirection = current.getString("wind_dir");


            JSONObject condition = current.getJSONObject("condition");
            String conditionIcon = condition.getString("icon");
            String conditionText = condition.getString("text");

            JSONObject forecastJsonResponse = new JSONObject(weatherJSON);
            JSONObject forecast = forecastJsonResponse.getJSONObject("forecast");
            JSONArray forecastDay = forecast.getJSONArray("forecastday");

            HashMap<Integer, Forecast> forecastHashMap = new HashMap<>();

            try
            {
                for (int i = 1; i < forecastDay.length(); i++)
                {

                    JSONObject index = forecastDay.getJSONObject(i);

                    //convert to day time
                    long timeInMilliseconds = index.getLong("date_epoch");
                    String forecastDayName = formatDateTime(timeInMilliseconds);

                    JSONObject day = index.getJSONObject("day");
                    int forecastTemp = (int) Math.round(day.getDouble("maxtemp_f"));

                    JSONObject forecastCondition = day.getJSONObject("condition");
                    String forecastConditionIcon = forecastCondition.getString("icon");

                    Forecast forecastM = new Forecast(forecastDayName, forecastTemp, forecastConditionIcon);

                    forecastHashMap.put(i - 1, forecastM);

                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            return new Weather(cityName, country, tempF, conditionIcon, conditionText, currentHumidity, currentWindMph, currentWindDirection, currentPressureMb, currentVisibilityM, forecastHashMap);

        } catch (JSONException e)
        {
            Log.e(TAG, "Cannot parse the JSON results", e);
        }
        return null;
    }

    private static String formatDateTime(long timeInMilliseconds)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(("E"), Locale.ENGLISH);
        return formatter.format(new Date(timeInMilliseconds * 1000));
    }




















        }
