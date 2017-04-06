package com.aneeshjoshi.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

//Code referenced from Josh Skeen from github.com/@mutexkid
public class FlickrFetchr {

    private static final String SERVER_URL = "https://api.flickr.com";
    private static final String API_KEY = "ff0a9e00e05ba69e75a6b10478cb5e5f";
    private static final String TAG = "FlickrFetch";


    public FlickrServiceInterface getServiceInterface(){
        return getRestAdapter().create(FlickrServiceInterface.class);
    }

    public RestAdapter getRestAdapter(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("api_key", API_KEY);
                        request.addQueryParam("format", "json");
                        request.addQueryParam("nojsoncallback", "1");//disable the JSONP callback stuff flickr seems to assume we want
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog(TAG))
                .build();

        return restAdapter;
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out  = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return null;
            }
            int bytesRead = 0;

            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0){
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }


}
