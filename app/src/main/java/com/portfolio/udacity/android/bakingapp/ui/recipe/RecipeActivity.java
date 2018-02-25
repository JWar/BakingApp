package com.portfolio.udacity.android.bakingapp.ui.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.ui.detail.DetailActivity;
import com.portfolio.udacity.android.bakingapp.utils.Injection;
import com.portfolio.udacity.android.bakingapp.utils.Utils;

/**
 * Design:
 * RecipeActivity will have to handle Grid style/List style
 * Clicking Recipe will lead to a new DetailActivity, which will have the Ingredients and a
 * Step list.
 * Clicking on a step brings stepfragment up which has media player etc...
 *
 * TODO:180223_UI! Layouts! Presenter/View/Repositorys. MediaPlayer going to be fun... Testing too!
 */
public class RecipeActivity extends AppCompatActivity implements RecipeContract.ActivityRecipe {

    private RecipePresenter mRecipePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        try {
            RecipeFragment fragment = (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.activity_recipe_fragment_container);
            if (fragment==null) {
                fragment = new RecipeFragment();
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(RecipeFragment.TAG)
                        .add(R.id.activity_recipe_fragment_container,
                                fragment,
                                RecipeFragment.TAG)
                        .commit();
            }
            mRecipePresenter = new RecipePresenter(
                    Injection.provideRecipeRepository(
                            Injection.provideRecipeRemoteDataSource(Injection.provideBakingAppApi())),
                    Injection.provideSchedulerProvider(),
                    fragment,
                    this);
        } catch (Exception e) {
            Utils.logDebug("Error in RecipeActivity.onCreate: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onRecipeClick(int aRecipeId) {
        DetailActivity.start(this,aRecipeId);
    }

    @Override
    public void onBackPressed() {
        //This is because for some reason fragment manager pops fragment leaving activity on its own
        if (getSupportFragmentManager().getBackStackEntryCount()<2) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
