package com.pavelprymak.bakingapp.data.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;

import java.lang.reflect.Type;
import java.util.List;

public class StepsConverter {

    @TypeConverter
    public String fromStepsList(List<StepsItem> steps) {
        if (steps == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StepsItem>>() {
        }.getType();
        return gson.toJson(steps, type);
    }

    @TypeConverter
    public List<StepsItem> toStepsList(String stepsString) {
        if (stepsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StepsItem>>() {
        }.getType();
        return gson.fromJson(stepsString, type);
    }
}
