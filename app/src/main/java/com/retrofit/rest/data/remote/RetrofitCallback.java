package com.retrofit.rest.data.remote;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Artur Vasilov
 */
public abstract class RetrofitCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        // Do nothing
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        // Do nothing
    }
}
