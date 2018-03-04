package com.portfolio.udacity.android.bakingapp.data.repository;

import android.support.annotation.NonNull;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.source.remote.RecipeRemoteDataSource;
import com.portfolio.udacity.android.bakingapp.utils.Utils;

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
    //Checks to see if mRecipes is null
    public boolean isRecipesNull() {
        return mRecipes==null;
    }
    public void setRecipes(List<Recipe> aRecipes) {
        mRecipes=aRecipes;
    }
    public Observable<List<Recipe>> getRecipes() {
        if (mRecipes!=null) {
            return Observable.just(mRecipes);
        } else {
            return mRecipeRemoteDataSource.getRecipes();
        }
    }
    public Recipe getRecipe(int aRecipeId) {
        if (mRecipes==null) {
            //Should never be if DetailPresenter does its job. Must be a better way though.
            return null;
        } else {
            for (Recipe recipe : mRecipes) {
                if (recipe.mId == aRecipeId) {
                    return recipe;
                }
            }
            return null;
        }
    }
}
