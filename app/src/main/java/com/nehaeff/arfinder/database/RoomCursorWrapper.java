package com.nehaeff.arfinder.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nehaeff.arfinder.Room;
import com.nehaeff.arfinder.database.RoomDBSchema.RoomTable;

import java.util.UUID;

public class RoomCursorWrapper extends CursorWrapper {
    public RoomCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Room getRoom() {
        String uuidString = getString(getColumnIndex(RoomTable.Cols.UUID));
        String title = getString(getColumnIndex(RoomTable.Cols.TITLE));

        Room room = new Room(UUID.fromString(uuidString));
        room.setTitle(title);

        return room;
    }
}
