package com.portfolio.udacity.android.bakingapp.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Ingredient;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.model.Step;
import com.portfolio.udacity.android.bakingapp.ui.recipe.RecipeActivity;
import com.portfolio.udacity.android.bakingapp.ui.list.holders.AbstractHolder;
import com.portfolio.udacity.android.bakingapp.ui.list.holders.RecipeHolder;
import com.portfolio.udacity.android.bakingapp.ui.list.holders.StepHolder;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_ID;

/**
 * Created by JonGaming on 23/02/2018.
 * Love to know of a better way of doing this than having to specify each indivdual holder type...
 * Basically want to avoid casting...
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<AbstractHolder> {
    private final ListHandlerCallback mListener;
    //Specifies what sort of list wanted (client, user, activity etc...). Will use R.layout.fragment_list_item_...
    private int listItemType;

    private List<Recipe> mRecipes;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    public RecyclerViewAdapter(ListHandlerCallback listener, int type) {
        mListener = listener;
        listItemType = type;
        setHasStableIds(false);
    }

    //This is where the holder type is specified. Must check param list type and change accordingly.
    //User Type to User Holder etc...
    @Override
    public AbstractHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(listItemType, parent, false);
        //Control which viewholder to call based upon list (item) type.
        switch(listItemType) {
            case R.layout.list_item_recipe:
                return new RecipeHolder(view);
            case R.layout.list_item_step:
                return new StepHolder(view);
            default:
                RecipeActivity.logDebug("Error in ListRecyclerAdapter.onCreateViewHolder: listItemType unrecognised.");
                return null;
        }
    }
    //This is where the data is put into the holder (in bindData).
    @Override
    public void onBindViewHolder(final AbstractHolder holder, final int position) {
        try {
            String dId=null;
            if (holder instanceof RecipeHolder) {
                RecipeHolder recipeHolder = (RecipeHolder) holder;
                dId = recipeHolder.bindData(mRecipes.get(position));
            } else if (holder instanceof StepHolder) {
                StepHolder stepHolder = (StepHolder) holder;
                dId = stepHolder.bindData(mSteps.get(position));
            }
            final String dataId = dId;
            holder.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//This sets up what on click will be. Draws from parent Activity.
                    try {
                        if (null != mListener) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an item has been selected.
                            mListener.onListClick(holder.getAdapterPosition(), dataId);
                        }
                    } catch (Exception e) {
                        RecipeActivity.logDebug("Error in onListListener: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            RecipeActivity.logDebug("Error in ListRecyclerAdapter.onBindViewHolder: " + e.getMessage());
        }
    }
    @Override
    public long getItemId(int aPos) {
        //Not needed as no stable ids?
        return NO_ID;
    }
    @Override
    public int getItemCount() {
        try {
            if (mRecipes!= null) {
                return mRecipes.size();
            } else if (mIngredients!= null) {
                return mIngredients.size();
            } else if (mSteps!= null) {
                return mSteps.size();
            } else {
                return -1;
            }
        } catch (Exception e) {
            RecipeActivity.logDebug("Error in ListRecyclerAdapter.getItemCount: " + e.getMessage());
            return -1;
        }
    }
    public void swapRecipes(List<Recipe> aList) {
        mRecipes = aList;
        notifyDataSetChanged();
    }
    public void swapIngredients(List<Ingredient> aList) {
        mIngredients = aList;
        notifyDataSetChanged();
    }
    public void swapSteps(List<Step> aList) {
        mSteps = aList;
        notifyDataSetChanged();
    }
}