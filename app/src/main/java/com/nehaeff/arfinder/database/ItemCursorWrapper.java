package com.nehaeff.arfinder.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nehaeff.arfinder.Item;
import com.nehaeff.arfinder.database.ItemDBSchema.ItemTable;

import java.util.Date;
import java.util.UUID;

public class ItemCursorWrapper extends CursorWrapper {
    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Item getItem() {
        String uuidString = getString(getColumnIndex(ItemTable.Cols.UUID));
        String title = getString(getColumnIndex(ItemTable.Cols.TITLE));
        long date = getLong(getColumnIndex(ItemTable.Cols.DATE));
        String details = getString((getColumnIndex(ItemTable.Cols.DETAILT)));

        Item item = new Item(UUID.fromString(uuidString));
        item.setTitle(title);
        item.setDate(new Date(date));
        item.setDescription(details);

        return item;
    }
}
