package com.portfolio.udacity.android.bakingapp.ui.detail.step;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.ui.BasePresenter;
import com.portfolio.udacity.android.bakingapp.ui.BaseView;

/**
 * Created by JonGaming on 24/02/2018.
 * This will handle getting video etc...
 */

interface StepContract {
    interface ViewStep extends BaseView<PresenterStep> {
        void setRecipe(Recipe aRecipe);
    }
    interface PresenterStep extends BasePresenter {
        void getRecipe(int aRecipeId);
        //TODO:180224_This will have to handle upstep and downstep. Also what happens on back??
    }
}
