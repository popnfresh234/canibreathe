package com.dmtaiwan.alexander.canibreathe.Presenters;

import android.content.Context;
import android.util.Log;

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
    private List<AQStation> mAQStationList;

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
    public void requestCountyChange() {
        mInteractor.requestCountyChange();
    }

    @Override
    public void onResult(String result) {
        List<AQStation> AQStationList = parseResult(result);
        if (AQStationList.size() > 0) {
            mView.onDataReturned(AQStationList);
        }
    }

    @Override
    public void onError(String errorMessage) {
        mView.onErrorReturned(errorMessage);
    }

    @Override
    public void onNetworkSucess() {
        mView.onNetworkDataSuccess();
    }


    private List<AQStation> parseResult(String result) {
        List<AQStation> aqStations = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                AQStation aqStation = new AQStation();
                aqStation.setSiteNumber(i);
                aqStation.setSiteName(station.getString("SiteName"));

                Log.i("Station Number Test", station.getString("SiteName")+ " "+ "station" + String.valueOf(i));

                aqStation.setCounty(station.getString("County"));
                aqStation.setPM25(station.getString("PM2.5"));
                aqStation.setPublishTime(station.getString("PublishTime"));
                aqStation.setWindSpeed(station.getString("WindSpeed"));
                aqStation.setWindDirec(station.getString("WindDirec"));
                aqStations.add(aqStation);
            }
        } catch (JSONException e) {
            mView.onErrorReturned(mContext.getString(R.string.error_json));
        }
        return aqStations;
    }


    @Override
    public void showProgress() {
        mView.showProgressBar();
    }

    @Override
    public void hideProgress() {
        mView.hideProgressBar();
    }

}
