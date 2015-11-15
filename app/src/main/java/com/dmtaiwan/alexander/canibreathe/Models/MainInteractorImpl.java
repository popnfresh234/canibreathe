package com.dmtaiwan.alexander.canibreathe.Models;

import android.content.Context;
import android.util.Log;

import com.dmtaiwan.alexander.canibreathe.R;
import com.dmtaiwan.alexander.canibreathe.Service.AQService;
import com.dmtaiwan.alexander.canibreathe.Utilities.Utilities;

import java.net.HttpURLConnection;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Alexander on 11/10/2015.
 */
public class MainInteractorImpl implements MainInteractor {

    private MainInteractorListener mListener;
    private Context mContext;

    public MainInteractorImpl(MainInteractorListener listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
    }



    @Override
    public void fetchAQData() {

        //If there's stored data, load as default
        if (Utilities.doesFileExist(mContext)) {
            mListener.showProgress();
            String response = Utilities.readFromFile(mContext);
            mListener.onResult(response);
        }

        if (Utilities.isNetworkAvailable(mContext)) {
            mListener.showProgress();
            AQService aqService = new AQService();
            Observable<HttpResponse> httpResponseObservable = aqService.requestAQStations();
            Action1<HttpResponse> subscriber = new Action1<HttpResponse>() {
                @Override
                public void call(HttpResponse httpResponse) {
                    if (httpSuccess(httpResponse)) {
                        Log.i("sucess", "success");
                        Log.i("code", String.valueOf(httpResponse.getResponseCode()));
                        Utilities.writeToFile(httpResponse.getResponse(), mContext);
                        mListener.onResult(httpResponse.getResponse());
                        mListener.onNetworkSucess();
                    }else {
                        Log.i("error", "error");
                        mListener.onError(getErrorMessage(httpResponse));
                    }
                }
            };

            httpResponseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            //No network
            mListener.onError(mContext.getString(R.string.error_no_network));
        }
    }

    @Override
    public void requestCountyChange() {
        if (Utilities.doesFileExist(mContext)) {
            mListener.showProgress();
            String response = Utilities.readFromFile(mContext);
            mListener.onResult(response);
        }

        else if(Utilities.isNetworkAvailable(mContext)) {
            mListener.showProgress();
            AQService aqService = new AQService();
            Observable<HttpResponse> httpResponseObservable = aqService.requestAQStations();
            Action1<HttpResponse> subscriber = new Action1<HttpResponse>() {
                @Override
                public void call(HttpResponse httpResponse) {
                    if (httpSuccess(httpResponse)) {
                        Log.i("sucess", "success");
                        Log.i("code", String.valueOf(httpResponse.getResponseCode()));
                        Utilities.writeToFile(httpResponse.getResponse(), mContext);
                        mListener.onResult(httpResponse.getResponse());
                        mListener.onNetworkSucess();
                    }else {
                        Log.i("error", "error");
                        mListener.onError(getErrorMessage(httpResponse));
                    }
                }
            };

            httpResponseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            //No network
            mListener.onError(mContext.getString(R.string.error_no_network));
        }
    }

    private boolean httpSuccess(HttpResponse httpResponse) {
        if (httpResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return true;
        }else {
            return false;
        }
    }

    private String getErrorMessage(HttpResponse httpResponse) {
        int httpCode = httpResponse.getResponseCode();

        switch (httpCode) {
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                return mContext.getString(R.string.error_http_500);
            case HttpURLConnection.HTTP_UNAVAILABLE:
                return mContext.getString(R.string.error_http_503);
            default:
                return mContext.getString(R.string.error_http);
        }
    }


    public interface MainInteractorListener {
        void onResult(String result);

        void onError(String errorMessage);

        void onNetworkSucess();

        void showProgress();

        void hideProgress();
    }
}
