package com.portfolio.udacity.android.bakingapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.portfolio.udacity.android.bakingapp.Injection;
import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.ui.recipe.RecipeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
    private static final String STEP_DETAIL = "Step 1:\nRecipe Introduction";
    private static final String STEP_DETAIL_INSTRUCTION = "Recipe Introduction";
    private static final String INGREDIENTS = Injection.getIngredientsTestString();

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule =
            new ActivityTestRule<DetailActivity>(DetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, DetailActivity.class);
                    result.putExtra("recipeId", 1);
                    return result;
                }
            };

    private IdlingResource mIdlingResource;

    @Before
    public void setUp() throws Exception {
        mIdlingResource= mActivityTestRule.getActivity().getCountingIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }
    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void testDetailStepRVData() {
        onView(withId(R.id.fragment_detail_step_rv))
                .perform(RecyclerViewActions
                        .scrollToPosition(0));

        //Check if Step description is displayed.
        onView(withText(STEP_DETAIL))
                .check(matches(isDisplayed()));
    }
    @Test
    public void testIngredientsTV() {
        onView(withId(R.id.fragment_detail_recipe_ingredients))
                .check(matches(withText(INGREDIENTS)));
    }
    @Test
    public void testStepDetailClickAndData() {
        onView(withId(R.id.fragment_detail_step_rv))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0,click()));
        //Check Step detail instruction
        onView(withId(R.id.fragment_step_detail_instruction_tv))
                .check(matches(withText(STEP_DETAIL_INSTRUCTION)));

    }
    @Test
    public void testStepDetailExoplayerErrorImage() {
        onView(withId(R.id.fragment_detail_step_rv))
                .perform(RecyclerViewActions
                        .scrollToPosition(1));
        onView(withId(R.id.fragment_detail_step_rv))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(1,click()));
        //Check if error image is showing, since dummy data has no url.
        onView(withId(R.id.fragment_step_detail_player_error_iv))
                .check(matches(isDisplayed()));
    }
}
