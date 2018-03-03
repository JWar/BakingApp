package com.portfolio.udacity.android.bakingapp.ui.recipe;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import com.portfolio.udacity.android.bakingapp.R;

import static org.hamcrest.Matchers.anything;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by JonGaming on 27/02/2018.
 * Not sure if this is needed because of MVP but want to see if this works.
 * Trying to see how far onView can traverse the xml tree.
 * TODO: 180227_Look into why prodDebug doesnt work. Think its the Retrofit/Async problem.
 * Basically Mock testing for Mockolate Pie passes, but changing it to Nutella Pie and
 * running it on Prod fails. But there has been one or two occasions when it passed. I suspect
 * this is due to waiting for server response etc... Dont think I need to go into this yet but
 * its something to look out for later.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
    //Forced expected data. If I really wanted to be smart Id do dummy data in model and 'Injection'
    //that into the data via the RecipeRepository... Making androidTest have its own self-contained
    //data/tests
    private static final String MOCKOLATE_PIE = "Mockolate Pie";

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    @Before
    public void setUp() throws Exception {
        IdlingRegistry.getInstance().register(
                mActivityTestRule.getActivity().getCountingIdlingResource());
    }
    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(
                mActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void testRecipeRVData() {
        onView(withId(R.id.fragment_recipe_recipes_rv))
                .perform(RecyclerViewActions
                        .scrollToPosition(0));

        // Check if recipe name as displayed on the specified position of Recipe RecyclerView list matches the given name
        onView(withText(MOCKOLATE_PIE))
                .check(matches(isDisplayed()));
    }
}
