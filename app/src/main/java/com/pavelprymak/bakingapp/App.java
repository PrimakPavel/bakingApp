package com.pavelprymak.bakingapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.pavelprymak.bakingapp.data.FileToPOJOConverter;
import com.pavelprymak.bakingapp.data.db.AppDatabase;
import com.pavelprymak.bakingapp.data.db.repo.DbRepo;
import com.pavelprymak.bakingapp.data.db.repo.DbRepoImpl;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.utils.AppExecutors;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class App extends Application {
    private static final Object LOCK_1 = new Object();
    public static final String CHANNEL_ID = "BakingPlayerChannel";
    public static final String CHANNEL_NAME = "Baking App Channel";
    public static List<RecipeItem> recipes;
    public static AppExecutors appExecutors;
    public static Bus eventBus = new Bus();
    public static DbRepo dbRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        createNotificationChannel();
        recipes = loadRecipesDataFromFile(getApplicationContext());

        appExecutors = new AppExecutors();
        dbRepo = new DbRepoImpl(AppDatabase.getInstance(getApplicationContext()), appExecutors.diskIO());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public static List<RecipeItem> loadRecipesDataFromFile(Context context) {
        synchronized (LOCK_1) {
            try {
                if (recipes == null) {
                    recipes = Arrays.asList(FileToPOJOConverter.getRecipes(context));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return recipes;
        }
    }
}
