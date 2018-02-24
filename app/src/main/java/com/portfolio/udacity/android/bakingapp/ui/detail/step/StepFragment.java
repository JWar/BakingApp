package com.portfolio.udacity.android.bakingapp.ui.detail.step;

import android.content.Context;
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

/**
 * This will contain media player and step instructions. And an up step and down step navigation.
 */
public class StepFragment extends Fragment implements StepContract.ViewStep {
    public static final String TAG = "stepFragTag";
    private static final String RECIPE_ID = "recipeId";
    private static final String STEP_ID = "stepId";

    private int mRecipeId;
    private int mStepId;

    private StepContract.PresenterStep mPresenterStep;

    private StepFragmentListener mListener;

    public StepFragment() {}

    public static StepFragment newInstance(int aRecipeId, int aStepId) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, aRecipeId);
        args.putInt(STEP_ID, aStepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
            mStepId = savedInstanceState.getInt(STEP_ID);
        } else if (getArguments() != null) {
            mRecipeId = getArguments().getInt(RECIPE_ID);
            mStepId = getArguments().getInt(STEP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
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
    public void setPresenter(StepContract.PresenterStep aPresenter) {
        mPresenterStep=aPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepFragmentListener) {
            mListener = (StepFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StepFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface StepFragmentListener {
        void onUpStepClick(int aRecipeId, int aStepId);
        void onDownStepClick(int aRecipeId, int aStepId);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID,mRecipeId);
        outState.putInt(STEP_ID,mStepId);
    }
}
