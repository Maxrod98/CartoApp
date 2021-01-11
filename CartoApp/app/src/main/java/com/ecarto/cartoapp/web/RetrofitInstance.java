package com.ecarto.cartoapp.web;

import android.content.Context;

import com.ecarto.cartoapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitInstance {
    public Retrofit retrofit;
    private static RetrofitInstance retrofitInstance;

    private RetrofitInstance(Context context){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseUrl))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

    }

    public static RetrofitInstance getInstance(Context context){

        if(retrofitInstance == null){
            retrofitInstance = new RetrofitInstance(context);
        }

        return  retrofitInstance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
