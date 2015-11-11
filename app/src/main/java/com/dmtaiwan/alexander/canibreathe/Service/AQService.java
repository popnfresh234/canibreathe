package com.dmtaiwan.alexander.canibreathe.Service;

import com.dmtaiwan.alexander.canibreathe.Models.HttpResponse;
import com.dmtaiwan.alexander.canibreathe.Utilities.Utilities;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Alexander on 11/10/2015.
 */
public class AQService {
    public Observable<HttpResponse> requestAQStations() {
        Observable<HttpResponse> AQStationObservcable = Observable.create(new Observable.OnSubscribe<HttpResponse>() {
            @Override
            public void call(Subscriber<? super HttpResponse> subscriber) {
                String apiUrl = Utilities.API_URL;
                HttpResponse httpResponse = new HttpResponse();
                int resultCode;
                try {
                    URL url = new URL(apiUrl);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    resultCode = response.code();
                    httpResponse.setResponseCode(resultCode);
                    if (resultCode == HttpURLConnection.HTTP_OK) {
                        httpResponse.setResponse(response.body().string());
                        subscriber.onNext(httpResponse);
                    } else {
                        subscriber.onNext(httpResponse);
                    }
                } catch (Exception e) {
                    httpResponse.setResponseCode(0);
                    httpResponse.setResponse(e.toString());
                    subscriber.onNext(httpResponse);
                }
            }
        });
        return AQStationObservcable;
    }
}
