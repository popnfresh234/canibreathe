package com.dmtaiwan.alexander.canibreathe.Presenters;

import android.content.Context;

import com.dmtaiwan.alexander.canibreathe.Models.DetailInteractorImpl;

/**
 * Created by lenovo on 11/15/2015.
 */
public class DetailPresenterImpl implements DetailPresenter, DetailInteractorImpl.DetailListener {

    private Context mContext;
    private DetailInteractorImpl mInteractor;

    public DetailPresenterImpl(Context context) {
        this.mContext = context;
        this.mInteractor = new DetailInteractorImpl(context, this);

    }

    @Override
    public void requestParseData() {
        mInteractor.requestParseData();
    }

    @Override
    public void onDataReturned() {
        
    }

    @Override
    public void showLoading() {

    }
}
