package com.henallux.sportapp.DataAccess.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.utils.ObjectUtils;
import com.henallux.sportapp.DataAccess.RecupToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fran on 05-12-17.
 */

public class CloudinaryAccess {
    private Cloudinary cloudinary;
    private Uri photoUri;
    private Map config=null;
    private Context context;
    private String urlPhoto;
    private static CloudinaryAccess ourInstance = null;

    private CloudinaryAccess(Context context){
        this.context=context;
        config();
    }

    public static CloudinaryAccess CloudinaryAccess(Context context) {
        if(ourInstance==null){
            ourInstance=new CloudinaryAccess(context);
        }
        return ourInstance;
    }

    public void config(){
        config = new HashMap();
        config.put("cloud_name", "dz1qqniju");
        config.put("api_key", "638647662212323");
        config.put("api_secret", "vhdDMqszifcaOml_xWrKcFGkzMQ");
        MediaManager.init(context, config);
        cloudinary = MediaManager.get().getCloudinary();
    }

    public void uploadPhoto(){
        new AsynUpload().execute();
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public String getUrlPhoto(){
        return this.urlPhoto;
    }

    private class AsynUpload extends AsyncTask<Void, Void, Map> {
        protected Map doInBackground(Void ... v){
            try {
                return cloudinary.uploader().upload(context.getContentResolver().openInputStream(photoUri),ObjectUtils.emptyMap());
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Map map) {
            urlPhoto = map.get("secure_url").toString();
        }
    }



}