package com.pavelprymak.bakingapp.data.pojo;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.data.Repo;

import java.util.Arrays;
import java.util.List;

public class RepoImpl implements Repo {
    @Override
    public List<RecipeItem> getRecipes() {
        RecipeItem[] recipes = App.recipes;
        if (recipes != null) {
            return Arrays.asList(recipes);
        }
        return null;
    }

    @Override
    public List<IngredientsItem> getIngredientsById(int recipeId) {
        RecipeItem[] recipes = App.recipes;
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
        RecipeItem[] recipes = App.recipes;
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
