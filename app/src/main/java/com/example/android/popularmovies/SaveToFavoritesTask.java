package com.example.android.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;

import java.sql.SQLException;

/**
 * Created by Christian Utama on 7/10/2015.
 */
public class SaveToFavoritesTask extends AsyncTask<ContentValues, Void, Void>{

    private Context mContext;
    private boolean addSuccess;

    public SaveToFavoritesTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(ContentValues... params) {
        try {
            Uri uri;
            uri = mContext.getContentResolver().insert(MovieContract.MoviesEntry.FAVORITES_CONTENT_URI, params[0]);
//            Log.v("New Uri", uri.toString());
            addSuccess = true;
        } catch (Exception e){
            Log.e("Error", e.getMessage());
            addSuccess = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (addSuccess){
            Toast.makeText(mContext, "Added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Already on favorites", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(aVoid);
    }
}
