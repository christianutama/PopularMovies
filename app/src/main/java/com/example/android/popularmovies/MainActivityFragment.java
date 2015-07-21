package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public GridView gridView;

    private final String POPULAR_TAG = "popularity.desc";
    private final String RATING_TAG = "vote_average.desc";
//    public ArrayList<String[]> store = null;
//    public static final String TITLE_KEY = "title";
//    public static final String PLOT_KEY = "plot";
//    public static final String RATING_KEY = "rating";
//    public static final String DATE_KEY = "date";
//    public static final String POSTER_KEY = "poster";

    public static final String[] MOVIE_COLUMNS = new String[]{
            MovieContract.MoviesEntry._ID,
            MovieContract.MoviesEntry.COL_ID,
            MovieContract.MoviesEntry.COL_TITLE,
            MovieContract.MoviesEntry.COL_PLOT,
            MovieContract.MoviesEntry.COL_RATING,
            MovieContract.MoviesEntry.COL_DATE,
            MovieContract.MoviesEntry.COL_POSTER,
            MovieContract.MoviesEntry.COL_TRAILER
    };

    public static final int _ID = 0;
    public static final int COL_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_PLOT = 3;
    public static final int COL_RATING = 4;
    public static final int COL_DATE = 5;
    public static final int COL_POSTER = 6;
    public static final int COL_TRAILER = 7;

    private static final int LOADER_ID = 0;
    private boolean isInFavorites = false;

    private MovieAdapter mAdapter;


    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FetchDataTask fetchDataTask = new FetchDataTask(getActivity());
        fetchDataTask.execute(POPULAR_TAG);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public void updateData(String query){
        if (!isInFavorites) {
            FetchDataTask fetchDataTask = new FetchDataTask(getActivity());
            fetchDataTask.execute(query);
        }
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        gridView = (GridView) rootView.findViewById(R.id.grid_view);
//        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI,
//                MOVIE_COLUMNS,
//                null,
//                null,
//                null);
        mAdapter = new MovieAdapter(getActivity(), null, 0);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Log.v("Clicked", "item at " + cursor.getString(_ID));
                Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_LONG).show();
                Uri uri;
                if (isInFavorites){
                    uri = MovieContract.MoviesEntry.buildFavoritesUri(id);
                } else {
                    uri = MovieContract.MoviesEntry.buildMoviesUri(id);
                }
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        //titleAdapter = new ArrayAdapter<>(getActivity(),
        //        R.layout.list_layout, R.id.title_text_view, new ArrayList<String>());
        /*
        String[] mock = {"Aven", "Gers"};
        String[] mockPlot = {"So funny", "HAHA"};
        String[] mockRating = {"9.2", "9.1"};
        String[] mockDate = {"Jul 2014", "Dec 2015"};
        myAdapter = new MyAdapter(getActivity(),mock, mockPlot, mockRating, mockDate);
        listView.setAdapter(myAdapter);
        */
//
//        new FetchDataTask().execute(POPULAR_TAG);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent = new Intent(getActivity(), DetailActivity.class);
////                intent.putExtra(TITLE_KEY, store.get(0)[position]);
////                intent.putExtra(PLOT_KEY, store.get(1)[position]);
////                intent.putExtra(RATING_KEY, store.get(2)[position]);
////                intent.putExtra(DATE_KEY, store.get(3)[position]);
////                intent.putExtra(POSTER_KEY, store.get(4)[position]);
////                startActivity(intent);
////                Toast.makeText(getActivity(), store.get(0)[position],Toast.LENGTH_LONG).show();
//            }
//        });


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_most_popular:
                isInFavorites = false;
                updateData(POPULAR_TAG);
                return true;
            case R.id.action_high_rating:
                isInFavorites = false;
                updateData(RATING_TAG);
                return true;
            case R.id.action_favorites:
                isInFavorites = true;
                updateData("fav");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Log.v("In", "CREATELOADER");
        if (isInFavorites){
            return new CursorLoader(getActivity(),
                    MovieContract.MoviesEntry.FAVORITES_CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }
        return new CursorLoader(getActivity(),
                MovieContract.MoviesEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        Log.v("In", "FINISHED");
        if (!data.moveToFirst()) {
            Log.v("HEY", "CURSOR IS EMPTY BIATCH");
            mAdapter.swapCursor(null);
            return;
        }
        mAdapter.swapCursor(data);
        gridView.smoothScrollToPosition(0);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        Log.v("In", "RESET");
            mAdapter.swapCursor(null);

    }
}
