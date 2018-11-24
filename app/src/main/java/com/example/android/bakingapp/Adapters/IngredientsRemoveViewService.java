package com.example.android.bakingapp.Adapters;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.BakingAppWidgetProvider;
import com.example.android.bakingapp.POJO.RecipeJson;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utility.RecipeDatabase;
import com.example.android.bakingapp.interfaces.RecipeDatabaseCallback;

import java.util.ArrayList;
import java.util.List;

public class IngredientsRemoveViewService implements RemoteViewsService.RemoteViewsFactory,
        RecipeDatabaseCallback {

    private static final String TAG = IngredientsRemoveViewService.class.getSimpleName();
    List<RecipeJson> recipeJsonList = new ArrayList<>();
    List<RecipeJson.Ingredients> ingredientsList = new ArrayList<>();

    private Context context = null;
    private int appWidgetId;
    private String recipeName;

    public IngredientsRemoveViewService(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
//        ingredientsList.add(new RecipeJson.Ingredients(5, "", "FIsh"));
//        ingredientsList.add(new RecipeJson.Ingredients(5, "", "FIsh"));
//        ingredientsList.add(new RecipeJson.Ingredients(5, "", "FIsh"));
//        ingredientsList.add(new RecipeJson.Ingredients(5, "", "FIsh"));

        new GetRecipeAsyncTask(RecipeDatabase.getDatabase(context.getApplicationContext()), this)
                .execute();
    }

    @Override
    public void recipesGotten(List<RecipeJson> recipeJsonList) {
        this.recipeJsonList = recipeJsonList;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, BakingAppWidgetProvider.class));
        Log.e(TAG, "RecipeList Gotten from database -- " + recipeJsonList.toString());
        if (recipeJsonList.size() > 0) {
            ingredientsList = recipeJsonList.get(0).getIngredients();
            recipeName = recipeJsonList.get(0).getName();
            Log.e(TAG, "RecipeName :: -- " + recipeName);
        }
        else {

        }
        //onDataSetChanged();
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                R.id.appwidget_recipe_ingredients_list);
        updateWidgetHeader();
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_item);
        Log.e(TAG, "ingredientList Gotten from database -- " + ingredientsList.toString());
    }

    void updateWidgetHeader(){
        new RemoteViews(context.getPackageName(), R.layout.exo_playback_control_view)
                .setTextViewText(R.id.appwidget_list_item, recipeName);
    }
    @Override
    public void recipeAdded() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_widget_ingredients);
        String ingredient = ingredientsList.get(i).getIngredient();
        rv.setTextViewText(R.id.appwidget_list_item, ingredient);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * This creates an asyncTask to get the Recipe database that was added to the widgets
     * then initializes the ingredients list, when the async task is done
     */
    private static class GetRecipeAsyncTask extends AsyncTask<Void, Void, List<RecipeJson>> {
        private RecipeDatabase recipeDatabase;
        private RecipeDatabaseCallback callback;

        public GetRecipeAsyncTask(RecipeDatabase recipeDatabase, RecipeDatabaseCallback callback) {
            super();
            this.recipeDatabase = recipeDatabase;

            this.callback = callback;
        }

        @Override
        protected List<RecipeJson> doInBackground(Void... voids) {
            return recipeDatabase.getRecipeDao().getRecipe();
        }

        @Override
        protected void onPostExecute(List<RecipeJson> recipeJsons) {
            super.onPostExecute(recipeJsons);
            callback.recipesGotten(recipeJsons);
        }
    }
}
