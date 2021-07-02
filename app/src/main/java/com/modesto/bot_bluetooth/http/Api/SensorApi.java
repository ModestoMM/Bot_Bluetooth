package com.modesto.bot_bluetooth.http.Api;


import retrofit2.Call;
import retrofit2.http.GET;

public interface SensorApi {
    @GET("sensor?from=0&limit=10000")
    public Call<Sensor> getdata();
}
