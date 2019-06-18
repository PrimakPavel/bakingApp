package com.pavelprymak.bakingapp.presentation.viewModels;

import androidx.lifecycle.ViewModel;

import com.pavelprymak.bakingapp.data.pojo.IngredientsItem;
import com.pavelprymak.bakingapp.data.RepoImpl;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_POSITION;

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

    public int getPositionByStepId(int stepId) {
        if (mSteps != null) {
            for (int i = 0; i < mSteps.size(); i++) {
                StepsItem stepsItem = mSteps.get(i);
                if (stepsItem != null && stepId == stepsItem.getId()) {
                    return i;
                }
            }
        }
        return INVALID_STEP_POSITION;
    }
}
