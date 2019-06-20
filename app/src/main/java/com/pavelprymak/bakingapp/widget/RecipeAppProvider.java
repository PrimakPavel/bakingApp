package com.pavelprymak.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.pavelprymak.bakingapp.MainActivity;
import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;

import org.jetbrains.annotations.NotNull;

import static com.pavelprymak.bakingapp.presentation.adapters.RecipeCardAdapter.createIngredientsStr;
import static com.pavelprymak.bakingapp.presentation.screens.RecipeInfoFragment.ARG_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.screens.RecipeInfoFragment.ARG_RECIPE_TITLE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppProvider extends AppWidgetProvider {

    static void updateAppWidget(@NonNull Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, @NonNull RecipeItem recipe) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        if (height < 300 || width < 200) {
            rv = getSingleRemoteViews(context, recipe);
        } else {
            rv = getRecipeGridRemoteView(context);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @NotNull
    private static RemoteViews getSingleRemoteViews(@NonNull Context context, @NonNull RecipeItem recipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_provider);
        views.setTextViewText(R.id.app_w_recipe_title, recipe.getName());
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ARG_RECIPE_ID, recipe.getId());
        intent.putExtra(ARG_RECIPE_TITLE, recipe.getName());
        if (recipe.getIngredients() != null && recipe.getIngredients().size() > 0) {
            views.setTextViewText(R.id.app_w_recipe_ingredients, createIngredientsStr(context, recipe.getIngredients()));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.app_w_recipe_title, pendingIntent);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeUpdateWidgetService.startActionUpdateRecipesWidgets(context);
    }

    static void updateAppWidget(@NonNull Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, @NonNull RecipeItem recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RecipeUpdateWidgetService.startActionUpdateRecipesWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Creates and returns the RemoteViews to be displayed in the GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
    private static RemoteViews getRecipeGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }
}

