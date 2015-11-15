package com.dmtaiwan.alexander.canibreathe.Presenters;

import android.content.Context;

import com.dmtaiwan.alexander.canibreathe.Models.DetailInteractorImpl;
import com.dmtaiwan.alexander.canibreathe.Views.DetailView;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by lenovo on 11/15/2015.
 */
public class DetailPresenterImpl implements DetailPresenter, DetailInteractorImpl.DetailListener {

    private Context mContext;
    private DetailView mView;
    private DetailInteractorImpl mInteractor;

    public DetailPresenterImpl(DetailView detailView, Context context) {
        this.mContext = context;
        this.mView = detailView;
        this.mInteractor = new DetailInteractorImpl(context, this);

    }

    @Override
    public void requestParseData(String currentStation) {
        mInteractor.requestParseData(currentStation);
    }

    @Override
    public void onDataReturned(ColumnChartData chartData) {
        mView.onDataReturned(chartData);
    }

    @Override
    public void showLoading() {

    }
}
