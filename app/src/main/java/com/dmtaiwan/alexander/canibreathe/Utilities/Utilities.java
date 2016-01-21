package com.dmtaiwan.alexander.canibreathe.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * Created by Alexander on 11/10/2015.
 */
public class Utilities {
    public static final String API_URL = "http://opendata.epa.gov.tw/ws/Data/AQX/?$orderby=County&$skip=0&$top=1000&format=json";
    private static final DecimalFormat mDecimalFormat = new DecimalFormat("0.#");
    public static final String FILE_NAME = "epa.json";

    //Intent extras
    public static final String EXTRA_AQ_STATION = "com.dmtaiwan.alexander.canibreathe.aqstation";

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static String formatTime(String time) {
        String parsedTime = "";

        for (int i = (time.length() - 5); i < time.length(); i++) {
            parsedTime = parsedTime + time.charAt(i);
        }
        return parsedTime;
    }

    public static String formatWindSpeed(String windSpeed) {
        if (windSpeed.equals("")) {
            return "0 km/h";
        } else {
            float metersPerSec = Float.valueOf(windSpeed);
            metersPerSec = metersPerSec * (18 / 5);
            String formattedWindSpeed = mDecimalFormat.format(metersPerSec);
            return formattedWindSpeed + " km/h";
        }
    }

    public static String formatWindDirection(String windDirection) {
        if (windDirection.equals("")) {
            return "0" + "\u00B0";
        } else {
            return windDirection + "\u00B0";
        }
    }

    public static float getWindDegreeForRotate(String windDirection) {
        if (windDirection.equals("")) {
            return 0;
        } else {
            return Float.valueOf(windDirection);
        }
    }

