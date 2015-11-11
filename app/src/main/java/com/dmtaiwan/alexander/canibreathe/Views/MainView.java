package com.dmtaiwan.alexander.canibreathe.Views;

import com.dmtaiwan.alexander.canibreathe.Models.AQStation;

import java.util.List;

/**
 * Created by Alexander on 11/10/2015.
 */
public interface MainView {
    void onDataReturned(List<AQStation> aqStations);
}
