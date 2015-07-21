package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.data.MovieContract;

import java.sql.SQLException;

/**
 * Created by Christian Utama on 7/10/2015.
 */
public class SaveToFavoritesTask extends AsyncTask<ContentValues, Void, Void>{

    private Context mContext;

    public SaveToFavoritesTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(ContentValues... params) {
        try {
            Uri uri;
            uri = mContext.getContentResolver().insert(MovieContract.MoviesEntry.FAVORITES_CONTENT_URI, params[0]);
            Log.v("New Uri", uri.toString());
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        return null;
    }
}
