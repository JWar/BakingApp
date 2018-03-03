package com.portfolio.udacity.android.bakingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.portfolio.udacity.android.bakingapp.Injection;
import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Ingredient;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.repository.RecipeRepository;
import com.portfolio.udacity.android.bakingapp.ui.recipe.RecipeActivity;
import com.portfolio.udacity.android.bakingapp.utils.Utils;
import com.portfolio.udacity.android.bakingapp.utils.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by JonGaming on 03/03/2018.
 * Handles Widget View
 */

public class WidgetActivity extends AppCompatActivity {

    private RadioButton mRadioButton;
    private RadioButton[] mRadioButtons;
    private RadioGroup mRadioGroupRecipeOptions;

    private int mAppWidgetId;
    private RadioGroup.LayoutParams mLayoutParams;
    private RecipeRepository mRecipeRepository;
    private List<Recipe> mRecipes;

    int mPrevRecipeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget);
        try {
            mRecipeRepository = RecipeRepository.getInstance(
                    Injection.provideRecipeRemoteDataSource(Injection.provideBakingAppApi()));
//            BaseSchedulerProvider mBaseSchedulerProvider = Injection.provideSchedulerProvider();

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
            }

            findViewById(R.id.activity_widget_choose_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processRecipeClick();
                }
            });
            //Only need to run widget if recipes isnt null. If it is then run app from start.
            if (mRecipeRepository.isRecipesNull()) {
                Toast.makeText(this, getString(R.string.widget_launching_app), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, RecipeActivity.class));
                finish();
            } else {
                mRecipeRepository.getRecipes()
                        //Dont need observeOn etc... as recipe list in repo.
//                        .observeOn(mBaseSchedulerProvider.ui())
//                        .subscribeOn(mBaseSchedulerProvider.io())
                        .subscribe(new Consumer<List<Recipe>>() {
                            @Override
                            public void accept(List<Recipe> aRecipes) throws Exception {
                                mRecipes=aRecipes;
                                // Retrieve intent extras
                                processIntentExtras();

                                // Populate the Radio Options
                                displayRecipeOptions();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable aThrowable) throws Exception {
                                Utils.logDebug("Error in WidgetActivity.onCreate.getRecipes: "+aThrowable.getLocalizedMessage());
                            }
                        });
            }
        } catch (Exception e) {
            Utils.logDebug("Error in WidgetActivity.onCreate: "+e.getLocalizedMessage());
        }
    }

    //Displays options in Radio Group by going through list of available recipes
    public void displayRecipeOptions() {
        mRadioGroupRecipeOptions = findViewById(R.id.activity_widget_radiogroup);
        mLayoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mRadioButtons = new RadioButton[mRecipes.size()];

        int i = 0;
        for (Recipe recipe : mRecipes) {
            mRadioButtons[i] = new RadioButton(this);
            mRadioGroupRecipeOptions.addView(mRadioButtons[i]);
            mRadioButtons[i].setText(recipe.mName);
            mRadioButtons[i].setTag(recipe.mId);
            mLayoutParams.setMargins(20, 20, 20, 20);
            mRadioButtons[i].setLayoutParams(mLayoutParams);
            mRadioButtons[i].setPadding(40, 0, 0, 0);

            if (mPrevRecipeId != -1) {
                if (mPrevRecipeId == recipe.mId) {
                    mRadioButtons[i].setChecked(true);
                }
            }
        }
    }

    //OnClick. Processes selected Recipe and sends to the widget.
    public void processRecipeClick() {
        int selectId = mRadioGroupRecipeOptions.getCheckedRadioButtonId();
        mRadioButton = findViewById(selectId);

        if (mRadioButton != null) {
            int recipeId = (int)mRadioButton.getTag();
            String[] widgetRecipe = new String[3];
            widgetRecipe[0] = recipeId+"";

            widgetRecipe[1] = mRadioButton.getText().toString();

            List<Ingredient> ingredientsList = mRecipeRepository.getRecipe(recipeId).mIngredients;
            StringBuilder stringBuilder = new StringBuilder();
            String toDisplay = getString(R.string.ingredients) + "\n\n";
            stringBuilder.append(toDisplay);
            for (Ingredient ingredient : ingredientsList) {
                toDisplay = ingredient.mQuantity + " ";
                stringBuilder.append(toDisplay);
                toDisplay = ingredient.mMeasure.toLowerCase() + " ";
                stringBuilder.append(toDisplay);
                toDisplay = Utils.stringToFirstCapital(ingredient.mIngredient) + "\n";
                stringBuilder.append(toDisplay);
            }
            widgetRecipe[2] = stringBuilder.toString();
            //Lets user know click received/processing
            Toast.makeText(this, getString(R.string.widget_selected_recipe_text), Toast.LENGTH_SHORT).show();

            BakingAppWidgetService.startActionUpdateWidget(this, widgetRecipe);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        } else {
            Utils.logDebug("Error in WidgetActivity.processRecipeClick: radio button is null");
            Toast.makeText(this, getString(R.string.widget_problem_selecting_recipe), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentExtras();
    }

    /**
     * Method to receive Intent Extras passed on from home screen Widget
     */
    private void processIntentExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mPrevRecipeId = intent.getExtras().getInt(BakingAppWidgetService.WIDGET_PREV_RECIPE_ID,-1);
        }
    }
}
