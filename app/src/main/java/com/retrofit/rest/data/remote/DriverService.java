package com.retrofit.rest.data.remote;

import com.retrofit.rest.data.model.Driver;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by TechnoA on 24.07.2017.
 */


public interface DriverService {

    String BASE_URL = "http://10.0.3.2:8080";

    @Headers({"Accept: application/json"})
    @GET("/drivers")
    Call<ArrayList<Driver>> getDrivers();

    @GET("/drivers/{id}")
    Call<Driver> getDriver(@Path("id") String id);

    @Headers({"Accept: application/json"})
    @POST("/drivers")
    Call<Driver> createDriver(@Body Driver driver);

    @DELETE("/drivers/{id}")
    Call<Void> deleteDriver(@Path("id") long id);

    @Headers({"Accept: application/json"})
    @PUT("/drivers/{id}")
    Call<Driver> updateDriver(@Body Driver driver, @Path("id") long id);
}

