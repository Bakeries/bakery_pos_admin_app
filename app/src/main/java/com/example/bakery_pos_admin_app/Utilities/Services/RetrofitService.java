package com.example.bakery_pos_admin_app.Utilities.Services;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private final Retrofit retrofit;

    public RetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.101:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() { return retrofit; }
}
