package com.portfolio.udacity.android.bakingapp.ui.stepdetail;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.model.Step;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by JonGaming on 26/02/2018.
 * ...
 */

public class StepDetailPresenterTest {
    @Mock
    private RecipeRepository mRecipeRepository;

    @Mock
    private StepDetailContract.ViewStep mViewStep;

    @Mock
    private StepDetailContract.ActivityStep mActivityStep;

    private StepDetailPresenter mStepDetailPresenter;

    private BaseSchedulerProvider mSchedulerProvider;

    private Recipe mRecipe;

    private int mId;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();
        mId=0;
        mRecipe = new Recipe();
        mRecipe.mId=mId;
        mRecipe.mName="Mock";
        mRecipe.mSteps = new ArrayList<>();
        Step step = new Step();
        step.mId=mId;
        step.mShortDescription="Mock step";
        mRecipe.mSteps.add(step);
    }
    @Test
    public void setPresenterToView() {
        mStepDetailPresenter = new StepDetailPresenter(mRecipeRepository,
                mSchedulerProvider,
                mViewStep,
                mActivityStep);
        verify(mViewStep).setPresenter(mStepDetailPresenter);
    }
    @Test
    public void getRecipe() {
        mStepDetailPresenter = new StepDetailPresenter(mRecipeRepository,
                mSchedulerProvider,
                mViewStep,
                mActivityStep);
        setRecipeAvailable(mId);
        mStepDetailPresenter.getRecipe(mId);
        verify(mRecipeRepository).getRecipe(mId);
        verify(mViewStep).setRecipe(mRecipe);
    }

    private void setRecipeAvailable(int aId) {
        when(mRecipeRepository.getRecipe(aId)).thenReturn(mRecipe);
    }
}
