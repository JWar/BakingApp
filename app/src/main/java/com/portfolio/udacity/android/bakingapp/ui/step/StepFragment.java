package com.portfolio.udacity.android.bakingapp.ui.step;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.portfolio.udacity.android.bakingapp.R;

/**
 * This will contain media player and step instructions. And an up step and down step navigation.
 */
public class StepFragment extends Fragment {
    private static final String RECIPE_ID = "recipeId";
    private static final String STEP_ID = "stepId";

    private int mRecipeId;
    private int mStepId;

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
