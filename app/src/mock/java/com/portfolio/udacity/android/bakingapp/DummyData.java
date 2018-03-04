package com.portfolio.udacity.android.bakingapp;

import com.portfolio.udacity.android.bakingapp.data.model.Ingredient;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JonGaming on 27/02/2018.
 * Yeh...
 */

public class DummyData {

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.mId=1;
        recipe.mName="Mockolate Pie";
        recipe.mImage="";
        recipe.mServing=1;
        Ingredient ingredient = new Ingredient();
        ingredient.mQuantity=2;
        ingredient.mMeasure="CUP";
        ingredient.mIngredient="Mockolate Paste";
        recipe.mIngredients.add(ingredient);
        Step step = new Step();
        step.mId=0;
        step.mShortDescription="Recipe Introduction";
        step.mDescription="Recipe Introduction";
        step.mVideoURL="";
        step.mThumbnailURL="";
        recipe.mSteps.add(step);
        recipes.add(recipe);
        step = new Step();
        step.mId=1;
        step.mShortDescription="Recipe Step 2";
        step.mDescription="Recipe Step 2";
        step.mVideoURL="";
        step.mThumbnailURL="";
        recipe.mSteps.add(step);
        recipes.add(recipe);
        return recipes;
    }
}
