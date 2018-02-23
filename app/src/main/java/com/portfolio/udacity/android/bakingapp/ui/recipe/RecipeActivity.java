package com.portfolio.udacity.android.bakingapp.ui.recipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.source.remote.BakingAppApi;
import com.portfolio.udacity.android.bakingapp.utils.Injection;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Design:
 * RecipeActivity will have to handle Grid style/List style
 * Clicking Recipe will lead to a new DetailActivity, which will have the Ingredients and a
 * Step list.
 * Clicking on a step brings stepfragment up which has media player etc...
 *
 * TODO:180223_UI! Layouts! Presenter/View/Repositorys. MediaPlayer going to be fun... Testing too!
 */
public class RecipeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BakingApp";

    public static void logDebug(String aMsg) {
        Log.i(LOG_TAG,aMsg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        try {
            BakingAppApi bakingAppApi = Injection.provideBakingAppApi();
            BaseSchedulerProvider schedulerProvider = Injection.provideSchedulerProvider();
            bakingAppApi
                    .getRecipes()
                    .observeOn(schedulerProvider.ui())
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(new Consumer<List<Recipe>>() {
                        @Override
                        public void accept(List<Recipe> aS) throws Exception {
                            Recipe recipe = aS.get(0);

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable aThrowable) throws Exception {
                            RecipeActivity.logDebug("Error in RecipeActivity.onCreate API: " + aThrowable.getLocalizedMessage());
                        }
                    });
        } catch (Exception e) {
            RecipeActivity.logDebug("Error in RecipeActivity.onCreate: " + e.getLocalizedMessage());
        }
    }
}
