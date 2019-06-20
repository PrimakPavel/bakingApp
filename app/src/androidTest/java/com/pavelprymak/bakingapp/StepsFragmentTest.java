package com.pavelprymak.bakingapp;

import android.content.pm.ActivityInfo;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_POSITION;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_STEP_FIRST_POSITION;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_STEP_FIRST_TITLE;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.RECIPE_CARD_FIRST_ITEM_STEP_SECOND_TITLE;
import static com.pavelprymak.bakingapp.MainActivityTestHelper.isTablet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class StepsFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void moveToStepsFragment() {
        onFirstRecipeCardElementClick();
        onFirstStepItemClick();
    }

    @Test
    public void rotateDevice(){
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Test
    public void checkTextItemsPlayerViewAndButtons() {
        onView(withId(R.id.playerView)).check(matches(isDisplayed()));
        onView(withId(R.id.descriptionShortTv)).check(matches(withText(RECIPE_CARD_FIRST_ITEM_STEP_FIRST_TITLE)));
        if (isTablet()) {
            onView(withId(R.id.prevStepBtn)).check(matches(not(isDisplayed())));
            onView(withId(R.id.nextStepBtn)).check(matches(not(isDisplayed())));
        } else {
            onView(withId(R.id.prevStepBtn)).check(matches(isDisplayed()));
            onView(withId(R.id.nextStepBtn)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void a_onPrevBtnClickOnFirstElement() {
        if (!isTablet()) {
            onView(withId(R.id.prevStepBtn)).perform(click());
            onView(withId(R.id.descriptionShortTv)).check(matches(withText(RECIPE_CARD_FIRST_ITEM_STEP_FIRST_TITLE)));
            //check toast message
            onView(withText(R.string.error_step_prev)).inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        }
    }

    @Test
    public void b_onNextBtnClick() {
        if (!isTablet()) {
            onView(withId(R.id.nextStepBtn)).perform(click());
            onView(withId(R.id.descriptionShortTv)).check(matches(withText(RECIPE_CARD_FIRST_ITEM_STEP_SECOND_TITLE)));
        }
    }

    private void onFirstRecipeCardElementClick() {
        onView(withId(R.id.recipeRecycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_CARD_FIRST_ITEM_POSITION, click()));
    }

    private void onFirstStepItemClick() {
        onView(withId(R.id.recipeInfoRecycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_CARD_FIRST_ITEM_STEP_FIRST_POSITION, click()));
    }

}


