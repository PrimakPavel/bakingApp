package com.pavelprymak.bakingapp.data.db.repo;

import androidx.lifecycle.LiveData;

import com.pavelprymak.bakingapp.data.db.RecipeEntity;

import java.util.List;

public interface DbRepo {
    void insertRecipe(RecipeEntity recipe);

    void insertAllRecipes(List<RecipeEntity> recipes);

    void updateRecipe(RecipeEntity recipe);

    void deleteRecipe(RecipeEntity recipe);

    LiveData<List<RecipeEntity>> loadAllRecipes();
    Integer loadRecipesCount();

    LiveData<List<RecipeEntity>> loadAllFavoritesRecipes();

    LiveData<RecipeEntity> loadRecipeById(int recipeId);
}
