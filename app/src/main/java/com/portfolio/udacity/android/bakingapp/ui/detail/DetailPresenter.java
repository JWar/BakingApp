package com.portfolio.udacity.android.bakingapp.ui.detail;

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
 * Handles communication between data and view for Detail
 */

public class DetailPresenter implements DetailContract.PresenterDetail {
    @NonNull
    private final RecipeRepository mRecipeRepository;
    @NonNull
    private final BaseSchedulerProvider mBaseSchedulerProvider;
    @NonNull
    private final DetailContract.ViewDetail mViewDetail;
    @NonNull
    private final DetailContract.ActivityDetail mActivityDetail;
    private CompositeDisposable mDisposables;

    public DetailPresenter(@NonNull RecipeRepository aRecipeRepository,
                           @NonNull BaseSchedulerProvider aBaseSchedulerProvider,
                           @NonNull DetailContract.ViewDetail aViewDetail,
                           @NonNull DetailContract.ActivityDetail aActivityDetail) {
        mRecipeRepository=aRecipeRepository;
        mBaseSchedulerProvider=aBaseSchedulerProvider;
        mViewDetail=aViewDetail;
        mViewDetail.setPresenter(this);
        mActivityDetail=aActivityDetail;
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
                            mViewDetail.setRecipe(aRecipe);
                        } else {
                            mViewDetail.problemFindingData();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable aThrowable) throws Exception {
                        Utils.logDebug("DetailPresenter.getRecipe: "+aThrowable.getLocalizedMessage());
                        mViewDetail.problemFindingData();
                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void onStepClick(int aRecipeId, int aStepId) {
        mActivityDetail.onStepClick(aRecipeId,aStepId);
    }
}
