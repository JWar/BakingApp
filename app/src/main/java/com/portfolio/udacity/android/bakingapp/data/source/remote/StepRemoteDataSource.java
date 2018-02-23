package com.portfolio.udacity.android.bakingapp.data.source.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by JonGaming on 23/02/2018.
 *
 */

public class StepRemoteDataSource {
    private static StepRemoteDataSource sInstance=null;
    private BakingAppApi mBakingAppApi;

    public static synchronized StepRemoteDataSource getInstance(@NonNull BakingAppApi aBakingAppApi) {
        if (sInstance==null) {
            sInstance=new StepRemoteDataSource(aBakingAppApi);
        }
        return sInstance;
    }
    private StepRemoteDataSource(@NonNull BakingAppApi aBakingAppApi) {
        mBakingAppApi=aBakingAppApi;
    }

    //Hmm... ExoPlayer handles streaming itself?
    public Uri getVideo(String aUrl) {
        return Uri.parse(aUrl);
//        Call<ResponseBody> call = mBakingAppApi.getVideo(aUrl);
//        try {
//            InputStream is = call.execute().body().byteStream();
//
//        } catch (IOException e) {
//
//        }
    }
}
