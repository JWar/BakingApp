package com.portfolio.udacity.android.bakingapp.ui.recipe;

import android.support.annotation.NonNull;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.utils.EspressoIdlingResource;
import com.portfolio.udacity.android.bakingapp.utils.Utils;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by JonGaming on 24/02/2018.
 * Just gets all recipes and handles list click on recipe
 */

public class RecipePresenter implements RecipeContract.PresenterRecipe {
    @NonNull
    private final RecipeRepository mRecipeRepository;
    @NonNull
    private final BaseSchedulerProvider mBaseSchedulerProvider;
    @NonNull
    private final RecipeContract.ViewRecipe mViewRecipe;
    @NonNull
    private final RecipeContract.ActivityRecipe mActivityRecipe;

    private CompositeDisposable mDisposables;

    RecipePresenter(@NonNull RecipeRepository aRecipeRepository,
                           @NonNull BaseSchedulerProvider aBaseSchedulerProvider,
                           @NonNull RecipeContract.ViewRecipe aViewRecipe,
                           @NonNull RecipeContract.ActivityRecipe aActivityRecipe) {
        mRecipeRepository = aRecipeRepository;
        mBaseSchedulerProvider = aBaseSchedulerProvider;
        mViewRecipe = aViewRecipe;
        mViewRecipe.setPresenter(this);
        mActivityRecipe = aActivityRecipe;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }

    @Override
    public void getRecipes() {
        EspressoIdlingResource.increment();
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
                            mViewRecipe.setRecipes(aRecipes);
                        } else {
                            mViewRecipe.problemFindingData();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable aThrowable) throws Exception {
                        Utils.logDebug("RecipePresenter.getRecipes: " + aThrowable.getLocalizedMessage());
                        mViewRecipe.problemFindingData();
                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void onRecipeClick(int aRecipeId) {
        mActivityRecipe.onRecipeClick(aRecipeId);
    }
}