package com.pavelprymak.bakingapp.presentation.viewModels;

import androidx.lifecycle.ViewModel;

import com.pavelprymak.bakingapp.data.pojo.IngredientsItem;
import com.pavelprymak.bakingapp.data.pojo.RepoImpl;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_RECIPE_ID;

public class RecipeInfoViewModel extends ViewModel {

    private RepoImpl mRepo = new RepoImpl();
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
