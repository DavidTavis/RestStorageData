package com.retrofit.rest.loader;

import android.content.Context;
import android.database.Cursor;

import com.retrofit.rest.data.model.Driver;
import com.retrofit.rest.data.remote.ApiFactory;
import com.retrofit.rest.data.remote.DriverService;
import com.retrofit.rest.database.table.DriversTable;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class DriversLoader extends BaseLoader {

    Context context;
    public DriversLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected Cursor apiCall() throws IOException {
        DriverService service = ApiFactory.getDriverService();
        Call<ArrayList<Driver>> call = service.getDrivers();
        ArrayList<Driver> drivers = call.execute().body();
        DriversTable.clear(context);
        DriversTable.save(getContext(), drivers);
        return getContext().getContentResolver().query(DriversTable.URI,
                null, null, null, null);
    }
}


