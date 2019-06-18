package com.pavelprymak.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.screens.RecipeInfoFragment.ARG_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.screens.RecipeInfoFragment.ARG_RECIPE_TITLE;

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<RecipeItem> mRecipes;

    GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
        mRecipes = App.loadRecipesDataFromFile(mContext);
    }


    @Override
    public int getCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipes == null || mRecipes.size() == 0) return null;
        RecipeItem recipe = mRecipes.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_app_provider);
        Bundle extras = new Bundle();
        extras.putInt(ARG_RECIPE_ID, recipe.getId());
        extras.putString(ARG_RECIPE_TITLE, recipe.getName());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.appwidget_text, fillInIntent);
        views.setTextViewText(R.id.appwidget_text, recipe.getName());
        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}


