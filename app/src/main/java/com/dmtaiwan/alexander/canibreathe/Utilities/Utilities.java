package com.dmtaiwan.alexander.canibreathe.Utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;

import com.dmtaiwan.alexander.canibreathe.R;

import java.text.DecimalFormat;

/**
 * Created by Alexander on 11/10/2015.
 */
public class Utilities {
    public static final String API_URL = "http://opendata.epa.gov.tw/ws/Data/AQX/?$orderby=County&$skip=0&$top=1000&format=json";
    private static final DecimalFormat mDecimalFormat = new DecimalFormat("0.#");

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static String parseTime(String time) {
        String parsedTime = "";

        for(int i = (time.length()-5); i <time.length(); i++) {
            parsedTime = parsedTime + time.charAt(i);
        }
        return parsedTime;
    }

    public static String formatWindSpeed(String windSpeed) {
        if (windSpeed.equals("")) {
            return "0 km/h";
        }else {
            float metersPerSec = Float.valueOf(windSpeed);
            metersPerSec = metersPerSec * (18 / 5);
            String formattedWindSpeed = mDecimalFormat.format(metersPerSec);
            return formattedWindSpeed + " km/h";
        }
    }

    public static String aqiCalc(String pm25String) {
        Double pm25;
        if(pm25String.equals("")) {
            return "?";
        }
        else {
            pm25 = Double.valueOf(pm25String);
        }
        Double c = Math.floor((10 * pm25) / 10);
        Double AQI = null;
        if (c >= 0 && c < 12.1) {
            AQI = linear(50.0, 0.0, 12.0, 0.0, c);
        } else if (c >= 12.1 && c < 35.5) {
            AQI = linear(100.0, 51.0, 35.4, 12.1, c);
        } else if (c >= 35.5 && c < 55.5) {
            AQI = linear(150.0, 101.0, 55.4, 35.5, c);
        } else if (c >= 55.5 && c < 150.5) {
            AQI = linear(200.0, 151.0, 150.4, 55.5, c);
        } else if (c >= 150.5 && c < 250.5) {
            AQI = linear(300.0, 201.0, 250.4, 150.5, c);
        } else if (c >= 250.5 && c < 350.5) {
            AQI = linear(400.0, 301.0, 350.4, 250.5, c);
        } else if (c >= 350.5 && c < 500.5) {
            AQI = linear(500.0, 401.0, 500.4, 350.5, c);
        } else {
            AQI = -1.0;
        }
        return mDecimalFormat.format(AQI);
    }

    public static Drawable getAqiBackground(String aqi, Context context) {
        if (aqi.equals("?")) {
            return ContextCompat.getDrawable(context, R.drawable.circle_bg_green);
        }

        double aqiDouble = Double.valueOf(aqi);

        if (aqiDouble <= 50) {
            return ContextCompat.getDrawable(context, R.drawable.circle_bg_green);
        } else if (aqiDouble > 51 && aqiDouble <= 100) {
            return ContextCompat.getDrawable(context, R.drawable.circle_bg_yellow);
        } else if (aqiDouble > 101 && aqiDouble <= 150) {
            return ContextCompat.getDrawable(context, R.drawable.circle_bg_orange);
        } else if (aqiDouble > 151 && aqiDouble <= 200) {
            return ContextCompat.getDrawable(context, R.drawable.circle_bg_red);
        } else return ContextCompat.getDrawable(context, R.drawable.circle_bg_purple);
    }

    private static Double linear(Double AQIHigh, Double AQILow, Double concHigh, Double concLow, Double Concentration) {
        Double linear;
        Double a = ((Concentration - concLow) / (concHigh - concLow)) * (AQIHigh - AQILow) + AQILow;
        linear = Double.valueOf(Math.round(a));
        return linear;
    }
}
