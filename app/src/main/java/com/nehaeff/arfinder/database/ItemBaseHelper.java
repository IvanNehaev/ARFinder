package com.nehaeff.arfinder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nehaeff.arfinder.database.ItemDBSchema.ItemTable;

public class ItemBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "ARFinderBase.db";

    public ItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ItemTable.Cols.UUID + ", " +
                ItemTable.Cols.TITLE + ", " +
                ItemTable.Cols.DATE + ", " +
                ItemTable.Cols.DETAILT + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void createTable(SQLiteDatabase db) {
        try {
/*            db.execSQL("create table " + ItemTable.NAME + "(" +
                    "_id integer primary key autoincrement, " +
                    ItemTable.Cols.UUID + ", " +
                    ItemTable.Cols.TITLE + ", " +
                    ItemTable.Cols.DATE + ", " +
                    ItemTable.Cols.DETAILT + ")");*/
        } finally {

        }

    }

    public static void createTable(SQLiteDatabase db, String name) {
        try {
            db.execSQL("create table if not exists " + name + "(" +
                    "_id integer primary key autoincrement, " +
                    ItemTable.Cols.UUID + ", " +
                    ItemTable.Cols.TITLE + ", " +
                    ItemTable.Cols.DATE + ", " +
                    ItemTable.Cols.DETAILT + ")");
        } finally {

        }

    }
}
