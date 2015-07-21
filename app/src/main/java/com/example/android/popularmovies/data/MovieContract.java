package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Christian Utama on 7/9/2015.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVORITES = "favorites";

    public static final class MoviesEntry implements BaseColumns{

        public static final String TABLE_NAME = "movies";
        public static final String FAVORITE_TABLE_NAME = "favorites";

        public static final String COL_ID = "id";
        public static final String COL_TITLE = "title";
        public static final String COL_PLOT = "plot";
        public static final String COL_RATING = "rating";
        public static final String COL_DATE = "date";
        public static final String COL_POSTER = "poster";
        public static final String COL_TRAILER = "trailer";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
                appendPath(PATH_MOVIES).build();

        public static final Uri FAVORITES_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String FAVORITE_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final String FAVORITE_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoritesUri(long id) {
            return ContentUris.withAppendedId(FAVORITES_CONTENT_URI, id);
        }

        public static String getIdFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

    }

}
