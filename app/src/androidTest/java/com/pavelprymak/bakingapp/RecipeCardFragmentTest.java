package com.pavelprymak.bakingapp;

import android.widget.TextView;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_POSITION;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_TITLE;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_LAST_ITEM_POSITION;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_LAST_ITEM_TITLE;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.isTablet;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeCardFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void checkRecipeListFirstAndLastItemText() {
        //recipeRecycler first item check
        onView(withText(RECIPE_CARD_FIRST_ITEM_TITLE)).check(matches(isDisplayed()));

        //Scroll to last position and check
        onView(ViewMatchers.withId(R.id.recipeRecycler))
                .perform(RecyclerViewActions.scrollToPosition(RECIPE_CARD_LAST_ITEM_POSITION));
        onView(withText(RECIPE_CARD_LAST_ITEM_TITLE)).check(matches(isDisplayed()));
    }


    @Test
    public void recipeCardFragment_onRecipeRecyclerItemClick() {
        onFirstRecipeCardElementClick();
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(RECIPE_CARD_FIRST_ITEM_TITLE)));
        if (isTablet()) {
            onView(withId(R.id.playerView)).check(matches(isDisplayed()));
        }
    }

    private void onFirstRecipeCardElementClick() {
        onView(withId(R.id.recipeRecycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_CARD_FIRST_ITEM_POSITION, click()));
    }

}

