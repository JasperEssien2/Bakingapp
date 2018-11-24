package com.example.android.bakingapp.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.bakingapp.POJO.RecipeJson;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("select * from Recipe")
    List<RecipeJson> getRecipe();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeJson user);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeJson... users);
}
