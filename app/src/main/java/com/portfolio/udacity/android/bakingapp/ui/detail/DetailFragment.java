package com.portfolio.udacity.android.bakingapp.ui.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;

public class DetailFragment extends Fragment implements DetailContract.ViewDetail {
    public static final String TAG = "detailFragTag";
    private static final String RECIPE_ID = "recipeId";
    private int mRecipeId;

    private DetailContract.PresenterDetail mPresenterDetail;

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
    }

    @Override
    public void problemFindingData() {
        Toast.makeText(getActivity(), getString(R.string.problem_finding_data), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRecipe(Recipe aRecipe) {

    }

    @Override
    public void setPresenter(DetailContract.PresenterDetail aPresenter) {
        mPresenterDetail=aPresenter;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID,mRecipeId);
    }
}
