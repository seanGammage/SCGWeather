package com.scgweather.scgweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//to make sure the user is connected to the internet
public class HasConnection
{

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}