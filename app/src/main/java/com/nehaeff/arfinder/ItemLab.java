package com.nehaeff.arfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nehaeff.arfinder.database.ItemBaseHelper;
import com.nehaeff.arfinder.database.ItemCursorWrapper;
import com.nehaeff.arfinder.database.ItemDBSchema;
import com.nehaeff.arfinder.database.ItemDBSchema.ItemTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLab {
    private static ItemLab sItemLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ItemLab get(Context context) {
        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }
        return sItemLab;
    }

    private ItemLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addItem(Item item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
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

    public void updateItem(Item item) {
        String uuidString = item.getId().toString();
        ContentValues values = getContentValues(item);

        mDatabase.update(ItemTable.NAME, values,
                ItemTable.Cols.UUID + " = ?",
                new String[]{ uuidString });
    }

    public boolean deleteItem(UUID id) {

        return false;
    }

    public boolean deleteItem(Item item) {

        mDatabase.delete(ItemTable.NAME, ItemTable.Cols.UUID + " = ?",
                new String[] {item.getId().toString()});

        return true;
    }

    public void deleteItemFromDB(Item item) {

        mDatabase.delete(ItemTable.NAME, ItemTable.Cols.UUID + " = ?",
                new String[] {item.getId().toString()});
    }

    private static ContentValues getContentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, item.getId().toString());
        values.put(ItemTable.Cols.TITLE, item.getTitle());
        values.put(ItemTable.Cols.DATE, item.getDate().getTime());
        values.put(ItemTable.Cols.DETAILT, item.getDescription());

        return values;
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ItemTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ItemCursorWrapper(cursor);
    }
}
