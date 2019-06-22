package com.pavelprymak.bakingapp.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.utils.RecipeItemToRecipeEntityConverter;

import java.util.List;

import static com.pavelprymak.bakingapp.App.CHANNEL_ID;
import static com.pavelprymak.bakingapp.App.CHANNEL_NAME;

public class RecipeUpdateWidgetService extends IntentService {
    public static final String TAG = RecipeUpdateWidgetService.class.getSimpleName();
    private PowerManager.WakeLock wakeLock;
    private static final long WAKE_LOCK_TIMEOUT = 60000;
    public static final String ACTION_UPDATE_RECIPES_WIDGET = "com.pavelprymak.bakingapp.action.update_plant_widgets";

    public RecipeUpdateWidgetService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BakingApp:Wakelock");
        wakeLock.acquire(WAKE_LOCK_TIMEOUT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Running...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(1, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPES_WIDGET.equals(action)) {
                handleActionUpdateWidgets();
            }
        }
    }

    public static void startActionUpdateRecipesWidgets(Context context) {
        Intent intent = new Intent(context, RecipeUpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPES_WIDGET);
        ContextCompat.startForegroundService(context, intent);
    }

    private void handleActionUpdateWidgets() {
        List<Integer> favoriteIds = App.dbRepo.loadAllFavoritesRecipeIds();
        List<RecipeItem> recipeItems = RecipeItemToRecipeEntityConverter.convertToRecipeItemList(App.dbRepo.loadRecipesByIds(favoriteIds));
        RecipeItem recipeFirstItem = null;
        if (recipeItems != null && recipeItems.size() > 0) {
            recipeFirstItem = recipeItems.get(0);
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        RecipeAppProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, recipeFirstItem);
    }
}

