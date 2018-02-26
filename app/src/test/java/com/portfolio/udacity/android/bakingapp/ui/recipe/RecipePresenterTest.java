package com.portfolio.udacity.android.bakingapp.ui.recipe;

import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by JonGaming on 26/02/2018.
 * Testing presenter/view
 */

public class RecipePresenterTest {
    @Mock
    private RecipeRepository mRecipeRepository;

    @Mock
    private RecipeContract.ViewRecipe mViewRecipe;

    @Mock
    private RecipeContract.ActivityRecipe mActivityRecipe;

    private RecipePresenter mRecipePresenter;

    private BaseSchedulerProvider mSchedulerProvider;

    private List<Recipe> mEntities;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();
        mEntities =  new ArrayList<>();
    }
    @Test
    public void setPresenterToView() {
        mRecipePresenter = new RecipePresenter(mRecipeRepository,
                mSchedulerProvider,
                mViewRecipe,
                mActivityRecipe);
        verify(mViewRecipe).setPresenter(mRecipePresenter);
    }
    @Test
    public void getRecipes() {
        mRecipePresenter = new RecipePresenter(mRecipeRepository,
                mSchedulerProvider,
                mViewRecipe,
                mActivityRecipe);
        setRecipesAvailable();
        mRecipePresenter.getRecipes();
        verify(mRecipeRepository).getRecipes();
        verify(mViewRecipe).setRecipes(mEntities);
    }

    private void setRecipesAvailable() {
        when(mRecipeRepository.getRecipes()).thenReturn(Observable.just(mEntities));
    }
}
