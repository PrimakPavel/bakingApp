package com.pavelprymak.bakingapp.data.db.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.pavelprymak.bakingapp.data.db.AppDatabase;
import com.pavelprymak.bakingapp.data.db.RecipeEntity;

import java.util.List;
import java.util.concurrent.Executor;

public class DbRepoImpl implements DbRepo {

    private final AppDatabase mDb;
    private final Executor diskIO;

    public DbRepoImpl(@NonNull AppDatabase appDatabase, @NonNull Executor discIOExecutor) {
        mDb = appDatabase;
        diskIO = discIOExecutor;
    }

    @Override
    public void insertRecipe(RecipeEntity recipe) {
        diskIO.execute(() -> mDb.recipesDao().insertRecipe(recipe));

    }

    @Override
    public void insertAllRecipes(List<RecipeEntity> recipes) {
        diskIO.execute(() -> mDb.recipesDao().insertAllRecipes(recipes));
    }

    @Override
    public void updateRecipe(RecipeEntity recipe) {
        diskIO.execute(() -> mDb.recipesDao().updateRecipe(recipe));
    }

    @Override
    public void deleteRecipe(RecipeEntity recipe) {
        diskIO.execute(() -> mDb.recipesDao().deleteRecipe(recipe));
    }

    @Override
    public LiveData<List<RecipeEntity>> loadAllRecipes() {
        return mDb.recipesDao().loadAllRecipes();
    }

    @Override
    public Integer loadRecipesCount() {
        return mDb.recipesDao().getRowCount();
    }

    @Override
    public LiveData<List<RecipeEntity>> loadAllFavoritesRecipes() {
        return mDb.recipesDao().loadAllFavoritesRecipe();
    }

    @Override
    public LiveData<RecipeEntity> loadRecipeById(int recipeId) {
        return mDb.recipesDao().loadRecipeById(recipeId);
    }
}
