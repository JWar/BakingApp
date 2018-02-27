package com.portfolio.udacity.android.bakingapp.utils.schedulers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JonGaming on 22/02/2018.
 *
 */

public class ImmediateSchedulerProvider implements BaseSchedulerProvider {
    @Nullable
    private static ImmediateSchedulerProvider INSTANCE;

    // Prevent direct instantiation.
    private ImmediateSchedulerProvider() {
    }

    public static synchronized ImmediateSchedulerProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImmediateSchedulerProvider();
        }
        return INSTANCE;
    }
    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return Schedulers.trampoline();
    }
}