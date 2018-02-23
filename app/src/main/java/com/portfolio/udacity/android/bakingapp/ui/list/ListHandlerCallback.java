package com.portfolio.udacity.android.bakingapp.ui.list;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by JonGaming on 23/02/2018.
 * For handling list clicks...
 */

public interface ListHandlerCallback {
    void onListClick(int aPosition, String aId);
    void onListTouch(View aView, MotionEvent aMotionEvent);
}