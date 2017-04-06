package com.aneeshjoshi.photogallery.models;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


public class GetPhotosResponse {

    @SerializedName("photos")
    PhotoMeta mPhotoMeta;

    @SerializedName("stat")
    String mStat;

    public PhotoMeta getPhotoMeta() {
        return mPhotoMeta;
    }

    public void setPhotoMeta(PhotoMeta photoMeta) {
        mPhotoMeta = photoMeta;
    }

    public String getStat() {
        return mStat;
    }

    public void setStat(String stat) {
        mStat = stat;
    }
}
