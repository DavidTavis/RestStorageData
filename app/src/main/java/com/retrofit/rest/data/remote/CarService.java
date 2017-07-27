package com.retrofit.rest.data.remote;

import com.retrofit.rest.data.model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by TechnoA on 18.07.2017.
 */

public interface CarService {

    String BASE_URL = "http://10.0.3.2:8080";

    @Headers({"Accept: application/json"})
    @GET("/cars")
    Call<List<Car>> getCars();

    @GET("/cars/{id}")
    Call<Car> getCar(@Path("id") String id);

    @Headers({"Accept: application/json"})
    @POST("/cars")
    Call<Car> createCar(@Body Car car);

}
