package com.pavelprymak.bakingapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.pavelprymak.bakingapp.data.FileToPOJOConverter;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;

import java.io.IOException;

import timber.log.Timber;

public class App extends Application {
    public static final String CHANNEL_ID = "BakingPlayerChannel";
    public static final String CHANNEL_NAME = "Baking App Player Channel";
    public static RecipeItem[] recipes;

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
        createNotificationChannel();
        loadRecipesDataFromFile();
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

    private void loadRecipesDataFromFile(){
        try {
            recipes = FileToPOJOConverter.getRecipes(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
