package com.example.android.bakingapp.Utility;

import android.arch.persistence.room.TypeConverter;

import com.example.android.bakingapp.POJO.RecipeJson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RecipeTypeConverters {

    private static Gson gson = new Gson();

    @TypeConverter
    public static String ingredientsToString(List<RecipeJson.Ingredients> ingredients) {
        return gson.toJson(ingredients);
    }

    @TypeConverter
    public static List<RecipeJson.Ingredients> stringToIngredients(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<RecipeJson.Ingredients>>() {
        }
                .getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String stepsToString(List<RecipeJson.Steps> steps) {
        return gson.toJson(steps);
    }

    @TypeConverter
    public static List<RecipeJson.Steps> stringToSteps(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<RecipeJson.Steps>>() {
        }
                .getType();
        return gson.fromJson(data, listType);
    }
}
