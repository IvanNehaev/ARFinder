package com.nehaeff.arfinder;

import java.util.UUID;

public class Room {
    private UUID mId;
    private String mTitle;

    public Room() {
        this.mId = UUID.randomUUID();
    }

    public Room(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getPhotoFilename() {
        return "IMG_ROOM_" + getId().toString() + ".jpg";
    }
}
