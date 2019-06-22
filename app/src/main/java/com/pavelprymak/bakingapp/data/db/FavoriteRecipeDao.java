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
public interface FavoriteRecipeDao {
    @Query("SELECT * FROM favorite_recipe")
    LiveData<List<FavoriteRecipeEntity>> loadAllFavorites();

    @Query("SELECT recipe_id FROM favorite_recipe")
    List<Integer> loadAllFavoritesIds();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(FavoriteRecipeEntity favoriteRecipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(RecipeEntity favoriteRecipe);

    @Delete
    void deleteFavorite(RecipeEntity favoriteRecipe);

    @Query("DELETE FROM favorite_recipe WHERE recipe_id = :recipeId")
    void deleteFavoriteByRecipeId(long recipeId);


}
