package com.portfolio.udacity.android.bakingapp.ui.recipe;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.ui.BasePresenter;
import com.portfolio.udacity.android.bakingapp.ui.BaseView;

import java.util.List;

/**
 * Created by JonGaming on 24/02/2018.
 * yadayada
 */

class RecipeContract {
    interface ViewRecipe extends BaseView<PresenterRecipe> {
        void setRecipes(List<Recipe> aRecipes);
    }
    interface PresenterRecipe extends BasePresenter {
        void getRecipes();
        void onRecipeClick(int aRecipeId);
    }
    interface ActivityRecipe {
        void onRecipeClick(int aRecipeId);
    }
}
