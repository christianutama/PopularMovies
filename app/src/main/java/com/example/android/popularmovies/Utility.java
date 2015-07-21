package com.example.android.popularmovies;

import android.content.Context;

/**
 * Created by Christian Utama on 7/21/2015.
 */
public class Utility {

    public static int convertDpToPx(int dp, float scale){
        int dpInPx = (int) (dp * scale + 0.5f);
        return dpInPx;
    }

}
