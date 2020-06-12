package com.nehaeff.arfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.nehaeff.arfinder.database.ItemDBSchema;
import com.nehaeff.arfinder.database.RoomBaseHelper;
import com.nehaeff.arfinder.database.RoomCursorWrapper;
import com.nehaeff.arfinder.database.RoomDBSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomLab {
    private static RoomLab sRoomLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static RoomLab get(Context context) {
        if (sRoomLab == null) {
            sRoomLab = new RoomLab(context);
        }
        return sRoomLab;
    }

    private RoomLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RoomBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addRoom(Room room) {
        ContentValues values = getContentValues(room);
        mDatabase.insert(RoomDBSchema.RoomTable.NAME, null, values);
    }

    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();

        RoomCursorWrapper cursor = queryRooms(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                rooms.add(cursor.getRoom());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return rooms;
    }

    public Room getRoom(UUID id) {

        RoomCursorWrapper cursor = queryRooms(RoomDBSchema.RoomTable.Cols.UUID + " = ?",
                new String[] { id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getRoom();

        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Room room) {
        File externalFileDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFileDir == null) {
            return null;
        }

        return new File(externalFileDir, room.getPhotoFilename());
    }

    public void updateRoom(Room room) {
        String uuidString = room.getId().toString();
        ContentValues values = getContentValues(room);

        mDatabase.update(RoomDBSchema.RoomTable.NAME, values,
                RoomDBSchema.RoomTable.Cols.UUID + " = ?",
                new String[]{ uuidString });
    }

    public void deleteRoom(Room room) {

        mDatabase.delete(RoomDBSchema.RoomTable.NAME, RoomDBSchema.RoomTable.Cols.UUID + " = ?",
                new String[] {room.getId().toString()});
    }

    private static ContentValues getContentValues(Room room) {
        ContentValues values = new ContentValues();
        values.put(RoomDBSchema.RoomTable.Cols.UUID, room.getId().toString());
        values.put(RoomDBSchema.RoomTable.Cols.TITLE, room.getTitle());

        return values;
    }

    private RoomCursorWrapper queryRooms(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RoomDBSchema.RoomTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new RoomCursorWrapper(cursor);
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }
}
