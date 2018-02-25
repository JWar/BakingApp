package com.portfolio.udacity.android.bakingapp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by JonGaming on 23/02/2018.
 * For those methods that just don't have a proper home...
 */

public class Utils {
    private static final String LOG_TAG = "BakingApp";
    public static void logDebug(String aMsg) {
        Log.i(LOG_TAG,aMsg);
    }
    //Needed to check difference between table and phone.
    //Basically layout automatically handles it but if Im going to be using different views
    //for the Tablet layout, then the code needs to be different...
    public static boolean isTablet(Context aContext) {
        return aContext.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    //These are needed to tidy up Ingredients
    public static String stringToFirstCapital (String aWord) {
        return aWord.toUpperCase().charAt(0) + aWord.substring(1).toLowerCase();
    }
    public static String stringToTitleCase (String aInputString) {
        String [] split = aInputString.split(" ");
        for (int i = 0; i < split.length; i++) {
            split[i] = stringToFirstCapital(split[i]);
        }
        return TextUtils.join(" ", split);
    }

}
