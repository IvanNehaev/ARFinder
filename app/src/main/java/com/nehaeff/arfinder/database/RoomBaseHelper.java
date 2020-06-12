package com.nehaeff.arfinder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nehaeff.arfinder.database.RoomDBSchema.RoomTable;

public class RoomBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "ARFinderBase.db";

    public RoomBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RoomTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                RoomTable.Cols.UUID + ", " +
                RoomTable.Cols.TITLE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
