package com.pavelprymak.bakingapp.viewModels;

import androidx.lifecycle.ViewModel;

import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.data.pojo.RepoImpl;

import java.util.List;

public class RecipeCardViewModel extends ViewModel {
    private RepoImpl mRepo = new RepoImpl();
    private List<RecipeItem> mRecipes;

    public List<RecipeItem> getRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepo.getRecipes();
        }
        return mRecipes;
    }

}
