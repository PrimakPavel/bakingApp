package com.pavelprymak.bakingapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;


class MainActivityTestHelper {
    static final String RECIPE_CARD_FIRST_ITEM_TITLE = "Nutella Pie";
    static final String RECIPE_CARD_LAST_ITEM_TITLE = "Cheesecake";
    static final int RECIPE_CARD_FIRST_ITEM_POSITION = 0;
    static final int RECIPE_CARD_LAST_ITEM_POSITION = 3;

    static final String RECIPE_CARD_FIRST_ITEM_STEP_FIRST_TITLE = "Recipe Introduction";
    static final String RECIPE_CARD_FIRST_ITEM_STEP_SECOND_TITLE = "Starting prep";
    static final String RECIPE_CARD_FIRST_ITEM_STEP_LAST_TITLE = "Finishing Steps";
    static final int RECIPE_CARD_FIRST_ITEM_STEP_FIRST_POSITION = 1;
    static final int RECIPE_CARD_FIRST_ITEM_STEP_SECOND_POSITION = 2;
    static final int RECIPE_CARD_FIRST_ITEM_STEP_LAST_POSITION = 7;

    static boolean isTablet() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return targetContext.getResources().getBoolean(R.bool.isTablet);
    }
}
