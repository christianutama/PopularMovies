package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Christian Utama on 7/9/2015.
 */
public class MovieAdapter extends CursorAdapter {

    int test = 0;
    public MovieAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_layout,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView poster = (ImageView) view.findViewById(R.id.poster);
        String posterPath = cursor.getString(MainActivityFragment.COL_POSTER);
//        Log.v("POSTER PATH", "No." + cursor.getPosition() +":" + posterPath);
        if (posterPath.equals("null")) {
            Picasso.with(context).load(R.drawable.no_image).into(poster);
            return;
        }
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+posterPath)
                .into(poster);
    }
}
