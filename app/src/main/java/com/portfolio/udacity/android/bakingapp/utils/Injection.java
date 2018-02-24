package com.portfolio.udacity.android.bakingapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.data.source.remote.BakingAppApi;
import com.portfolio.udacity.android.bakingapp.data.source.remote.RecipeRemoteDataSource;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.SchedulerProvider;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JonGaming on 22/02/2018.
 * Usually Id put this in Mock/Prod...
 */

public class Injection {
    public static RecipeRepository provideRecipeRepository(RecipeRemoteDataSource aRecipeRemoteDataSource) {
        return RecipeRepository.getInstance(aRecipeRemoteDataSource);
    }
    public static RecipeRemoteDataSource provideRecipeRemoteDataSource(BakingAppApi aBakingAppApi) {
        return RecipeRemoteDataSource.getInstance(aBakingAppApi);
    }
    public static BaseSchedulerProvider provideSchedulerProvider() throws Exception {
        return SchedulerProvider.getInstance();
    }
    public static BakingAppApi provideBakingAppApi() throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BakingAppApi.END_POINT)
//                .addConverterFactory(new ToStringConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build();
        return retrofit.create(BakingAppApi.class);
    }
    private static Gson provideGson() {
        return new GsonBuilder().registerTypeAdapter(Double.class,
                new DoubleTypeAdapter()).create();
    }
}
