package com.aneeshjoshi.photogallery.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


public class PhotoMeta {

    @SerializedName("photo")
    List<GalleryItem> mGalleryItems;

    @SerializedName("page")
    int mPage;

    @SerializedName("pages")
    int mPages;

    @SerializedName("total")
    int mTotal;

    @SerializedName("perpage")
    int mPerPage;

    public List<GalleryItem> getGalleryItems() {
        return mGalleryItems;
    }

    public void setGalleryItems(List<GalleryItem> galleryItems) {
        mGalleryItems = galleryItems;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public int getPages() {
        return mPages;
    }

    public void setPages(int pages) {
        mPages = pages;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public int getPerPage() {
        return mPerPage;
    }

    public void setPerPage(int perPage) {
        mPerPage = perPage;
    }
}
