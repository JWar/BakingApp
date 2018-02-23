package com.portfolio.udacity.android.bakingapp.data.repository;

import android.support.annotation.NonNull;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.source.remote.BakingAppApi;
import com.portfolio.udacity.android.bakingapp.data.source.remote.RecipeRemoteDataSource;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by JonGaming on 23/02/2018.
 * Layer for dealing with Recipe
 */

public class RecipeRepository {
    private static RecipeRepository sInstance=null;

    private RecipeRemoteDataSource mRecipeRemoteDataSource;

    private List<Recipe> mRecipes;

    public static synchronized RecipeRepository getInstance(@NonNull RecipeRemoteDataSource aRecipeRemoteDataSource) {
        if (sInstance==null) {
            sInstance=new RecipeRepository(aRecipeRemoteDataSource);
        }
        return sInstance;
    }
    private RecipeRepository(@NonNull RecipeRemoteDataSource aRecipeRemoteDataSource) {
        mRecipeRemoteDataSource=aRecipeRemoteDataSource;
    }

    public void setRecipes(List<Recipe> aRecipes) {
        mRecipes=aRecipes;
    }
    public Observable<List<Recipe>> getRecipes() {
        if (mRecipes!=null){
            return Observable.just(mRecipes);
        } else {
            return mRecipeRemoteDataSource.getRecipes();
        }
    }
}
