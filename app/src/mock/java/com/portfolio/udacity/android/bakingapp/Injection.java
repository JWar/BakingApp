package com.portfolio.udacity.android.bakingapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.data.source.remote.BakingAppApi;
import com.portfolio.udacity.android.bakingapp.data.source.remote.MockRecipeRemoteDataSource;
import com.portfolio.udacity.android.bakingapp.data.source.remote.RecipeRemoteDataSource;
import com.portfolio.udacity.android.bakingapp.utils.DoubleTypeAdapter;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.ImmediateSchedulerProvider;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JonGaming on 27/02/2018.
 * Will use mock data!
 */

public class Injection {
    public static RecipeRepository provideRecipeRepository(RecipeRemoteDataSource aRecipeRemoteDataSource) {
        return RecipeRepository.getInstance(aRecipeRemoteDataSource);
    }
    public static RecipeRemoteDataSource provideRecipeRemoteDataSource(BakingAppApi aBakingAppApi) {
        return MockRecipeRemoteDataSource.getInstance(aBakingAppApi);
    }
    public static BaseSchedulerProvider provideSchedulerProvider() throws Exception {
        return ImmediateSchedulerProvider.getInstance();
    }
    //Sigh will need to do better than this. No point init Retrofit if it is Mock...
    //But havent the time atm to rig something proper up.
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
