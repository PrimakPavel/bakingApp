package com.pavelprymak.bakingapp.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pavelprymak.bakingapp.data.db.converters.IngredientsConverter;
import com.pavelprymak.bakingapp.data.db.converters.StepsConverter;

import timber.log.Timber;

@Database(entities = {RecipeEntity.class,FavoriteRecipeEntity.class}, version = 1, exportSchema = false)
@TypeConverters(value = {IngredientsConverter.class, StepsConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "db_recipes";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d("Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Timber.d("Getting the database instance");
        return sInstance;
    }

    public abstract RecipeDao recipesDao();
    public abstract FavoriteRecipeDao favoritesDao();
}