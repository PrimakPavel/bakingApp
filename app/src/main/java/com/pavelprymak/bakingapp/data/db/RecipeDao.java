package com.pavelprymak.bakingapp.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes")
    LiveData<List<RecipeEntity>> loadAllRecipes();

    @Query("SELECT* FROM recipes WHERE id = :recipeId")
    LiveData<RecipeEntity> loadRecipeById(int recipeId);

    @Query("SELECT* FROM recipes WHERE is_favorite = 1")
    LiveData<List<RecipeEntity>> loadAllFavoritesRecipe();

    @Query("SELECT COUNT(id) FROM recipes")
    Integer getRowCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeEntity favoriteMovie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllRecipes(List<RecipeEntity> recipes);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeEntity favoriteMovie);

    @Delete
    void deleteRecipe(RecipeEntity favoriteMovie);

}
