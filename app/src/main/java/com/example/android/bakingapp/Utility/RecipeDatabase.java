package com.example.android.bakingapp.Utility;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.android.bakingapp.POJO.RecipeJson;
import com.example.android.bakingapp.interfaces.RecipeDao;

@Database(entities = {RecipeJson.class}, version = 1, exportSchema = false)
@TypeConverters(RecipeTypeConverters.class)
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase INSTANCE;

    public abstract RecipeDao getRecipeDao();

    public static synchronized RecipeDatabase getDatabase(Context context){
        if(INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class,
                    "recipe-database")
                    .build();
        return INSTANCE;
    }
}
