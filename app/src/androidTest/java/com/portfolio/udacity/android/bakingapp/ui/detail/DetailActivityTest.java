package com.portfolio.udacity.android.bakingapp.ui.detail;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.ui.recipe.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by JonGaming on 27/02/2018.
 * Does Detail and StepRV testing...
 */
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {
    //Forced expected data. If I really wanted to be smart Id do dummy data in model and 'Injection'
    //that into the data via the RecipeRepository... Making androidTest have its own self-contained
    //data/tests
    private static final String STEP_DETAIL = "Recipe Introduction";
    private static final String MOCKOLATE_PASTE = "Mockolate Paste";

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule =
            new ActivityTestRule<>(DetailActivity.class);

    @Test
    public void testDetailStepRVData() {
        onView(withId(R.id.fragment_detail_step_rv))
                .perform(RecyclerViewActions
                        .scrollToPosition(0));

        // Check if recipe name as displayed on the specified position of Recipe RecyclerView list matches the given name
        onView(withText(STEP_DETAIL))
                .check(matches(isDisplayed()));
    }
    @Test
    public void testIngredientsTV() {
        onView(withId(R.id.fragment_detail_recipe_ingredients))
                .check(matches(withText(MOCKOLATE_PASTE)));
    }
}
