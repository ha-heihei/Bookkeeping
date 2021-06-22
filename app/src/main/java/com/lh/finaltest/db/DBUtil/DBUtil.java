package com.lh.finaltest.db.DBUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBUtil extends SQLiteOpenHelper {

    private final String USER_TABLE="create table user(" +
            "id text primary key," +
            "name text," +
            "pwd text)";

    private String CHARGE_TABLE="create table charge(" +
            "id text primary key," +
            "name text," +
            "type text," +
            "money text,"+
            "remark text," +
            "date text," +
            "userid text)";

    public DBUtil(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(CHARGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
