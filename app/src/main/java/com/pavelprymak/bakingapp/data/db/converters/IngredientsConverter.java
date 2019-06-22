package com.pavelprymak.bakingapp.data.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pavelprymak.bakingapp.data.pojo.IngredientsItem;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientsConverter {

    @TypeConverter
    public String fromIngredientsList(List<IngredientsItem> ingredients) {
        if (ingredients == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<IngredientsItem>>() {
        }.getType();
        return gson.toJson(ingredients, type);
    }

    @TypeConverter
    public List<IngredientsItem> toIngredientsList(String ingredientsString) {
        if (ingredientsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<IngredientsItem>>() {
        }.getType();
        return gson.fromJson(ingredientsString, type);
    }
}
