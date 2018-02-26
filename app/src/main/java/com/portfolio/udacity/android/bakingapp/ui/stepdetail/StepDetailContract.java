package com.portfolio.udacity.android.bakingapp.ui.stepdetail;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.ui.BasePresenter;
import com.portfolio.udacity.android.bakingapp.ui.BaseView;

/**
 * Created by JonGaming on 24/02/2018.
 * This will handle getting video etc...
 */

public interface StepDetailContract {
    interface ViewStep extends BaseView<PresenterStep> {
        void setRecipe(Recipe aRecipe);
    }
    interface PresenterStep extends BasePresenter {
        void getRecipe(int aRecipeId);
        //For navigation between steps.
        void onUpClick(int aRecipeId, int aStepId);
        void onDownClick(int aRecipeId, int aStepId);
        //This is for setting new step when up or down clicking
        void setView(ViewStep aViewStep);
    }
    interface ActivityStep {
        //For navigation between steps. StepsLen is length of steps. Stops index problems
        void onUpClick(int aRecipeId, int aStepId);
        void onDownClick(int aRecipeId, int aStepId, int aStepsLen);
    }
}
