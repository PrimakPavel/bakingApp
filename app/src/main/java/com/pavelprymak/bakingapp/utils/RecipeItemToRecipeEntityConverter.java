package com.pavelprymak.bakingapp.utils;

import androidx.annotation.NonNull;

import com.pavelprymak.bakingapp.data.db.RecipeEntity;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class RecipeItemToRecipeEntityConverter {

    public static RecipeEntity convertToRecipeEntity(@NonNull RecipeItem recipe) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(recipe.getId());
        recipeEntity.setImage(recipe.getImage());
        recipeEntity.setName(recipe.getName());
        recipeEntity.setIngredients(recipe.getIngredients());
        recipeEntity.setSteps(recipe.getSteps());
        recipeEntity.setServings(recipe.getServings());
        return recipeEntity;
    }

    public static RecipeItem convertToRecipeItem(@NonNull RecipeEntity recipe) {
        RecipeItem recipeItem = new RecipeItem();
        recipeItem.setId(recipe.getId());
        recipeItem.setImage(recipe.getImage());
        recipeItem.setName(recipe.getName());
        recipeItem.setIngredients(recipe.getIngredients());
        recipeItem.setSteps(recipe.getSteps());
        recipeItem.setServings(recipe.getServings());
        return recipeItem;
    }

    public static List<RecipeEntity> convertToRecipeEntityList(@NonNull List<RecipeItem> recipes) {
        List<RecipeEntity> recipeEntities = new ArrayList<>();
        for (RecipeItem recipeItem : recipes) {
            recipeEntities.add(convertToRecipeEntity(recipeItem));
        }
        return recipeEntities;
    }

    public static List<RecipeItem> convertToRecipeItemList(@NonNull List<RecipeEntity> recipes) {
        List<RecipeItem> recipeItems = new ArrayList<>();
        for (RecipeEntity recipeEntity : recipes) {
            recipeItems.add(convertToRecipeItem(recipeEntity));
        }
        return recipeItems;
    }
}
