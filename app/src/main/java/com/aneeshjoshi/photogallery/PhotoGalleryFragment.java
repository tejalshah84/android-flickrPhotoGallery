package com.aneeshjoshi.photogallery;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.support.v4.view.MenuItemCompat;

import com.aneeshjoshi.photogallery.models.GalleryItem;
import com.aneeshjoshi.photogallery.models.GetPhotosResponse;

import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    GridView mGridView;
    ArrayList<GalleryItem> mItems;
    ThumbnailDownloader<ImageView> mThumbnailThread;
    private LruCache<String, Bitmap> mGalleryItemCache;

    @TargetApi(12)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        new FetchItemsTask().execute();

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/16th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 16;

        mGalleryItemCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        mThumbnailThread = new ThumbnailDownloader<>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>(){
            @Override
            public void onThumbnailDownloaded(String url, ImageView imageView, Bitmap thumbnail) {
                if(isVisible()){
                    mGalleryItemCache.put(url, thumbnail);
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mGridView = (GridView) v. findViewById(R.id.gridView);
        setupAdapter();
        return v;

    }

    void setupAdapter() {
        if(getActivity() == null || mGridView == null) return;

        if(mItems != null){
            mGridView.setAdapter(new GalleryItemAdapter(mItems));

        } else {
            mGridView.setAdapter(null);
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>{

        public static final int PER_PAGE = 100;
        public static final int PAGE = 1;
        public static final String EXTRAS = "url_s";
        public String query = "matrix";

        public FetchItemsTask(){};

        public FetchItemsTask(String query){
            this.query = query;
        }

        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {
            try{
                FlickrServiceInterface flickrServiceInterface = new FlickrFetchr().getServiceInterface();
                GetPhotosResponse result;
                if(query != null){
                    result = flickrServiceInterface.searchPhotos(PER_PAGE, PAGE, EXTRAS, query);
                } else {
                    result = flickrServiceInterface.getRecentPhotos(PER_PAGE, PAGE, EXTRAS);
                }
                Log.i(TAG, "Fetched contents from flicker" +  result);
                return (ArrayList<GalleryItem>) result.getPhotoMeta().getGalleryItems();
            } catch(Exception e){
                Log.e(TAG, "Error Fetching from flicker", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }

    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem> {

        public GalleryItemAdapter(ArrayList<GalleryItem> items){
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.gallery_item, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.brian_up_close);

            GalleryItem item = getItem(position);
            if(item == null || item.getUrl() == null || mGalleryItemCache.get(item.getUrl()) == null) {
                mThumbnailThread.queueThumbnail(imageView, item.getUrl());
            } else {
                Log.i(TAG, "Using LruCache for url: " + item.getUrl());
                imageView.setImageBitmap(mGalleryItemCache.get(item.getUrl()));
            }
            return convertView;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        MenuItem item = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(queryTextListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            //case R.id.menu_item_clear:
              //  return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener(){
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText.length()>=3) {
                new FetchItemsTask(newText).execute();
            }
            return false;
        }
    };
}

