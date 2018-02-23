package com.portfolio.udacity.android.bakingapp.ui.list.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JonGaming on 23/02/2018.
 * Base class for holders
 */

public abstract class AbstractHolder extends RecyclerView.ViewHolder {
    public AbstractHolder(View itemView) {
        super(itemView);
    }
    public abstract void setListener(View.OnClickListener aListener);
}