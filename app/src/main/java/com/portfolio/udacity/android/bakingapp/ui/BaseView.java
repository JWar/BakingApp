package com.portfolio.udacity.android.bakingapp.ui;

/**
 * Created by JonGaming on 24/02/2018.
 * Determines methods all views must have
 */

public interface BaseView<T> {
    //Could return something a little bit more detailed (i.e. put something in param)?
    void problemFindingData();
    void setPresenter(T aPresenter);
}
