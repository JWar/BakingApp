package com.portfolio.udacity.android.bakingapp.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by JonGaming on 03/03/2018.
 * Handles Widget updates etc...
 */

public class BakingAppWidgetService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.portfolio.udacity.android.bakingapp.action.update_widget";
    private static final String WIDGET_RECIPE_DETAILS= "widgetRecipeDetails";
    public static final String WIDGET_PREV_RECIPE_ID = "prevRecipeId";

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void startActionUpdateWidget(Context context, String[] recipe) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(WIDGET_RECIPE_DETAILS, recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                final String[] extra = intent.getStringArrayExtra(WIDGET_RECIPE_DETAILS);
                handleActionUpdateWidget(extra);
            }
        }
    }

    private void handleActionUpdateWidget(String[] arg) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));

        // Update widgets
        BakingAppWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds, arg);
    }
}
