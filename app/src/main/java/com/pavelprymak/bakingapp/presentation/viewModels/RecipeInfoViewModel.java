package com.pavelprymak.bakingapp.presentation.viewModels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.data.RecipeItemToRecipeEntityConverter;
import com.pavelprymak.bakingapp.data.db.FavoriteRecipeEntity;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.util.ArrayList;
import java.util.List;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_POSITION;

public class RecipeInfoViewModel extends ViewModel {
    private LiveData<RecipeItem> recipeItemData = new MutableLiveData<>();
    private LiveData<List<Integer>> favoriteRecipeIdsData = new MutableLiveData<>();
    private List<StepsItem> mSteps;

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

    public LiveData<RecipeItem> getRecipeItemById(int recipeId) {
        if (recipeItemData.getValue() == null || recipeItemData.getValue().getId() != recipeId) {
            recipeItemData = Transformations.map(App.dbRepo.loadRecipeById(recipeId), input -> {
                if (input != null) {
                    mSteps = input.getSteps();
                    return RecipeItemToRecipeEntityConverter.convertToRecipeItem(input);
                }
                return null;
            });
        }
        return recipeItemData;
    }

    public LiveData<List<Integer>> getFavoritesIdList() {
        if (favoriteRecipeIdsData.getValue() == null) {
            favoriteRecipeIdsData = Transformations.map(App.dbRepo.loadAllFavoritesRecipes(), input -> {
                if (input != null) {
                    List<Integer> favoriteIds = new ArrayList<>();
                    for (FavoriteRecipeEntity favoriteRecipe : input) {
                        if (favoriteRecipe != null)
                            favoriteIds.add(favoriteRecipe.getRecipeId());
                    }
                    return favoriteIds;
                }
                return null;
            });
        }
        return favoriteRecipeIdsData;
    }

    public void addToFavorite() {
        if (recipeItemData.getValue() != null) {
            App.dbRepo.insertFavoriteRecipe(recipeItemData.getValue().getId());
        }
    }

    public void removeFromFavorite() {
        if (recipeItemData.getValue() != null) {
            App.dbRepo.deleteFavoriteRecipe(recipeItemData.getValue().getId());
        }
    }

    public void removeObservers(LifecycleOwner lifecycleOwner){
        favoriteRecipeIdsData.removeObservers(lifecycleOwner);
        recipeItemData.removeObservers(lifecycleOwner);
    }

}
