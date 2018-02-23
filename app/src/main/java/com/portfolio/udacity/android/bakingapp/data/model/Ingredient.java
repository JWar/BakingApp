package com.portfolio.udacity.android.bakingapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JonGaming on 23/02/2018.
 * Very basic sub class for Recipe
 */

public class Ingredient extends entity {
    @SerializedName("quantity")
    public double mQuantity;
    @SerializedName("measure")
    public String mMeasure;
    @SerializedName("ingredient")
    public String mIngredient;
    public Ingredient() {}
}
