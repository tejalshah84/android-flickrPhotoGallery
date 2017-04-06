package com.aneeshjoshi.photogallery;

import com.aneeshjoshi.photogallery.models.GetPhotosResponse;

import retrofit.http.GET;
import retrofit.http.Query;

public interface FlickrServiceInterface {

    @GET("/services/rest/?method=flickr.photos.getRecent")
    public GetPhotosResponse getRecentPhotos(@Query("per_page") int perPage, @Query("page") int page, @Query("extras") String extras);

    @GET("/services/rest/?method=flickr.photos.search")
    public GetPhotosResponse searchPhotos(@Query("per_page") int perPage, @Query("page") int page, @Query("extras") String extras, @Query("text") String term);

}
