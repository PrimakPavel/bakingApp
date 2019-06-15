package com.pavelprymak.bakingapp.data;

import com.pavelprymak.bakingapp.data.pojo.IngredientsItem;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.util.List;

public interface Repo {

    List<RecipeItem> getRecipes();

    List<IngredientsItem> getIngredientsById(int recipeId);

    List<StepsItem> getStepsById(int recipeId);

}
