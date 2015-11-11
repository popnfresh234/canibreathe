package com.dmtaiwan.alexander.canibreathe.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alexander on 11/10/2015.
 */
public class Utilities {
    public static final String API_URL = "http://opendata.epa.gov.tw/ws/Data/AQX/?$orderby=County&$skip=0&$top=1000&format=json";

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
