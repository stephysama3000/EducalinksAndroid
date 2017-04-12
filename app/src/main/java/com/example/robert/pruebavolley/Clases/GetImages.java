package com.example.robert.pruebavolley.Clases;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.robert.pruebavolley.R;

import java.net.URL;
import java.net.URLConnection;

public class GetImages extends AsyncTask<Object, Object, Object> {
    private String requestUrl, imagename_;
    private int i_;
    private Bitmap[] images;

    private GetImages(String requestUrl, String _imagename_, int i ) {
        this.requestUrl = requestUrl;
        this.imagename_ = _imagename_;
        this.i_ = i;
    }

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            java.net.URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();
            images[i_] = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception ex) {
            images[i_] = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_profile);
            Log.d("error en getimages:",ex.toString());
        }
        return images[i_];
    }

    @Override
    protected void onPostExecute(Object o) {
        if (!ImageStorage.checkifImageExists(imagename_)) {
            ImageStorage.saveToSdCard(images[i_], imagename_);
        }
    }
}