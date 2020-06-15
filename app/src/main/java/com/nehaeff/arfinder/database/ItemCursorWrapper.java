package com.nehaeff.arfinder.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.google.ar.core.Pose;
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
        float qVar[] = new float[4];
        float tVar[] = new float[3];

        tVar[0] = getFloat(getColumnIndex(ItemTable.Cols.TX));
        tVar[1] = getFloat(getColumnIndex(ItemTable.Cols.TY));
        tVar[2] = getFloat(getColumnIndex(ItemTable.Cols.TZ));

        qVar[0] = getFloat(getColumnIndex(ItemTable.Cols.QX));
        qVar[1] = getFloat(getColumnIndex(ItemTable.Cols.QY));
        qVar[2] = getFloat(getColumnIndex(ItemTable.Cols.QZ));
        qVar[3] = getFloat(getColumnIndex(ItemTable.Cols.QW));

        Pose pose = new Pose(tVar, qVar);

        int arFlag = getInt(getColumnIndex(ItemTable.Cols.AR));

        Item item = new Item(UUID.fromString(uuidString));
        item.setTitle(title);
        item.setDate(new Date(date));
        item.setDescription(details);
        item.setPose(pose);
        item.setArFlag(arFlag);

        return item;
    }
}
