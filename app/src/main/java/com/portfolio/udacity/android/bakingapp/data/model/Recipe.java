package com.portfolio.udacity.android.bakingapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JonGaming on 23/02/2018.
 * Recipe POJO
 * Lazy, no getters and setters cos cba.
 */

public class Recipe extends entity {
    @SerializedName("name")
    public String mName;
    @SerializedName("ingredients")
    public List<Ingredient> mIngredients;
    @SerializedName("steps")
    public List<Step> mSteps;
    @SerializedName("servings")
    public int mServing;
    @SerializedName("image")
    public String mImage;
    public Recipe() {
        mIngredients=new ArrayList<>();
        mSteps=new ArrayList<>();
    }
}