    public static String aqiCalc(String pm25String) {
        Double pm25;
        if (pm25String.equals("")) {
            return "?";
        } else {
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
            return ContextCompat.getDrawable(context, R.drawable.circle_bg_gray);
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

    public static int getDetailHeader(String aqi, Context context) {
        if (aqi.equals("?")) {
            return R.drawable.aq_good;
        }

        double aqiDouble = Double.valueOf(aqi);

        if (aqiDouble <= 50) {
            return R.drawable.aq_good;
        } else if (aqiDouble > 51 && aqiDouble <= 100) {
            return R.drawable.aq_moderate;
        } else if (aqiDouble > 101 && aqiDouble <= 150) {
            return R.drawable.aq_unhealthy;
        } else if (aqiDouble > 151 && aqiDouble <= 200) {
            return R.drawable.aq_dangerous;
        } else return R.drawable.aq_unknown;
    }

    public static int getTextColor(String aqi, Context context) {
        if (aqi.equals("?")) {
            return context.getResources().getColor(R.color.textWhite);
        }
        double aqiDouble = Double.valueOf(aqi);
        if (aqiDouble > 51 && aqiDouble <= 100) {
            return context.getResources().getColor(R.color.textDarkBg54);
        } else return context.getResources().getColor(R.color.textWhite);
    }

    private static Double linear(Double AQIHigh, Double AQILow, Double concHigh, Double concLow, Double Concentration) {
        Double linear;
        Double a = ((Concentration - concLow) / (concHigh - concLow)) * (AQIHigh - AQILow) + AQILow;
        linear = Double.valueOf(Math.round(a));
        return linear;
    }

    static public boolean doesFileExist(Context context) {
        File file = context.getFileStreamPath(FILE_NAME);
        return file.exists();
    }

    public static void writeToFile(String json, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(Utilities.FILE_NAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(Context context) {
        String json = "";
        try {
            InputStream inputStream = context.openFileInput(Utilities.FILE_NAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                json = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String getAQDetailTitle(int position, Context context) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.aq_detail_title_pm25);
            case 1:
                return context.getResources().getString(R.string.aq_detail_title_wind_direction);
            case 2:
                return context.getResources().getString(R.string.aq_detail_title_wind_speed);
            case 3:
                return context.getResources().getString(R.string.aq_detail_title_update);
            default:
                return null;
        }
    }

    public static int getAqIcon(int positon, Context context) {
        switch (positon) {
            case 0:
                return R.drawable.icon_pm25;
            case 1:
                return R.drawable.icon_wind_direction;
            case 2:
                return R.drawable.icon_wind_speed;
            case 3:
                return R.drawable.icon_time;
            default:
                return 0;
        }
    }

    public static String getAqData(int position, AQStation aqStation) {
        switch (position) {
            case 0:
                return aqiCalc(aqStation.getPM25());
            case 1:
                return formatWindDirection(aqStation.getWindDirec());
            case 2:
                return formatWindSpeed(aqStation.getWindSpeed());
            case 3:
                return formatTime(aqStation.getPublishTime());
            default:
                return null;
        }
    }

    public static ColumnChartData ReturnChartData(ArrayList<Float> floatArray, Resources resources) {
        final boolean hasAxes = true;
        final boolean hasAxesNames = true;
        final boolean hasLabels = false;
        final boolean hasLabelForSelected = false;
        //Generate ChartData from floatArray
        int numColumns = 24;
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; i++) {
            values = new ArrayList<>();
            values.add(new SubcolumnValue(floatArray.get(i), resources.getColor(R.color.statusPurple)));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        ColumnChartData chartData = new ColumnChartData(columns);
        if (hasAxes) {
            Axis axisX = new Axis();
            axisX.setTextColor(resources.getColor(android.R.color.black));
            List<AxisValue> axisValues = new ArrayList<>();
            for (int i = 0; i <= 24; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValues.add(axisValue);
            }
            axisX.setValues(axisValues);
            axisX.setAutoGenerated(false);
            axisX.setMaxLabelChars(1);
            axisX.setTextSize(6);
            Axis axisY = new Axis().setHasLines(true);
            axisY.setTextColor(resources.getColor(android.R.color.black));
            if (hasAxesNames) {
                axisX.setName("Time");
                axisY.setName("PM25");
            }
            chartData.setAxisXBottom(axisX);
            chartData.setAxisYLeft(axisY);
        } else {
            chartData.setAxisXBottom(null);
            chartData.setAxisYLeft(null);
        }
        return chartData;
    }

    public static String getTabTitle(Context context, int tab) {
        String languagePref = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_key_language), context.getString(R.string.pref_language_eng));
        switch (tab) {
            case 0:
                if (languagePref.equals(context.getString(R.string.pref_language_zh))) {
                    return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_key_county), context.getString(R.string.pref_county_taipei_city));
                } else if (languagePref.equals(context.getString(R.string.pref_language_eng))) {
                    String[] keyArray = context.getResources().getStringArray(R.array.pref_county_values);
                    List<String> keyArrayList = Arrays.asList(keyArray);
                    if (keyArrayList.contains(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_key_county), context.getString(R.string.pref_county_taipei_city)))) {
                        int number = keyArrayList.indexOf(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_key_county), context.getString(R.string.pref_county_taipei_city)));
                        String[] labelsArray = context.getResources().getStringArray(R.array.pref_county_options);
                        List<String> labelArray = Arrays.asList(labelsArray);
                        return labelArray.get(number);
                    } else return null;
                }
            case 1:
                if (languagePref.equals(context.getString(R.string.pref_language_zh))) {
                    return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_key_secondary_county), context.getString(R.string.pref_county_taipei_city));
                } else if (languagePref.equals(context.getString(R.string.pref_language_eng))) {
                    String[] keyArray = context.getResources().getStringArray(R.array.pref_county_values);
                    List<String> keyArrayList = Arrays.asList(keyArray);
                    if (keyArrayList.contains(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_key_secondary_county), context.getString(R.string.pref_county_taipei_city)))) {
                        int number = keyArrayList.indexOf(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_key_secondary_county), context.getString(R.string.pref_county_taipei_city)));
                        String[] labelsArray = context.getResources().getStringArray(R.array.pref_county_options);
                        List<String> labelArray = Arrays.asList(labelsArray);
                        return labelArray.get(number);
                    } else return null;
                }
            default:
                return null;
        }
    }
}
