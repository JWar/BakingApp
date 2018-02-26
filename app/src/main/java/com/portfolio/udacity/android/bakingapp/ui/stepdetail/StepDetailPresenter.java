package com.portfolio.udacity.android.bakingapp.ui.stepdetail;

import android.support.annotation.NonNull;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.utils.Utils;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by JonGaming on 24/02/2018.
 *
 */

public class StepDetailPresenter implements StepDetailContract.PresenterStep {

    @NonNull
    private final RecipeRepository mRecipeRepository;
    @NonNull
    private final BaseSchedulerProvider mBaseSchedulerProvider;
    //Sigh not final due to need to change View. Would it be better to remake presenter?
    @NonNull
    private StepDetailContract.ViewStep mViewStep;
    @NonNull
    private final StepDetailContract.ActivityStep mActivityStep;

    private CompositeDisposable mDisposables;

    public StepDetailPresenter(@NonNull RecipeRepository aRecipeRepository,
                               @NonNull BaseSchedulerProvider aBaseSchedulerProvider,
                               @NonNull StepDetailContract.ViewStep aViewStep,
                               @NonNull StepDetailContract.ActivityStep aActivityStep) {
        mRecipeRepository=aRecipeRepository;
        mBaseSchedulerProvider=aBaseSchedulerProvider;
        mViewStep=aViewStep;
        mViewStep.setPresenter(this);
        mActivityStep=aActivityStep;
        mDisposables=new CompositeDisposable();
    }


    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }

    @Override
    public void getRecipe(int aRecipeId) {
        Disposable disposable = Observable.just(mRecipeRepository.getRecipe(aRecipeId))
                .observeOn(mBaseSchedulerProvider.ui())
                .subscribeOn(mBaseSchedulerProvider.io())
                .subscribe(new Consumer<Recipe>() {
                    @Override
                    public void accept(Recipe aRecipe) throws Exception {
                        if (aRecipe!=null) {
                            mViewStep.setRecipe(aRecipe);
                        } else {
                            mViewStep.problemFindingData();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable aThrowable) throws Exception {
                        Utils.logDebug("StepDetailPresenter.getRecipe: "+aThrowable.getLocalizedMessage());
                        mViewStep.problemFindingData();
                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void onUpClick(int aRecipeId, int aStepId) {
        Utils.logDebug("onUpClick");
        mActivityStep.onUpClick(aRecipeId,aStepId);
    }

    @Override
    public void onDownClick(int aRecipeId, int aStepId) {
        Utils.logDebug("onDownClick");
        mActivityStep.onDownClick(aRecipeId,aStepId,mRecipeRepository.getRecipe(aRecipeId).mSteps.size());
    }

    @Override
    public void setView(StepDetailContract.ViewStep aViewStep) {
        mViewStep=aViewStep;
        mViewStep.setPresenter(this);
        //Clear disposables if view is changing.
        unSubscribe();
    }
}
