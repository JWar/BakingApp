package com.portfolio.udacity.android.bakingapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JonGaming on 23/02/2018.
 *
 */

public class Step extends entity {
    @SerializedName("shortDescription")
    public String mShortDescription;
    @SerializedName("description")
    public String mDescription;
    @SerializedName("videoURL")
    public String mVideoURL;
    @SerializedName("thumbnailURL")
    public String mThumbnailURL;
    public Step() {}
}
