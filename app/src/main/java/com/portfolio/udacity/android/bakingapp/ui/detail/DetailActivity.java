package com.portfolio.udacity.android.bakingapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.ui.stepdetail.StepDetailContract;
import com.portfolio.udacity.android.bakingapp.ui.stepdetail.StepDetailFragment;
import com.portfolio.udacity.android.bakingapp.ui.stepdetail.StepDetailPresenter;
import com.portfolio.udacity.android.bakingapp.utils.Injection;
import com.portfolio.udacity.android.bakingapp.utils.Utils;

/**
 * This will hold both Detail and Step.
 */
public class DetailActivity extends AppCompatActivity implements DetailContract.ActivityDetail,
        StepDetailContract.ActivityStep {

    private static final String RECIPE_ID = "recipeId";
    private int mRecipeId;

    private DetailPresenter mDetailPresenter;
    private StepDetailPresenter mStepDetailPresenter;

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
                StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_step_fragment_container);
                if (stepDetailFragment != null) {//StepDetailFragment present!
                    mStepDetailPresenter = new StepDetailPresenter(
                            Injection.provideRecipeRepository(
                                    Injection.provideRecipeRemoteDataSource(Injection.provideBakingAppApi())),
                            Injection.provideSchedulerProvider(),
                            stepDetailFragment,
                            this);
                }
            }
        } catch (Exception e) {
            Utils.logDebug("Error in DetailActivity.onCreate: " + e.getLocalizedMessage());
        }
    }
    @Override
    public void onStepClick(int aRecipeId, int aStepId) {
        try {
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(aRecipeId, aStepId);
            mStepDetailPresenter = new StepDetailPresenter(
                    Injection.provideRecipeRepository(
                            Injection.provideRecipeRemoteDataSource(Injection.provideBakingAppApi())),
                    Injection.provideSchedulerProvider(),
                    stepDetailFragment,
                    this);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(StepDetailFragment.TAG)
                    .add(R.id.activity_detail_step_fragment_container,
                            stepDetailFragment,
                            StepDetailFragment.TAG)
                    .commit();
        } catch (Exception e) {
            Utils.logDebug("Error in DetailActivity.onStepClick: "+e.getLocalizedMessage());
            Toast.makeText(this, getString(R.string.problem_loading_screen), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpClick(int aRecipeId, int aStepId) {
        Utils.logDebug("ActonUpClick");
        if (aStepId!=0) {
            stepNavigation(aRecipeId, aStepId - 1);
        }
    }

    @Override
    public void onDownClick(int aRecipeId, int aStepId, int aStepsLen) {
        if (aStepId<(aStepsLen-1)) {
            stepNavigation(aRecipeId, aStepId + 1);
        }
    }
    private void stepNavigation(int aRecipeId, int aStepId) {
        try {
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(aRecipeId, aStepId);
            mStepDetailPresenter.setView(stepDetailFragment);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(StepDetailFragment.TAG)
                    .replace(R.id.activity_detail_step_fragment_container,
                            stepDetailFragment,
                            StepDetailFragment.TAG)
                    .commit();
        } catch (Exception e) {
            Utils.logDebug("Error in DetailActivity.onUpClick: "+e.getLocalizedMessage());
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
