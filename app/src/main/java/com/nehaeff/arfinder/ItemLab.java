package com.nehaeff.arfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.google.ar.core.Pose;
import com.nehaeff.arfinder.database.ItemBaseHelper;
import com.nehaeff.arfinder.database.ItemCursorWrapper;
import com.nehaeff.arfinder.database.ItemDBSchema.ItemTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLab {
    private static ItemLab sItemLab;
    private Context mContext;
    private static SQLiteDatabase mDatabase;
    private static String tableName;
    private Pose mPoseBase;
    private Pose mPoseItem;
    private UUID mSelectedItemId;

    public static ItemLab get(Context context) {
        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }
        return sItemLab;
    }

    public static ItemLab get() {
        return sItemLab;
    }

    private ItemLab(Context context) {
        mContext = context.getApplicationContext();
        //mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
        mDatabase = RoomLab.get(context).getDatabase();
        ItemBaseHelper.createTable(mDatabase);
    }

    public void addItem(Item item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(tableName, null, values);
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        ItemCursorWrapper cursor = queryItems(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return items;
    }

    public Item getItem(UUID id) {

        ItemCursorWrapper cursor = queryItems(ItemTable.Cols.UUID + " = ?",
                new String[] { id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItem();

        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Item item) {
        File externalFileDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFileDir == null) {
            return null;
        }

        return new File(externalFileDir, item.getPhotoFilename());
    }


    public void updateItem(Item item) {
        String uuidString = item.getId().toString();
        ContentValues values = getContentValues(item);

        mDatabase.update(tableName, values,
                ItemTable.Cols.UUID + " = ?",
                new String[]{ uuidString });
    }

    public boolean deleteItem(UUID id) {

        return false;
    }

    public boolean deleteItem(Item item) {

        mDatabase.delete(tableName, ItemTable.Cols.UUID + " = ?",
                new String[] {item.getId().toString()});

        return true;
    }

    public void deleteItemFromDB(Item item) {

        mDatabase.delete(tableName, ItemTable.Cols.UUID + " = ?",
                new String[] {item.getId().toString()});
    }

    private static ContentValues getContentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, item.getId().toString());
        values.put(ItemTable.Cols.TITLE, item.getTitle());
        values.put(ItemTable.Cols.DATE, item.getDate().getTime());
        values.put(ItemTable.Cols.DETAILT, item.getDescription());
        values.put(ItemTable.Cols.AR, item.getArFlag());

        Pose pose = item.getPose();
        values.put(ItemTable.Cols.TX, pose.tx());
        values.put(ItemTable.Cols.TY, pose.ty());
        values.put(ItemTable.Cols.TZ, pose.tz());

        values.put(ItemTable.Cols.QW, pose.qw());
        values.put(ItemTable.Cols.QX, pose.qx());
        values.put(ItemTable.Cols.QY, pose.qy());
        values.put(ItemTable.Cols.QZ, pose.qz());


        return values;
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                tableName,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ItemCursorWrapper(cursor);
    }

    public static void setTableName(String name) {
        tableName = ItemTable.NAME + "_" + name.replace("-", "_");

        ItemBaseHelper.createTable(mDatabase, tableName);
    }

    public void setPoseBase(Pose pose) {
        mPoseBase = pose;
    }

    public Pose getPoseBase() {
        return mPoseBase;
    }

    public Pose getPoseItem() {
        return mPoseItem;
    }

    public void setPoseItem(Pose pose) {
        mPoseItem = pose;
    }

    public void setSelectedItemId(UUID id) {
        mSelectedItemId = id;
    }

    public UUID getSelectedItemId() {
        return mSelectedItemId;
    }
}
