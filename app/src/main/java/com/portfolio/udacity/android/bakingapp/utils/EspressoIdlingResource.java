package com.portfolio.udacity.android.bakingapp.utils;

import android.support.test.espresso.IdlingResource;

/**
 * Created by JonGaming on 28/02/2018.
 * Mock build type.
 */

public class EspressoIdlingResource {
    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}