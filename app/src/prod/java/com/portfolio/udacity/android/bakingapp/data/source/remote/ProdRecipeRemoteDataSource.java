package com.portfolio.udacity.android.bakingapp.data.source.remote;

import android.support.annotation.NonNull;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.utils.Utils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by JonGaming on 27/02/2018.
 * Real one for making real http calls!
 * Hmm not convinced by the cast call in constructor... but it should work.
 */

public class ProdRecipeRemoteDataSource implements RecipeRemoteDataSource {
    private static ProdRecipeRemoteDataSource sInstance = null;

    private BakingAppApi mBakingAppApi;

    public static synchronized ProdRecipeRemoteDataSource getInstance(@NonNull BakingAppApi aBakingAppApi) {
        if (sInstance == null) {
            sInstance = new ProdRecipeRemoteDataSource(aBakingAppApi);
        }
        return sInstance;
    }

    private ProdRecipeRemoteDataSource(@NonNull BakingAppApi aBakingAppApi) {
        mBakingAppApi = aBakingAppApi;
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        return mBakingAppApi.getRecipes();
    }
}
