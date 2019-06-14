package com.pavelprymak.bakingapp.data;

import android.content.Context;

import com.google.gson.Gson;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileToPOJOConverter {
    private static final String RECIPES_FILE_NAME = "baking.json";
    private static final String ENCODING_TYPE = "UTF-8";

    public static RecipeItem[] getRecipes(Context context) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(RECIPES_FILE_NAME), ENCODING_TYPE));
        return new Gson().fromJson(br, RecipeItem[].class);
    }
}
