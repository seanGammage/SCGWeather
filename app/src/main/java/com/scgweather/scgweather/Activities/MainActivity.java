package com.scgweather.scgweather.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.support.constraint.Group;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.scgweather.scgweather.HasConnection;
import com.scgweather.scgweather.Models.Weather;
import com.scgweather.scgweather.R;
import com.scgweather.scgweather.WeatherAsyncTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Weather>
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String API_KEY = "7f4d67947e694a3491f145200180506";

    private EditText mSearchInput;
    private LoaderManager mLoaderManager;
    private Group mWeatherGroup;
    private String cityName;
    Button locationButton;
    TextView mCurrentConditionTV;
    ScrollView scrollView;
    TextView mCurrentCityTV;
    private String currentCcondition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherGroup = findViewById(R.id.weatherGroup);
        mWeatherGroup.setVisibility(View.GONE);
        Group mInputGroup = findViewById(R.id.inputGroup);
        mInputGroup.setVisibility(View.GONE);

        locationButton = (Button) findViewById(R.id.locationButton);
        mCurrentConditionTV = (TextView) findViewById(R.id.currentConditionTV);
        scrollView = (ScrollView) findViewById(R.id.main_activity);
        mCurrentCityTV = (TextView) findViewById(R.id.currentCityTV);

        if (API_KEY.isEmpty())
        {

            Toast.makeText(MainActivity.this, "No API Key", Toast.LENGTH_SHORT).show();

        } else if (!HasConnection.isOnline(this))
        {

            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

        } else
            {

            mInputGroup.setVisibility(View.VISIBLE);
            final Button mSearchButton = findViewById(R.id.searchButton);
            mSearchInput = findViewById(R.id.searchInput);
            mLoaderManager = getLoaderManager();

            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                    } else
                        {
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try
                        {
                            String city = hereLocation(location.getLatitude(), location.getLongitude());
                            mSearchInput.setText(city);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }

                }
            });






            mSearchButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {


                    cityName = mSearchInput.getText().toString();
                    if (TextUtils.isEmpty(cityName))
                    {
                        Toast.makeText(MainActivity.this, "Type the city name", Toast.LENGTH_SHORT).show();
                    } else
                        {
                         //change background note: INCOMPLETE

                            currentCcondition = mCurrentConditionTV.getText().toString();


                            if (currentCcondition == "Partly cloudy")
                            {
                                scrollView.setBackgroundResource(R.drawable.overcast);

                            }

                        mLoaderManager.restartLoader(1, null, MainActivity.this);
                        mLoaderManager.initLoader(1, null, MainActivity.this);

                    }

                    dismissKeyboard(MainActivity.this);
                }
            });


            // Deliver results after screen rotation
            if (mLoaderManager.getLoader(1) != null)
            {
                mLoaderManager.initLoader(1, null, this);
            }

        }
    }

    //for location permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults)
    {

        switch (requestCode)
        {
            case  1000:
                {
                if (grandResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        String city = hereLocation(location.getLatitude(), location.getLongitude());
                        mSearchInput.setText(city);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();

                } else
                    {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

//for location
    private String hereLocation(double lat, double lon)
    {
        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try
        {
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if (addresses.size() > 0)
            {
                for (Address adr : addresses)
                {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0)
                    {
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return cityName;
    }

        private void updateUi(Weather weather)
    {

        TextView city = findViewById(R.id.currentCityTV);
        city.setText(weather.getCurrentCity());

        TextView country = findViewById(R.id.currentCountryTV);
        country.setText(weather.getCurrentCountry());

        TextView tempFerenheight = findViewById(R.id.currentTempTV);
        tempFerenheight.setText(getString(R.string.temp_degree, Integer.toString(weather.getCurrentTemp())));

        ImageView imageview = findViewById(R.id.currentConditionIV);
        Picasso.with(MainActivity.this)
        .load("http:" + weather.getCurrentConditionIV())
                .resize(150,150)
                .into(imageview);

       TextView conditionText = findViewById(R.id.currentConditionTV);
        conditionText.setText(weather.getCurrentConditionTV());

        TextView humidity = findViewById(R.id.currentHumidityTV);
        humidity.setText(getString(R.string.humidity, Integer.toString(weather.getCurrentHumidity())));

        TextView windKph = findViewById(R.id.currentWindTV);
        windKph.setText(getString(R.string.wind, weather.getCurrentWindDirection(), Double.toString(weather.getCurrentWindMph())));

        TextView pressureMb = findViewById(R.id.currentPressureTV);
        pressureMb.setText(getString(R.string.pressure, Double.toString(weather.getCurrentPressureMb())));

        TextView visibilityKm = findViewById(R.id.currentVisibilityTV);
        visibilityKm.setText(getString(R.string.visibility, Double.toString(weather.getCurrentVisibilityM())));

        Resources r = getResources();
        String name = getPackageName();

        int[] resId = new int[4];

        for (int i = 0; i < 4; i++)
        {

            resId[i] = r.getIdentifier("day" + (i + 1) + "TV", "id", name);
            TextView forecastDayName = findViewById(resId[i]);

            resId[i] = r.getIdentifier("day" + (i + 1) + "IV", "id", name);

            ImageView forecastConditionIcon = findViewById(resId[i]);
            Picasso.with(MainActivity.this)
                    .load("http:" + weather.getForecastHashmap().get(i).getCurrentConditionIV())
                    .into(forecastConditionIcon);

            resId[i] = r.getIdentifier("day" + (i + 1) + "TempTV", "id", name);
            TextView tempForecast = findViewById(resId[i]);
            tempForecast.setText(getString(R.string.temp_degree, Integer.toString(weather.getForecastHashmap().get(i).getTemp())));

            mWeatherGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<Weather> onCreateLoader(int i, Bundle bundle)
    {
        return new WeatherAsyncTask(this, String.format("http://api.apixu.com/v1/forecast.json?key=%s&days=5&q=%s", API_KEY, cityName));
    }

    @Override
    public void onLoadFinished(Loader<Weather> loader, Weather model)
    {
        if (model == null)
        {

            mWeatherGroup.setVisibility(View.GONE);

        } else
            {

            updateUi(model);
        }
    }

    private void dismissKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            if (imm != null)
            {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getApplicationWindowToken(), 0);
            }
    }

    @Override
    public void onLoaderReset(Loader<Weather> loader)
    {
    }
}