package com.example.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Pattern;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    public DetailActivityFragment() {
    }


    private Uri mUri;
    private ImageView poster;
    private TextView title, plot, rating, date;
    private static final int LOADER_ID = 9;
    private Button addToFavorites;
    private Cursor cursor;
    private ListView trailer;
    private String[] trailerLink;
    private ArrayAdapter<String> mAdapter;
    private LinearLayout dynamicLinearLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        Bundle bundle = getArguments();
        if (bundle != null){
            mUri = bundle.getParcelable(DetailActivity.DETAIL_URI);
        }

        Log.v("Uri", mUri.toString());

        poster = (ImageView) rootView.findViewById(R.id.poster_view);
        title = (TextView) rootView.findViewById(R.id.title_text_view);
        plot = (TextView) rootView.findViewById(R.id.plot_text_view);
        rating = (TextView) rootView.findViewById(R.id.rating_text_view);
        date = (TextView) rootView.findViewById(R.id.date_text_view);

        trailer = (ListView) rootView.findViewById(R.id.trailer_list_view);
        dynamicLinearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_dynamic);
//        trailer.setVisibility(View.GONE);

        addToFavorites = (Button) rootView.findViewById(R.id.fav_button);
        addToFavorites.setOnClickListener(this);



        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null) {
            return new CursorLoader(getActivity(),
                    mUri,
                    MainActivityFragment.MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()){
            return;
        }
        cursor = data;
        String posterPath = data.getString(MainActivityFragment.COL_POSTER);
        if (posterPath.equals("null")) {
            Picasso.with(getActivity()).load(R.drawable.no_image).into(poster);
        } else {
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + posterPath)
                    .into(poster);
        }


        title.setText(data.getString(MainActivityFragment.COL_TITLE));
        plot.setText(data.getString(MainActivityFragment.COL_PLOT));
        rating.setText(data.getString(MainActivityFragment.COL_RATING));
        date.setText(data.getString(MainActivityFragment.COL_DATE));
        String trailerString = data.getString(MainActivityFragment.COL_TRAILER);
        trailerLink = trailerString.split(Pattern.quote("|"));

        for (int i = 0; i < trailerLink.length; i++){
            Log.v("parts parts parts", trailerLink[i]);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(layoutParams);
            textView.setText(trailerLink[i]);
            dynamicLinearLayout.addView(textView);
        }

        mAdapter = new PerformanceArrayAdapter(getActivity(), trailerLink);
        Log.v("GETITEM", mAdapter.getItem(0));
        Log.v("GETITEM 2", mAdapter.getItem(1));
        trailer.setAdapter(mAdapter);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        if (!cursor.moveToFirst()){
            return;
        }
        Toast.makeText(getActivity(), "ADD? Y U DO DIS", Toast.LENGTH_LONG).show();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MoviesEntry.COL_ID, cursor.getString(MainActivityFragment.COL_ID));
        contentValues.put(MovieContract.MoviesEntry.COL_TITLE, cursor.getString(MainActivityFragment.COL_TITLE));
        contentValues.put(MovieContract.MoviesEntry.COL_PLOT, cursor.getString(MainActivityFragment.COL_PLOT));
        contentValues.put(MovieContract.MoviesEntry.COL_RATING, cursor.getString(MainActivityFragment.COL_RATING));
        contentValues.put(MovieContract.MoviesEntry.COL_DATE, cursor.getString(MainActivityFragment.COL_DATE));
        contentValues.put(MovieContract.MoviesEntry.COL_POSTER, cursor.getString(MainActivityFragment.COL_POSTER));
        contentValues.put(MovieContract.MoviesEntry.COL_TRAILER, cursor.getString(MainActivityFragment.COL_TRAILER));

        SaveToFavoritesTask saveToFavoritesTask = new SaveToFavoritesTask(getActivity());
        saveToFavoritesTask.execute(contentValues);

    }

    @Override
    public void onDestroy() {
        if (!cursor.isClosed()){
            cursor.close();
        }
        super.onDestroy();
    }


}
