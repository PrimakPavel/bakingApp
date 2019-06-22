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

    @Query("SELECT * FROM recipes WHERE id IN (:recipeIds)")
    List<RecipeEntity> loadRecipesByIds(List<Integer> recipeIds);

    @Query("SELECT* FROM recipes WHERE id = :recipeId")
    LiveData<RecipeEntity> loadRecipeById(int recipeId);

    @Query("SELECT COUNT(id) FROM recipes")
    Integer getRowCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeEntity recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllRecipes(List<RecipeEntity> recipes);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeEntity recipe);

    @Delete
    void deleteRecipe(RecipeEntity recipe);

}
