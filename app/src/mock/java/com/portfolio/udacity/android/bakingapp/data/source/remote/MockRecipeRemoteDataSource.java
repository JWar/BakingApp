package com.portfolio.udacity.android.bakingapp.data.source.remote;

import android.support.annotation.NonNull;

import com.portfolio.udacity.android.bakingapp.DummyData;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by JonGaming on 23/02/2018.
 * For accessing remote data.
 */

public class MockRecipeRemoteDataSource implements RecipeRemoteDataSource {
    private static MockRecipeRemoteDataSource sInstance=null;

    private BakingAppApi mBakingAppApi;

    public static synchronized MockRecipeRemoteDataSource getInstance(@NonNull BakingAppApi aBakingAppApi) {
        if (sInstance==null) {
            sInstance=new MockRecipeRemoteDataSource(aBakingAppApi);
        }
        return sInstance;
    }
    private MockRecipeRemoteDataSource(@NonNull BakingAppApi aBakingAppApi) {
        mBakingAppApi=aBakingAppApi;
    }
    @Override
    public Observable<List<Recipe>> getRecipes() {
        return Observable.just(DummyData.getRecipes());
    }
}
