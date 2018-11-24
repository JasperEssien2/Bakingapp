package com.example.android.bakingapp.Rest;

import com.example.android.bakingapp.POJO.RecipeJson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("baking.json")
    Call<List<RecipeJson>> getRecipes();
}
