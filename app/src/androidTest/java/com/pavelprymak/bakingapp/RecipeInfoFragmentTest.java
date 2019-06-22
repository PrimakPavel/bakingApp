package com.pavelprymak.bakingapp;

import android.widget.TextView;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_POSITION;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_STEP_FIRST_POSITION;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_STEP_FIRST_TITLE;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_STEP_LAST_POSITION;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_STEP_LAST_TITLE;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_TITLE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeInfoFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void moveToRecipeInfoFragment() {
        onView(withId(R.id.recipeRecycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_CARD_FIRST_ITEM_POSITION, click()));
    }

    @Test
    public void checkStepsListFirstAndSecondItemText() {
        //recipeInfoRecycler first item check
        onView(withId(R.id.recipeInfoRecycler))
                .check(matches(hasDescendant(withText(RECIPE_CARD_FIRST_ITEM_STEP_FIRST_TITLE))));

        //Scroll to last position and check
        onView(ViewMatchers.withId(R.id.recipeInfoRecycler))
                .perform(RecyclerViewActions.scrollToPosition(RECIPE_CARD_FIRST_ITEM_STEP_LAST_POSITION));
        onView(withId(R.id.recipeInfoRecycler))
                .check(matches(hasDescendant(withText(RECIPE_CARD_FIRST_ITEM_STEP_LAST_TITLE))));
    }

    @Test
    public void onRecipeInfoFirstRecyclerItemClick() {
        onFirstStepItemClick();
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(RECIPE_CARD_FIRST_ITEM_TITLE)));
        onView(withId(R.id.playerView)).check(matches(isDisplayed()));
    }

    @Test
    public void onRecipeInfoLastRecyclerItemClick() {
        onLastStepItemClick();
        onView(withId(R.id.playerView)).check(matches(isDisplayed()));
        onView(withId(R.id.descriptionShortTv)).check(matches(withText(RECIPE_CARD_FIRST_ITEM_STEP_LAST_TITLE)));
    }

    private void onFirstStepItemClick() {
        onView(withId(R.id.recipeInfoRecycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_CARD_FIRST_ITEM_STEP_FIRST_POSITION, click()));
    }

    private void onLastStepItemClick() {
        onView(withId(R.id.recipeInfoRecycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_CARD_FIRST_ITEM_STEP_LAST_POSITION, click()));
    }

}


