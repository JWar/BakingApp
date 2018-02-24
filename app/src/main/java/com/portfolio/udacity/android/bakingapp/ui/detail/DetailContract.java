package com.portfolio.udacity.android.bakingapp.ui.detail;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.ui.BasePresenter;
import com.portfolio.udacity.android.bakingapp.ui.BaseView;

/**
 * Created by JonGaming on 24/02/2018.
 * Denotes needs of all involved in Detail
 */
interface DetailContract {
    interface ViewDetail extends BaseView<PresenterDetail> {
        void setRecipe(Recipe aRecipe);
    }
    interface PresenterDetail extends BasePresenter {
        void getRecipe(int aRecipeId);
        void onStepClick(int aRecipeId, int aStepId);
    }
    //Handles Fragment/Activity communication via Presenter
    interface ActivityDetail {
        void onStepClick(int aRecipeId, int aStepId);
    }
}
