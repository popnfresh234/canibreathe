package com.dmtaiwan.alexander.canibreathe.Models;

import android.content.Context;

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
                    String response = processData(httpResponse);
                    mListener.onResult(response);
                }
            };

            httpResponseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            //No network
            mListener.onResult(mContext.getString(R.string.error_no_network));
        }
    }


    private String processData(HttpResponse httpResponse) {
        int responseCode = httpResponse.getResponseCode();

        //If http response was good, get the json string
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Utilities.writeToFile(httpResponse.getResponse(), mContext);
            return httpResponse.getResponse();
        } else if (Utilities.doesFileExist(mContext)) {
            return Utilities.readFromFile(mContext);
        } else {
            return httpResponse.getResponse();
        }
    }

    public interface MainInteractorListener {
        void onResult(String result);

        void showProgress();

        void hideProgress();
    }
}
