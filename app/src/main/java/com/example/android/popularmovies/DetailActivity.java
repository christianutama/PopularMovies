package com.example.android.popularmovies;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivity extends ActionBarActivity {

    private String title, plot, rating, date, poster;

    public static final String DETAIL_URI = "uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DETAIL_URI, getIntent().getData());

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, detailActivityFragment)
                    .commit();
        }
//        title = getIntent().getExtras().getString(MainActivityFragment.TITLE_KEY);
//        plot = getIntent().getExtras().getString(MainActivityFragment.PLOT_KEY);
//        rating = getIntent().getExtras().getString(MainActivityFragment.RATING_KEY);
//        date = getIntent().getExtras().getString(MainActivityFragment.DATE_KEY);
//        poster = getIntent().getExtras().getString(MainActivityFragment.POSTER_KEY);
//
//        TextView titleView = (TextView) findViewById(R.id.title_text_view);
//        TextView plotView = (TextView) findViewById(R.id.plot_text_view);
//        TextView ratingView = (TextView) findViewById(R.id.rating_text_view);
//        TextView dateView = (TextView) findViewById(R.id.date_text_view);
//        ImageView posterView = (ImageView) findViewById(R.id.poster_view);
//
//        titleView.setText(title);
//        plotView.setText(plot);
//        ratingView.setText(rating);
//        dateView.setText(date);
//        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/"+ poster)
//                .into(posterView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
