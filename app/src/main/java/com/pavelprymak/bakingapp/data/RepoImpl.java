package com.pavelprymak.bakingapp.data;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.data.pojo.IngredientsItem;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.util.List;

public class RepoImpl implements Repo {
    @Override
    public List<RecipeItem> getRecipes() {
        return App.recipes;
    }

    @Override
    public List<IngredientsItem> getIngredientsById(int recipeId) {
        List<RecipeItem> recipes = App.recipes;
        if (recipes != null) {
            for (RecipeItem recipeItem : recipes) {
                if (recipeItem != null && recipeItem.getId() == recipeId) {
                    return recipeItem.getIngredients();
                }
            }
        }
        return null;
    }

    @Override
    public List<StepsItem> getStepsById(int recipeId) {
        List<RecipeItem> recipes = App.recipes;
        if (recipes != null) {
            for (RecipeItem recipeItem : recipes) {
                if (recipeItem != null && recipeItem.getId() == recipeId) {
                    return recipeItem.getSteps();
                }
            }
        }
        return null;
    }
}
