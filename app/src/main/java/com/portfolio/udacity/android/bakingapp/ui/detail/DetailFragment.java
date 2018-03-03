package com.portfolio.udacity.android.bakingapp.ui.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Ingredient;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.ui.list.ListHandlerCallback;
import com.portfolio.udacity.android.bakingapp.ui.list.RecyclerViewAdapter;
import com.portfolio.udacity.android.bakingapp.utils.Utils;

public class DetailFragment extends Fragment implements DetailContract.ViewDetail {
    public static final String TAG = "detailFragTag";

    private static final String RECIPE_ID = "recipeId";
    private int mRecipeId;

    private static final String STEP_RV = "stepRV";

    private DetailContract.PresenterDetail mPresenterDetail;

    private TextView mIngredientsTV;
    private RecyclerView mStepRV;

    public DetailFragment() {}

    public static DetailFragment newInstance(int aRecipeId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, aRecipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
        } else if (getArguments() != null) {
            mRecipeId = getArguments().getInt(RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIngredientsTV = view.findViewById(R.id.fragment_detail_recipe_ingredients);
        mStepRV = view.findViewById(R.id.fragment_detail_step_rv);
        mStepRV.setFocusable(false);
        mStepRV.setLayoutManager(new LinearLayoutManager(mStepRV.getContext()));
        mStepRV.setAdapter(new RecyclerViewAdapter(new ListHandlerCallback() {
            @Override
            public void onListClick(int aPosition, String aId) {
                mPresenterDetail.onStepClick(mRecipeId,Integer.parseInt(aId));
            }

            @Override
            public void onListTouch(View aView, MotionEvent aMotionEvent) {}
        },R.layout.list_item_step));
    }

    @Override
    public void problemFindingData() {
        Toast.makeText(getActivity(), getString(R.string.problem_finding_data), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRecipe(Recipe aRecipe) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String toDisplay = getString(R.string.ingredients) + "\n\n";
            stringBuilder.append(toDisplay);
            for (Ingredient ingredient : aRecipe.mIngredients) {
                toDisplay = ingredient.mQuantity + " ";
                stringBuilder.append(toDisplay);
                toDisplay = ingredient.mMeasure.toLowerCase() + " - ";
                stringBuilder.append(toDisplay);
                toDisplay = Utils.stringToFirstCapital(ingredient.mIngredient) + "\n";
                stringBuilder.append(toDisplay);
            }
            mIngredientsTV.setText(stringBuilder.toString());
            ((RecyclerViewAdapter) mStepRV.getAdapter()).swapSteps(aRecipe.mSteps);
        } catch (Exception e) {
            Utils.logDebug("DetailFragment.setRecipe: "+e.getLocalizedMessage());
            Toast.makeText(getActivity(), getString(R.string.problem_setting_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(DetailContract.PresenterDetail aPresenter) {
        mPresenterDetail=aPresenter;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null) {
            mStepRV.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(STEP_RV));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID,mRecipeId);
        outState.putParcelable(STEP_RV,mStepRV.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenterDetail.getRecipe(mRecipeId);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenterDetail.unSubscribe();
    }

}
