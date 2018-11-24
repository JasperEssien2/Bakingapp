package com.example.android.bakingapp.interfaces;

import com.example.android.bakingapp.POJO.RecipeJson;

import java.util.List;

public interface RecipeDatabaseCallback {

    void recipesGotten(List<RecipeJson> recipeJsonList);

    void recipeAdded();
}
