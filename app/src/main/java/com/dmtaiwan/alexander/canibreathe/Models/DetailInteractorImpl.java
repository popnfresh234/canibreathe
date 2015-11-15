package com.dmtaiwan.alexander.canibreathe.Models;

import android.content.Context;

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
    public void requestParseData() {

    }

    public interface DetailListener {
        void onDataReturned();

        void showLoading();
    }
}
