package com.portfolio.udacity.android.bakingapp.ui.detail;

import android.support.annotation.NonNull;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.utils.EspressoIdlingResource;
import com.portfolio.udacity.android.bakingapp.utils.Utils;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
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

    //Welp this is horrible and clearly not using RxJava properly.
    //Must be a way of doing all this check recipe is null handling in one observable??
    @Override
    public void getRecipe(final int aRecipeId) {
        EspressoIdlingResource.increment(); // App is busy until further notice
        if (mRecipeRepository.isRecipesNull()) {
            Disposable disposable = mRecipeRepository.getRecipes()
                    .observeOn(mBaseSchedulerProvider.ui())
                    .subscribeOn(mBaseSchedulerProvider.io())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                EspressoIdlingResource.decrement(); // Set app as idle.
                            }
                        }})
                    .subscribe(new Consumer<List<Recipe>>() {
                        @Override
                        public void accept(List<Recipe> aRecipes) throws Exception {
                            if (aRecipes != null) {
                                mRecipeRepository.setRecipes(aRecipes);
                                mViewDetail.setRecipe(mRecipeRepository.getRecipe(aRecipeId));
                            } else {
                                mViewDetail.problemFindingData();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable aThrowable) throws Exception {
                            Utils.logDebug("DetailPresenter.getRecipes: " + aThrowable.getLocalizedMessage());
                            mViewDetail.problemFindingData();
                        }
                    });
            mDisposables.add(disposable);
        } else {
            Disposable disposable = Observable.just(mRecipeRepository.getRecipe(aRecipeId))
                    .observeOn(mBaseSchedulerProvider.ui())
                    .subscribeOn(mBaseSchedulerProvider.io())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                EspressoIdlingResource.decrement(); // Set app as idle.
                            }
                        }
                    })
                    .subscribe(new Consumer<Recipe>() {
                        @Override
                        public void accept(Recipe aRecipe) throws Exception {
                            if (aRecipe != null&&aRecipe.mId>0) {
                                mViewDetail.setRecipe(aRecipe);
                            } else {//Should never happen!
                                Utils.logDebug("DetailPresenter.getRecipe... what??");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable aThrowable) throws Exception {
                            Utils.logDebug("DetailPresenter.getRecipe: " + aThrowable.getLocalizedMessage());
                            mViewDetail.problemFindingData();
                        }
                    });
            mDisposables.add(disposable);
        }
    }

    @Override
    public void onStepClick(int aRecipeId, int aStepId) {
        mActivityDetail.onStepClick(aRecipeId,aStepId);
    }
}
