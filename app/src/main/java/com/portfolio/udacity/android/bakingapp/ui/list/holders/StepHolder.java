package com.portfolio.udacity.android.bakingapp.ui.list.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Step;
import com.squareup.picasso.Picasso;

/**
 * Created by JonGaming on 23/02/2018.
 *
 */

public class StepHolder extends AbstractHolder {
    private View mView;
    private TextView mDescriptionShortTV;
    private ImageView mThumbnailIV;
    public StepHolder(View aView) {
        super(aView);
        mView=aView;
        mDescriptionShortTV = aView.findViewById(R.id.list_item_step_description_short_tv);
        mThumbnailIV = aView.findViewById(R.id.list_item_step_thumbnail_iv);
    }

    public String bindData(Step aStep) {
        String toDisplay = mView.getContext().getString(R.string.step) + " "
                + (aStep.mId+1)
                + ":\n"
                + aStep.mShortDescription;
        if (aStep.mThumbnailURL!=null&&!aStep.mThumbnailURL.equals("")) {
            Picasso.with(mView.getContext())
                    .load(aStep.mThumbnailURL)
                    .placeholder(R.drawable.ic_image_black_48px)
                    .error(R.drawable.ic_error_black_48px)
                    .resize(mView.getContext().getResources().getDimensionPixelSize(R.dimen.thumbnail_size),
                            mView.getContext().getResources().getDimensionPixelSize(R.dimen.thumbnail_size))
                    .into(mThumbnailIV);
        }
        mDescriptionShortTV.setText(toDisplay);
        return aStep.mId+"";
    }

    @Override
    public void setListener(View.OnClickListener aListener) {
        mView.setOnClickListener(aListener);
    }
}
