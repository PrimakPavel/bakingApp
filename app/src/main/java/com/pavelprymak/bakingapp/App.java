package com.pavelprymak.bakingapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.pavelprymak.bakingapp.data.db.AppDatabase;
import com.pavelprymak.bakingapp.data.db.repo.DbRepo;
import com.pavelprymak.bakingapp.data.db.repo.DbRepoImpl;
import com.pavelprymak.bakingapp.utils.AppExecutors;
import com.squareup.otto.Bus;

import timber.log.Timber;

public class App extends Application {
    public static final String CHANNEL_ID = "BakingPlayerChannel";
    public static final String CHANNEL_NAME = "Baking App Channel";
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

}
