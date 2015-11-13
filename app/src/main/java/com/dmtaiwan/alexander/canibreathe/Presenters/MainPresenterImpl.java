package com.dmtaiwan.alexander.canibreathe.Presenters;

import android.content.Context;
import android.preference.PreferenceManager;

import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.Models.MainInteractor;
import com.dmtaiwan.alexander.canibreathe.Models.MainInteractorImpl;
import com.dmtaiwan.alexander.canibreathe.R;
import com.dmtaiwan.alexander.canibreathe.Views.MainView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 11/10/2015.
 */
public class MainPresenterImpl implements MainPresenter, MainInteractorImpl.MainInteractorListener {

    private static final String LOG_TAG = MainPresenterImpl.class.getSimpleName();

    private MainView mView;
    private MainInteractor mInteractor;
    private Context mContext;

    public MainPresenterImpl(MainView mainView, Context context) {
        this.mView = mainView;
        this.mContext = context;
        this.mInteractor = new MainInteractorImpl(this, mContext);
    }

    @Override
    public void requestAQData() {
        mInteractor.fetchAQData();
    }

    @Override
    public void onResult(String result) {
        List<AQStation> AQStationList =  parseResult(result);
        List<AQStation> sortedAQStationList = sortStations(AQStationList);
        mView.onDataReturned(sortedAQStationList);
    }



    private List<AQStation> parseResult(String result) {
        List<AQStation> aqStations = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                AQStation aqStation = new AQStation();
                aqStation.setSiteName(station.getString("SiteName"));
                aqStation.setCounty(station.getString("County"));
                aqStation.setPM25(station.getString("PM2.5"));
                aqStation.setPublishTime(station.getString("PublishTime"));
                aqStation.setWindSpeed(station.getString("WindSpeed"));
                aqStations.add(aqStation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aqStations;
    }

    private List<AQStation> sortStations(List<AQStation> aqStationList) {
        List<AQStation> sortedStations = new ArrayList<>();
        String county = PreferenceManager.getDefaultSharedPreferences(mContext).getString(mContext.getString(R.string.pref_key_county), mContext.getString(R.string.pref_county_taipei_city));

        for (AQStation aqStation : aqStationList) {
            if (aqStation.getCounty().equals(county)) {
                sortedStations.add(aqStation);
            }
        }
        return sortedStations;
    }
}
