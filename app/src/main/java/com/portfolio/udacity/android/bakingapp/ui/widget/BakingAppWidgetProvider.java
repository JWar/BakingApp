package com.portfolio.udacity.android.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.portfolio.udacity.android.bakingapp.R;

/**
 * Created by JonGaming on 03/03/2018.
 *
 */

public class BakingAppWidgetProvider extends AppWidgetProvider {
    //Hmm opted to send Recipe as a string array with just the info needed (id, name, ingredients as string).
    //This is in part laziness and in part because the string for the ingredients display has already
    //been made, seems silly to need to reuse it.
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String[] recipe) {

        // Create an Intent to launch Activity when clicked using Recipe ID
        Intent intent = new Intent(context, WidgetActivity.class);
        intent.putExtra(BakingAppWidgetService.WIDGET_PREV_RECIPE_ID, Integer.parseInt(recipe[0]));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        //Update view
        if (!recipe[0].equals("-1")) { // Check if a recipe ID is available, in which case update UI
            views.setTextViewText(R.id.widget_header, recipe[1]);
            views.setTextViewText(R.id.widget_ingredients_tv, recipe[2]);
        } else { // Update UI with default text if no recipe ID is available
            views.setTextViewText(R.id.widget_header, "");
            views.setTextViewText(R.id.widget_ingredients_tv, context.getString(R.string.widget_default_text));
        }

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_ingredients_tv, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String [] recipe = new String[3];
        recipe[0] = "-1";
        recipe[1] = "";
        recipe[2] = context.getString(R.string.widget_default_text);
        updateBakingWidgets(context, appWidgetManager, appWidgetIds, recipe);
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                           String[] recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }


    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
