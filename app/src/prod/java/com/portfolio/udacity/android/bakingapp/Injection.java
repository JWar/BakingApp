package com.portfolio.udacity.android.bakingapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.data.source.remote.BakingAppApi;
import com.portfolio.udacity.android.bakingapp.data.source.remote.ProdRecipeRemoteDataSource;
import com.portfolio.udacity.android.bakingapp.data.source.remote.RecipeRemoteDataSource;
import com.portfolio.udacity.android.bakingapp.utils.DoubleTypeAdapter;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.SchedulerProvider;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JonGaming on 27/02/2018.
 *
 */

public class Injection {
    public static RecipeRepository provideRecipeRepository(RecipeRemoteDataSource aRecipeRemoteDataSource) {
        return RecipeRepository.getInstance(aRecipeRemoteDataSource);
    }
    public static RecipeRemoteDataSource provideRecipeRemoteDataSource(BakingAppApi aBakingAppApi) {
        return ProdRecipeRemoteDataSource.getInstance(aBakingAppApi);
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
    //Sigh horrible way of ensuring Testing strings are handled properly based upon build variant.
    public static String getIngredientsTestString() {
        return "Ingredients:\n\n2.0 cup - Graham cracker crumbs\n6.0 tblsp - Unsalted butter, melted\n0.5 cup - Granulated sugar\n1.5 tsp - Salt\n5.0 tblsp - Vanilla\n1.0 k - Nutella or other chocolate-hazelnut spread\n500.0 g - Mascapone cheese(room temperature)\n1.0 cup - Heavy cream(cold)\n4.0 oz - Cream cheese(softened)\n";
    }
}
