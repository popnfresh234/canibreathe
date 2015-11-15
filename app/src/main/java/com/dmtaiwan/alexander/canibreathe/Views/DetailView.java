package com.dmtaiwan.alexander.canibreathe.Views;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by lenovo on 11/15/2015.
 */
public interface DetailView {

    void showLoading();

    void onDataReturned(ColumnChartData chartData);
}
