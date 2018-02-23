package com.portfolio.udacity.android.bakingapp.ui.list.holders;

import android.view.View;
import android.widget.TextView;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;

/**
 * Created by JonGaming on 23/02/2018.
 *
 */

public class RecipeHolder extends AbstractHolder {
    private View mView;
    private TextView mNameTV;
    public RecipeHolder(View aView) {
        super(aView);
        mView=aView;
        mNameTV = mView.findViewById(R.id.list_item_recipe_name_tv);
    }

    public String bindData(Recipe aRecipe) {
        mNameTV.setText(aRecipe.mName);
        return aRecipe.mId+"";
    }

    @Override
    public void setListener(View.OnClickListener aListener) {
        mView.setOnClickListener(aListener);
    }
}
