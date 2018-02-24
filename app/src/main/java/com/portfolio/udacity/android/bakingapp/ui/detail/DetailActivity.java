package com.portfolio.udacity.android.bakingapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.ui.detail.step.StepFragment;
import com.portfolio.udacity.android.bakingapp.ui.detail.step.StepPresenter;
import com.portfolio.udacity.android.bakingapp.utils.Injection;
import com.portfolio.udacity.android.bakingapp.utils.Utils;

/**
 * This will hold both Detail and Step.
 */
public class DetailActivity extends AppCompatActivity implements DetailContract.ActivityDetail {

    private static final String RECIPE_ID = "recipeId";
    private int mRecipeId;

    private DetailPresenter mDetailPresenter;
    private StepPresenter mStepPresenter;

    public static void start(Context aContext, int aRecipeId) {
        Intent intent = new Intent(aContext,DetailActivity.class);
        intent.putExtra(RECIPE_ID,aRecipeId);
        aContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //See onOptionsItemSelected
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
        } else {
            mRecipeId = getIntent().getIntExtra(RECIPE_ID, -1);
        }
        try {
            //180224_This is is where it gets interesting. Trying to avoid Tablet checks
            //Tablet View wil have same containers but separated, phone will have containers layered
            //on top. The idea is for phone the layers will layer automatically, i.e. if there is
            //nothing in step container then the detail container will be shown. If step container
            //becomes filled then it will naturally just be on top of the detail container. No need
            //to faff about with any sort of Tablet check or anything like that.
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_detail_fragment_container);
            if (detailFragment == null) {//Definitely first onCreate and no previous fragments.
                detailFragment = DetailFragment.newInstance(mRecipeId);
                mDetailPresenter = new DetailPresenter(
                        Injection.provideRecipeRepository(
                                Injection.provideRecipeRemoteDataSource(Injection.provideBakingAppApi())),
                        Injection.provideSchedulerProvider(),
                        detailFragment,
                        this);
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(DetailFragment.TAG)
                        .add(R.id.activity_detail_detail_fragment_container,
                                detailFragment,
                                DetailFragment.TAG)
                        .commit();
            } else {//Detail fragment present. Check for Step Fragment
                mDetailPresenter = new DetailPresenter(
                        Injection.provideRecipeRepository(
                                Injection.provideRecipeRemoteDataSource(Injection.provideBakingAppApi())),
                        Injection.provideSchedulerProvider(),
                        detailFragment,
                        this);
                StepFragment stepFragment = (StepFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_step_fragment_container);
                if (stepFragment != null) {//StepFragment present!
                    mStepPresenter = new StepPresenter(
                            Injection.provideRecipeRepository(
                                    Injection.provideRecipeRemoteDataSource(Injection.provideBakingAppApi())),
                            Injection.provideSchedulerProvider(),
                            stepFragment);
                }
            }
        } catch (Exception e) {
            Utils.logDebug("Error in DetailActivity.onCreate: " + e.getLocalizedMessage());
        }
    }
    @Override
    public void onStepClick(int aRecipeId, int aStepId) {
        try {
            StepFragment stepFragment = StepFragment.newInstance(aRecipeId, aStepId);
            mStepPresenter = new StepPresenter(
                    Injection.provideRecipeRepository(
                            Injection.provideRecipeRemoteDataSource(Injection.provideBakingAppApi())),
                    Injection.provideSchedulerProvider(),
                    stepFragment);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(StepFragment.TAG)
                    .add(R.id.activity_detail_step_fragment_container,
                            stepFragment,
                            StepFragment.TAG)
                    .commit();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.problem_loading_screen), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //This is so Back Button goes back to previous Activity and doesnt recreate Parent Activity.
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
