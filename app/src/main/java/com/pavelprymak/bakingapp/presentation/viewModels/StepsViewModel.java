package com.pavelprymak.bakingapp.presentation.viewModels;


import androidx.lifecycle.ViewModel;

import com.pavelprymak.bakingapp.data.pojo.RepoImpl;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_ID;

public class StepsViewModel extends ViewModel {
    private static final int FIRST_POSITION = 0;


    private RepoImpl mRepo = new RepoImpl();
    private int mRecipeId = INVALID_RECIPE_ID;
    private int mStepId = INVALID_STEP_ID;
    private List<StepsItem> mSteps = null;


    public void prepareStepsByRecipeId(int recipeId) {
        if (mSteps == null || mRecipeId != recipeId) {
            mRecipeId = recipeId;
            mSteps = mRepo.getStepsById(mRecipeId);
            setFirstStepAsCurrent();
        }
    }

    private void setFirstStepAsCurrent() {
        if (mSteps != null && mSteps.size() > 0) {
            StepsItem firstStep = mSteps.get(FIRST_POSITION);
            if (firstStep != null) {
                mStepId = firstStep.getId();
            }
        }
    }

    public void setCurrentStepId(int stepId) {
        mStepId = stepId;
    }


    public StepsItem getCurrentStep() {
        if (mSteps != null && mStepId != INVALID_STEP_ID) {
            for (StepsItem step : mSteps) {
                if (step.getId() == mStepId) {
                    return step;
                }
            }
        }
        return null;
    }

    public StepsItem getNextStep() {
        if (mSteps != null && mStepId != INVALID_STEP_ID) {
            for (int i = 0; i < mSteps.size(); i++) {
                final int nextIndex = i + 1;
                if (mSteps.get(i) != null
                        && mSteps.get(i).getId() == mStepId
                        && (nextIndex) < mSteps.size()) {
                    StepsItem nextStep = mSteps.get(nextIndex);
                    if (nextStep != null) {
                        mStepId = nextStep.getId();
                    }
                    return nextStep;
                }
            }
        }
        return null;
    }

    public StepsItem getPrevStep() {
        if (mSteps != null && mStepId != INVALID_STEP_ID) {
            for (int i = 0; i < mSteps.size(); i++) {
                final int prevIndex = i - 1;
                if (mSteps.get(i) != null
                        && mSteps.get(i).getId() == mStepId
                        && (prevIndex) >= 0) {
                    StepsItem prevStep = mSteps.get(prevIndex);
                    if (prevStep != null) {
                        mStepId = prevStep.getId();
                    }
                    return prevStep;
                }
            }
        }
        return null;
    }
}
