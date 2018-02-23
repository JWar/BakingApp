package com.portfolio.udacity.android.bakingapp.ui.list.holders;

import android.view.View;
import android.widget.TextView;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Step;

/**
 * Created by JonGaming on 23/02/2018.
 *
 */

public class StepHolder extends AbstractHolder {
    private View mView;
    private TextView mDescriptionShortTV;
    public StepHolder(View aView) {
        super(aView);
        mView=aView;
        mDescriptionShortTV = aView.findViewById(R.id.list_item_step_description_short_tv);
    }

    public String bindData(Step aStep) {
        mDescriptionShortTV.setText(aStep.mShortDescription);
        return aStep.mId+"";
    }

    @Override
    public void setListener(View.OnClickListener aListener) {
        mView.setOnClickListener(aListener);
    }
}
