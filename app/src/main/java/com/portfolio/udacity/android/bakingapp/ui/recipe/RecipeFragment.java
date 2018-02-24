package com.portfolio.udacity.android.bakingapp.ui.recipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.ui.list.ListHandlerCallback;
import com.portfolio.udacity.android.bakingapp.ui.list.RecyclerViewAdapter;
import com.portfolio.udacity.android.bakingapp.utils.Utils;

import java.util.List;

/**
 * Just shows list of recipes
 */
public class RecipeFragment extends Fragment implements RecipeContract.ViewRecipe {
    public static final String TAG = "recipeFragTag";

    private RecipeContract.PresenterRecipe mPresenterRecipe;

    private RecyclerView mRecyclerView;

    public RecipeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.fragment_recipe_recipes_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),
                getResources().getInteger(R.integer.grid_column_span)));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(new ListHandlerCallback() {
            @Override
            public void onListClick(int aPosition, String aId) {
                mPresenterRecipe.onRecipeClick(Integer.parseInt(aId));
            }

            @Override
            public void onListTouch(View aView, MotionEvent aMotionEvent) {}
        },R.layout.list_item_recipe));
    }

    @Override
    public void problemFindingData() {
        Toast.makeText(getActivity(), getString(R.string.problem_finding_data), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRecipes(List<Recipe> aRecipes) {
        ((RecyclerViewAdapter)mRecyclerView.getAdapter()).swapRecipes(aRecipes);
    }

    @Override
    public void setPresenter(RecipeContract.PresenterRecipe aPresenter) {
        mPresenterRecipe=aPresenter;
    }
}
