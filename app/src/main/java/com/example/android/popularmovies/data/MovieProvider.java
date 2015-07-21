package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Christian Utama on 7/9/2015.
 */
public class MovieProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;
    static final int MOVIE = 100;
    static final int SINGLE_MOVIE = 101;
    static final int FAVORITE = 102;
    static final int SINGLE_FAVORITE = 103;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case SINGLE_MOVIE:
                String id = MovieContract.MoviesEntry.getIdFromUri(uri);
                String select = MovieContract.MoviesEntry._ID + " = ? ";
                retCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MoviesEntry.TABLE_NAME,
                                projection,
                                select,
                                new String[]{id},
                                null,
                                null,
                                sortOrder);
                break;
            case MOVIE:
                retCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MoviesEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            case SINGLE_FAVORITE:
                String idFav = MovieContract.MoviesEntry.getIdFromUri(uri);
                String selectFav = MovieContract.MoviesEntry._ID + " = ? ";
                retCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MoviesEntry.FAVORITE_TABLE_NAME,
                                projection,
                                selectFav,
                                new String[]{idFav},
                                null,
                                null,
                                sortOrder);
                break;
            case FAVORITE:
                retCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MoviesEntry.FAVORITE_TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case SINGLE_MOVIE:
                return MovieContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return MovieContract.MoviesEntry.CONTENT_TYPE;
            case SINGLE_FAVORITE:
                return MovieContract.MoviesEntry.FAVORITE_ITEM_TYPE;
            case FAVORITE:
                return MovieContract.MoviesEntry.FAVORITE_CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIE:
                long _id = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MoviesEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case FAVORITE:
                long _id2 = db.insert(MovieContract.MoviesEntry.FAVORITE_TABLE_NAME, null, values);
                if ( _id2 > 0 )
                    returnUri = MovieContract.MoviesEntry.buildFavoritesUri(_id2);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match){
            case MOVIE:
                rowsDeleted = db.delete(MovieContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE:
                rowsDeleted = db.delete(MovieContract.MoviesEntry.FAVORITE_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MoviesEntry.TABLE_NAME, values,
                        selection,
                        selectionArgs);
                break;
            case FAVORITE:
                rowsUpdated = db.update(MovieContract.MoviesEntry.FAVORITE_TABLE_NAME, values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case FAVORITE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MoviesEntry.FAVORITE_TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/*", SINGLE_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITES, FAVORITE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITES + "/*", SINGLE_FAVORITE);
        return uriMatcher;
    }
}
