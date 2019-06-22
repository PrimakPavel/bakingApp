package com.pavelprymak.bakingapp.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.data.RecipeItemToRecipeEntityConverter;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;

import java.util.List;

public class RecipeCardViewModel extends ViewModel {
    private LiveData<List<RecipeItem>> mRecipesData = new MutableLiveData<>();

    public LiveData<List<RecipeItem>> getRecipes() {
        if (mRecipesData.getValue() == null || mRecipesData.getValue().size() == 0) {
            mRecipesData = Transformations.map(App.dbRepo.loadAllRecipes(), RecipeItemToRecipeEntityConverter::convertToRecipeItemList);
        }
        return mRecipesData;
    }

}
