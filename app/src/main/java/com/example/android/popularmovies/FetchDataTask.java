package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Created by Christian Utama on 7/9/2015.
 */
public class FetchDataTask extends AsyncTask<String, Void, Void> {

    private Context mContext;
    private final String LOG_TAG = FetchDataTask.class.getSimpleName();
    public FetchDataTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        final String LOG_TAG = FetchDataTask.class.getSimpleName();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String dataJsonStr;
        ArrayList<String[]> result = null;

        final String BASE_API = "http://api.themoviedb.org/3/discover/movie?";
        final String API_KEY = "0ca54d94b9078cf39cf69b554c6dd0b9";

        Uri uri = Uri.parse(BASE_API).buildUpon()
                .appendQueryParameter("sort_by", params[0])
                .appendQueryParameter("api_key", API_KEY).build();

        String myUrl = uri.toString();
        try {
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                dataJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                dataJsonStr = null;
            }

            dataJsonStr = buffer.toString();
            getDataFromJson(dataJsonStr);

        } catch (Exception e){
            Log.e(LOG_TAG, "Error");
            dataJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }
//        if (dataJsonStr != null){
//            try{
//                result = getDataFromJson(dataJsonStr);
//            } catch (JSONException e){
//                Log.e(LOG_TAG, e.getMessage());
//            }
//        }

        return null;
    }

    private void getDataFromJson(String json) throws JSONException{
        final String ID = "id";
        final String TITLE = "original_title";
        final String PLOT = "overview";
        final String RATING = "vote_average";
        final String DATE = "release_date";
        final String POSTER = "poster_path";

//        final String[] ITERATE = {ID, TITLE, PLOT, RATING, DATE, POSTER};
        ArrayList<String[]> res = new ArrayList<>();

        JSONObject object = new JSONObject(json);
        JSONArray resultsArray = object.getJSONArray("results");

        Vector<ContentValues> contentValuesVector = new Vector<>(resultsArray.length());

        for (int i = 0; i < resultsArray.length(); i++){

            JSONObject curObject = resultsArray.getJSONObject(i);
            String id = curObject.getString(ID);

            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MoviesEntry.COL_ID, id);
            contentValues.put(MovieContract.MoviesEntry.COL_TITLE, curObject.getString(TITLE));
            contentValues.put(MovieContract.MoviesEntry.COL_PLOT, curObject.getString(PLOT));
            contentValues.put(MovieContract.MoviesEntry.COL_RATING, curObject.getString(RATING));
            contentValues.put(MovieContract.MoviesEntry.COL_DATE, curObject.getString(DATE));
            contentValues.put(MovieContract.MoviesEntry.COL_POSTER, curObject.getString(POSTER));
            contentValues.put(MovieContract.MoviesEntry.COL_TRAILER, getTrailerLink(id));

            contentValuesVector.add(contentValues);
        }

        int deleted;
        deleted = mContext.getContentResolver().delete(MovieContract.MoviesEntry.CONTENT_URI,
                null,
                null);
//        Log.v("Deleted", deleted + " entries");

        int inserted = 0;
        if (contentValuesVector.size() > 0){
            ContentValues[] values = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(values);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.MoviesEntry.CONTENT_URI, values);
        }

//        Log.v("Inserted", inserted + " entries");

//        for (String type:ITERATE){
//            String[] retValue = new String[resultsArray.length()];
//            for(int i = 0; i < resultsArray.length(); i++) {
//                retValue[i] = resultsArray.getJSONObject(i).getString(type);
//            }
//            res.add(retValue);
//        }
//        return res;
    }

    public String getTrailerLink(String id) {
        String link = "";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String VIDEOS_PATH = "videos";
            final String API_KEY_PARAM = "api_key";
            final String API_KEY = "0ca54d94b9078cf39cf69b554c6dd0b9";

            Uri uri = Uri.parse(TRAILER_BASE_URL).buildUpon()
                    .appendPath(id)
                    .appendPath(VIDEOS_PATH)
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return link;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            String linkJSON = buffer.toString();
//            Log.v("JSON", linkJSON);
            JSONObject jsonObject = new JSONObject(linkJSON);
            JSONArray results = jsonObject.getJSONArray("results");
//            Log.w("length of res", String.valueOf(results.length()));
            if (results.length() != 0) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject currentObject = results.getJSONObject(i);
//              Log.w("wth is this", currentObject.getString("key"));
                    link = link + currentObject.getString("key") + "|";
                }
            } else {link = "null";}


//            Log.w("Trailer string", link);
//            String[] parts = link.split(Pattern.quote("|"));
//
//            for (int i = 0; i < parts.length; i++){
//                Log.v("Split", parts[i]);
//            }


        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return link;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }


}