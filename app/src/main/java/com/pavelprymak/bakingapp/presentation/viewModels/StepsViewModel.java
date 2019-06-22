package com.pavelprymak.bakingapp.presentation.viewModels;


import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.data.RecipeItemToRecipeEntityConverter;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_ID;

public class StepsViewModel extends ViewModel {
    private static final int FIRST_POSITION = 0;
    private LiveData<RecipeItem> recipeItemData = new MutableLiveData<>();
    private int mStepId = INVALID_STEP_ID;
    private LiveData<StepsItem> currentStepData;
    private LiveData<StepsItem> nextStepData;
    private LiveData<StepsItem> prevStepData;

    public void prepareRecipeItemById(int recipeId, int currentStepId) {
        if (recipeItemData.getValue() == null || recipeItemData.getValue().getId() != recipeId) {
            recipeItemData = Transformations.map(App.dbRepo.loadRecipeById(recipeId), input -> {
                if (input != null) {
                    if (currentStepId == INVALID_STEP_ID) {
                        setFirstStepAsCurrent(input.getSteps());
                    } else {
                        mStepId = currentStepId;
                    }
                    return RecipeItemToRecipeEntityConverter.convertToRecipeItem(input);
                }
                return null;
            });
        }
    }

    private void setFirstStepAsCurrent(List<StepsItem> steps) {
        if (steps != null && steps.size() > 0 && mStepId == INVALID_STEP_ID) {
            StepsItem firstStep = steps.get(FIRST_POSITION);
            if (firstStep != null) {
                mStepId = firstStep.getId();
            }
        }
    }


    public LiveData<StepsItem> getCurrentStepData() {
        currentStepData = Transformations.map(recipeItemData, input -> {
            if (input != null && input.getSteps() != null && mStepId != INVALID_STEP_ID) {
                for (StepsItem step : input.getSteps()) {
                    if (step.getId() == mStepId) {
                        return step;
                    }
                }
            }
            return null;
        });
        return currentStepData;
    }

    public LiveData<StepsItem> getCurrentStepDataById(int stepId) {
        mStepId = stepId;
        return getCurrentStepData();
    }

    public LiveData<StepsItem> getNextStepItem() {
        nextStepData = Transformations.map(recipeItemData, input -> {
            if (input != null) {
                List<StepsItem> steps = input.getSteps();
                if (steps != null && mStepId != INVALID_STEP_ID) {
                    for (int i = 0; i < steps.size(); i++) {
                        final int nextIndex = i + 1;
                        if (steps.get(i) != null
                                && steps.get(i).getId() == mStepId
                                && (nextIndex) < steps.size()) {
                            StepsItem nextStep = steps.get(nextIndex);
                            if (nextStep != null) {
                                mStepId = nextStep.getId();
                            }
                            return nextStep;
                        }
                    }
                }
            }
            return null;
        });
        return nextStepData;
    }

    public LiveData<StepsItem> getPrevStepItem() {
        prevStepData = Transformations.map(recipeItemData, input -> {
            if (input != null) {
                List<StepsItem> steps = input.getSteps();
                if (steps != null && mStepId != INVALID_STEP_ID) {
                    for (int i = 0; i < steps.size(); i++) {
                        final int prevIndex = i - 1;
                        if (steps.get(i) != null
                                && steps.get(i).getId() == mStepId
                                && (prevIndex) >= 0) {
                            StepsItem prevStep = steps.get(prevIndex);
                            if (prevStep != null) {
                                mStepId = prevStep.getId();
                            }
                            return prevStep;
                        }
                    }
                }
            }
            return null;
        });
        return prevStepData;
    }

    public void removeObserversAll(LifecycleOwner lifecycleOwner) {
        recipeItemData.removeObservers(lifecycleOwner);
        removeObserversCurrentStepData(lifecycleOwner);
        removeObserversNextStepData(lifecycleOwner);
        removeObserversPrevStepData(lifecycleOwner);
    }

    public void removeObserversNextStepData(LifecycleOwner lifecycleOwner) {
        if (nextStepData != null) {
            nextStepData.removeObservers(lifecycleOwner);
        }
    }

    public void removeObserversPrevStepData(LifecycleOwner lifecycleOwner) {
        if (prevStepData != null) {
            prevStepData.removeObservers(lifecycleOwner);
        }
    }

    public void removeObserversCurrentStepData(LifecycleOwner lifecycleOwner) {
        if (currentStepData != null) {
            currentStepData.removeObservers(lifecycleOwner);
        }
    }
}
