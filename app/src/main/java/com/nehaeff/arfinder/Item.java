package com.nehaeff.arfinder;

import com.google.ar.core.Pose;
import com.nehaeff.arfinder.database.ItemDBSchema;

import java.util.Date;
import java.util.UUID;

public class Item {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mDescription;
    private Pose mPose;
    private int mAr;

    public Item() {
        this(UUID.randomUUID());
    }

    public Item(UUID id) {
        mId = id;
        mDate = new Date();
        mAr = 0;
        float qVar[] = new float[4];
        float tVar[] = new float[3];

        tVar[0] = 0;
        tVar[1] = 0;
        tVar[2] = 0;

        qVar[0] = 0;
        qVar[1] = 0;
        qVar[2] = 0;
        qVar[3] = 0;

        Pose pose = new Pose(tVar, qVar);
        mPose = pose;
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

    public  void setDate(Date date) {
        mDate = date;
    }

    public Date getDate() {
        return mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public Pose getPose() {
        return mPose;
    }

    public void setPose(Pose pose) {
        mPose = pose;
    }

    public void setArFlag(int state) {
        mAr = state;
    }

    public int getArFlag() {
        return mAr;
    }
}
