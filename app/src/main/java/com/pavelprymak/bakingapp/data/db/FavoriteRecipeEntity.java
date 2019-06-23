package com.pavelprymak.bakingapp.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "favorite_recipe")
public class FavoriteRecipeEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "recipe_id")
    private int recipeId;


    public FavoriteRecipeEntity(int recipeId) {
        this.recipeId = recipeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
