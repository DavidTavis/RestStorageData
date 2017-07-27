package com.retrofit.rest.database.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.retrofit.rest.data.model.Driver;
import com.retrofit.rest.database.SQLiteHelper;

import java.util.ArrayList;

public class DriversTable {

    public static final Uri URI = SQLiteHelper.BASE_CONTENT_URI.buildUpon().appendPath(Requests.TABLE_NAME).build();

    public static void save(Context context, @NonNull Driver driver) {
        context.getContentResolver().insert(URI, toContentValues(driver));
    }

    public static void save(Context context, @NonNull ArrayList<Driver> drivers) {
        ContentValues[] values = new ContentValues[drivers.size()];
        for (int i = 0; i < drivers.size(); i++) {
            values[i] = toContentValues(drivers.get(i));
        }
        context.getContentResolver().bulkInsert(URI, values);
    }

    @NonNull
    public static ContentValues toContentValues(@NonNull Driver driver) {
        ContentValues values = new ContentValues();
        values.put(Columns.ID, driver.getId());
        values.put(Columns.FIRSTNAME, driver.getFirstName());
        values.put(Columns.LASTNAME, driver.getLastName());
        return values;
    }

    @NonNull
    public static Driver fromCursor(@NonNull Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Columns.ID));
        String firstName = cursor.getString(cursor.getColumnIndex(Columns.FIRSTNAME));
        String lastName = cursor.getString(cursor.getColumnIndex(Columns.LASTNAME));
        return new Driver(id, firstName, lastName);
    }

    @NonNull
    public static ArrayList<Driver> listFromCursor(@NonNull Cursor cursor) {
        ArrayList<Driver> drivers = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return drivers;
        }
        try {
            do {
                drivers.add(fromCursor(cursor));
            } while (cursor.moveToNext());
            return drivers;
        } finally {
            cursor.close();
        }
    }

    public static void clear(Context context) {
        context.getContentResolver().delete(URI, null, null);
    }

    public static void deleteById(Context context, long id){
        context.getContentResolver().delete(URI,"id = ? ",new String[]{String.valueOf(id)});
    }

    public static void updateDriver(Context context, @NonNull Driver driver){
        context.getContentResolver().update(URI,toContentValues(driver),"id = ? ",new String[]{String.valueOf(driver.getId())});
    }

    public interface Columns {
        String ID = "id";
        String FIRSTNAME = "first_name";
        String LASTNAME = "last_name";
    }

    public interface Requests {

        String TABLE_NAME = DriversTable.class.getSimpleName();

        String CREATION_REQUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.FIRSTNAME + " VARCHAR(10) NOT NULL, " +
                Columns.LASTNAME + " VARCHAR(200) NOT NULL" + ");";

        String DROP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
