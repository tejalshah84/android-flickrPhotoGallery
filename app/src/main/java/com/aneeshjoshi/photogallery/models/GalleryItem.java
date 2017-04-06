package com.aneeshjoshi.photogallery.models;

import com.google.gson.annotations.SerializedName;

public class GalleryItem {
    @SerializedName("id")
    String mId;

    @SerializedName("owner")
    String mOwner;

    @SerializedName("secret")
    String mSecret;

    @SerializedName("server")
    String mServer;

    @SerializedName("title")
    String mTitle;

    @SerializedName("url_s")
    String mUrl;

    public String toString(){
        return mTitle;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public String getSecret() {
        return mSecret;
    }

    public void setSecret(String secret) {
        mSecret = secret;
    }

    public String getServer() {
        return mServer;
    }

    public void setServer(String server) {
        mServer = server;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
