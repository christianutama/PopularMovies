package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Christian Utama on 7/9/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MoviesEntry.TABLE_NAME + " (" +
                MovieContract.MoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MoviesEntry.COL_ID + " TEXT UNIQUE NOT NULL," +
                MovieContract.MoviesEntry.COL_TITLE + " TEXT NOT NULL," +
                MovieContract.MoviesEntry.COL_PLOT + " TEXT NOT NULL," +
                MovieContract.MoviesEntry.COL_RATING + " REAL NOT NULL," +
                MovieContract.MoviesEntry.COL_DATE + " TEXT NOT NULL," +
                MovieContract.MoviesEntry.COL_POSTER + " TEXT NOT NULL," +
                MovieContract.MoviesEntry.COL_TRAILER + " TEXT NOT NULL" + ");";

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + MovieContract.MoviesEntry.FAVORITE_TABLE_NAME + " (" +
                MovieContract.MoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MoviesEntry.COL_ID + " TEXT UNIQUE NOT NULL," +
                MovieContract.MoviesEntry.COL_TITLE + " TEXT NOT NULL," +
                MovieContract.MoviesEntry.COL_PLOT + " TEXT NOT NULL," +
                MovieContract.MoviesEntry.COL_RATING + " REAL NOT NULL," +
                MovieContract.MoviesEntry.COL_DATE + " TEXT NOT NULL," +
                MovieContract.MoviesEntry.COL_POSTER + " TEXT NOT NULL," +
                MovieContract.MoviesEntry.COL_TRAILER + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntry.FAVORITE_TABLE_NAME);
        onCreate(db);
    }
}
