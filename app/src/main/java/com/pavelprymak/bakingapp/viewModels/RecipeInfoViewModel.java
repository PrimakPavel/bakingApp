package com.pavelprymak.bakingapp.viewModels;

import androidx.lifecycle.ViewModel;

import com.pavelprymak.bakingapp.data.pojo.IngredientsItem;
import com.pavelprymak.bakingapp.data.pojo.RepoImpl;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.util.List;

public class RecipeInfoViewModel extends ViewModel {

    private RepoImpl mRepo = new RepoImpl();
    private static final int INVALID_RECIPE_ID = -1;
    private List<IngredientsItem> mIngredients;
    private List<StepsItem> mSteps;
    private int mRecipeId = INVALID_RECIPE_ID;

    public void prepareIngredientsAndSteps(int recipeId) {
        if (mSteps == null || recipeId != mRecipeId) {
            mSteps = mRepo.getStepsById(recipeId);
        }
        if (mIngredients == null || recipeId != mRecipeId) {
            mIngredients = mRepo.getIngredientsById(recipeId);
        }
        mRecipeId = recipeId;
    }

    public List<IngredientsItem> getIngredients() {
        return mIngredients;
    }

    public List<StepsItem> getSteps() {
        return mSteps;
    }
}
