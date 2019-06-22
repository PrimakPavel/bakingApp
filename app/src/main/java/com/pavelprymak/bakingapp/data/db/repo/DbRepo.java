package com.pavelprymak.bakingapp.data.db.repo;

import androidx.lifecycle.LiveData;

import com.pavelprymak.bakingapp.data.db.FavoriteRecipeEntity;
import com.pavelprymak.bakingapp.data.db.RecipeEntity;

import java.util.List;

public interface DbRepo {
    void insertRecipe(RecipeEntity recipe);

    void insertAllRecipes(List<RecipeEntity> recipes);

    void updateRecipe(RecipeEntity recipe);

    void deleteRecipe(RecipeEntity recipe);

    LiveData<List<RecipeEntity>> loadAllRecipes();

    List<RecipeEntity> loadRecipesByIds(List<Integer> recipeIds);

    Integer loadRecipesCount();

    LiveData<List<FavoriteRecipeEntity>> loadAllFavoritesRecipes();

    List<Integer> loadAllFavoritesRecipeIds();

    void deleteFavoriteRecipe(int recipeId);

    void insertFavoriteRecipe(int recipeId);


    LiveData<RecipeEntity> loadRecipeById(int recipeId);
}
