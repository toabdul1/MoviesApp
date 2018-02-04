package com.sky.movieapp.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by delaroy on 5/18/17.
 */
public class Client {

 Context context;
public Client(Context context) {
    this.context = context;
}
    public  final String BASE_URL = "https://movies-sample.herokuapp.com/";
    public  Retrofit retrofit = null;
    int cacheSize = 10 * 1024 * 1024; // 10 MiB

    public  Retrofit getClient(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public  Retrofit getRetrofit(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOKHTTPClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    private OkHttpClient getOKHTTPClient(){
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain)
                                throws IOException {
                            Request request = chain.request();
                            if (!isNetworkAvailable()) {
                                int maxStale = 60 * 10; // tolerate 10 minutes stale \
                                request = request
                                        .newBuilder()
                                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                        .build();
                            }
                            return chain.proceed(request);
                        }
                    })
                    .build();
        return okHttpClient;
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
