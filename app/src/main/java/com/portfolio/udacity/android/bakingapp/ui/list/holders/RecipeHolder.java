package com.portfolio.udacity.android.bakingapp.ui.list.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.squareup.picasso.Picasso;

/**
 * Created by JonGaming on 23/02/2018.
 *
 */

public class RecipeHolder extends AbstractHolder {
    private View mView;
    private TextView mNameTV;
    private ImageView mImageIV;
    public RecipeHolder(View aView) {
        super(aView);
        mView=aView;
        mNameTV = mView.findViewById(R.id.list_item_recipe_name_tv);
        mImageIV = mView.findViewById(R.id.list_item_recipe_iv);
    }

    public String bindData(Recipe aRecipe) {
        mNameTV.setText(aRecipe.mName);
        if (aRecipe.mImage!=null&&!aRecipe.mImage.equals("")) {
            Picasso.with(mView.getContext())
                    .load(aRecipe.mImage)
                    .placeholder(R.drawable.ic_image_black_48px)
                    .error(R.drawable.ic_error_black_48px)
                    .resize(mView.getContext().getResources().getDimensionPixelSize(R.dimen.thumbnail_size),
                            mView.getContext().getResources().getDimensionPixelSize(R.dimen.thumbnail_size))
                    .into(mImageIV);
        }
        return aRecipe.mId+"";
    }

    @Override
    public void setListener(View.OnClickListener aListener) {
        mView.setOnClickListener(aListener);
    }
}
