package com.portfolio.udacity.android.bakingapp.data.source.remote;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by JonGaming on 22/02/2018.
 * Retrofit...
 * Don't need anything more than this?
 */

public interface BakingAppApi {

    String END_POINT = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @GET("baking.json")
    Observable<List<Recipe>> getRecipes();

    @GET
    @Streaming
    Call<ResponseBody> getVideo(@Url String url);



}
