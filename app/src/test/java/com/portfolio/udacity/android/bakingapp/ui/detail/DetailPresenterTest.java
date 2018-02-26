package com.portfolio.udacity.android.bakingapp.ui.detail;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by JonGaming on 26/02/2018.
 * ...
 */

public class DetailPresenterTest {
    @Mock
    private RecipeRepository mRecipeRepository;

    @Mock
    private DetailContract.ViewDetail mViewDetail;

    @Mock
    private DetailContract.ActivityDetail mActivityDetail;

    private DetailPresenter mDetailPresenter;

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
    }
    @Test
    public void setPresenterToView() {
        mDetailPresenter = new DetailPresenter(mRecipeRepository,
                mSchedulerProvider,
                mViewDetail,
                mActivityDetail);
        verify(mViewDetail).setPresenter(mDetailPresenter);
    }
    @Test
    public void getRecipe() {
        mDetailPresenter = new DetailPresenter(mRecipeRepository,
                mSchedulerProvider,
                mViewDetail,
                mActivityDetail);
        setRecipeAvailable(mId);
        mDetailPresenter.getRecipe(mId);
        verify(mRecipeRepository).getRecipe(mId);
        verify(mViewDetail).setRecipe(mRecipe);
    }

    private void setRecipeAvailable(int aId) {
        when(mRecipeRepository.getRecipe(aId)).thenReturn(mRecipe);
    }
}
