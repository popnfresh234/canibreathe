package com.dmtaiwan.alexander.canibreathe.Models;

import android.content.Context;
import android.util.Log;

import com.dmtaiwan.alexander.canibreathe.Utilities.Utilities;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by lenovo on 11/15/2015.
 */
public class DetailInteractorImpl implements DetailInteractor {
    private Context mContext;
    private DetailListener mListener;

    public DetailInteractorImpl(Context context, DetailListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public void requestParseData(final String currentStation) {
        Log.i("requestParse", currentStation);
        //Array of JSON data (Json Array from Parse
        final ArrayList<JSONArray> jsonArrays = new ArrayList<>();

        //write null values to array
        for (int i = 0; i < 24; i++) {
            jsonArrays.add(null);
        }
        //Query parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Data");
        //Only one object stored in parse, so query for fisrt object
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                //if no error
                if (e == null) {
                    //get data string for each hour from parseObject
                    for (int i = 0; i < 24; i++) {
                        //info stored in parseObject as "data0 through data23, fetch and add to array
                        String data = object.getString("data" + String.valueOf(i));
                        //Null data for an hour is written as 0 by cloud code
                        if (!data.equals("0")) {
                            try {
                                //This array represents one hour of data for all AQstations
                                JSONArray stationData = new JSONArray(data);
                                //write to jsonArrays
                                jsonArrays.set(i, stationData);
                            } catch (JSONException e1) {
                                //TODO handle error
                            }
                        } else {
                            //Set null data if no data from parseObject
                            jsonArrays.set(i, null);
                        }
                    }
                    //Create ArrayList of float for chartData
                    ArrayList<Float> floatArray = new ArrayList<>();
                    for (int k = 0; k < 24; k++) {
                        //set array to all zeroes
                        floatArray.add(Float.valueOf("0"));
                    }
                    for (int j = 0; j < jsonArrays.size(); j++) {
                        //For every array of AQstationData
                        JSONArray currentArray = jsonArrays.get(j);
                        //If the array is not null
                        if (currentArray != null) {
                            try {
                                for (int i = 0; i < currentArray.length(); i++) {
                                    //For every station object in the arry of stations
                                    JSONObject station = currentArray.getJSONObject(i);
                                    //If the station matches the station passed in
                                    if (station.getString("SiteName").equals(currentStation)) {
                                        //Get the PM25 string
                                        String pm25String = station.getString("PM2.5");
                                        //If no data, set to 0
                                        if (pm25String.equals("")) {
                                            pm25String = "0";
                                        }
                                        //Convert to AQI
                                        String aqi = Utilities.aqiCalc(pm25String);
                                        float aqiFloat = Float.valueOf(aqi);
                                        floatArray.set(j, aqiFloat);
                                    }
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            floatArray.set(j, Float.valueOf("0"));
                        }
                    }
                    //Create chart data
                    ColumnChartData chartData = Utilities.ReturnChartData(floatArray, mContext.getResources());
                    mListener.onDataReturned(chartData);
                }
            }
        });
    }

    public interface DetailListener {
        void onDataReturned(ColumnChartData chartData);

        void showLoading();
    }
}
