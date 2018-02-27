package com.portfolio.udacity.android.bakingapp.data.source.remote;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by JonGaming on 27/02/2018.
 * Handles contract for remote data source. Basically allowing Mock/Prod switches
 */

public interface RecipeRemoteDataSource {
    Observable<List<Recipe>> getRecipes();
}
