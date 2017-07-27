package com.retrofit.rest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.retrofit.rest.database.table.DriversTable;


public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String CONTENT_AUTHORITY = "com.retrofit.rest.loaders";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String DATABASE_NAME = "com.retrofit.rest.db";

    private static final int DATABASE_VERSION = 2;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DriversTable.Requests.CREATION_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DriversTable.Requests.DROP_REQUEST);
        onCreate(db);
    }
}
