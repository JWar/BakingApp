package com.portfolio.udacity.android.bakingapp.data.source.remote;

import android.support.annotation.NonNull;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import java.util.List;
import io.reactivex.Observable;

/**
 * Created by JonGaming on 23/02/2018.
 * For accessing remote data.
 */

public class RecipeRemoteDataSource {
    private static RecipeRemoteDataSource sInstance=null;

    private BakingAppApi mBakingAppApi;

    public static synchronized RecipeRemoteDataSource getInstance(@NonNull BakingAppApi aBakingAppApi) {
        if (sInstance==null) {
            sInstance=new RecipeRemoteDataSource(aBakingAppApi);
        }
        return sInstance;
    }
    private RecipeRemoteDataSource(@NonNull BakingAppApi aBakingAppApi) {
        mBakingAppApi=aBakingAppApi;
    }
    public Observable<List<Recipe>> getRecipes() {
        return mBakingAppApi.getRecipes();
    }
}
