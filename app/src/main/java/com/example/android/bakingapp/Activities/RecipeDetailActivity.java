package com.example.android.bakingapp.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.android.bakingapp.Constants.BundleConstants;
import com.example.android.bakingapp.Fragments.RecipeListFragment;
import com.example.android.bakingapp.POJO.RecipeJson;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.example.android.bakingapp.interfaces.OnAddToWidgetFabClickedCallback;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private ActivityRecipeDetailBinding recipeDetailBinding;
    private List<RecipeJson.Steps> steps;
    private OnAddToWidgetFabClickedCallback fabClickedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDetailBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_recipe_detail);
        //RecipeListFragment fragment = (RecipeListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_step_list_fragment);
        recipeDetailBinding.recipeDetailAddToWidgetDatabase
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fabClickedCallback != null) fabClickedCallback.onAddToWidgetFabClicked();
                        else Log.e(TAG, "fabClickedCallback IS NULL --- ");
                    }
                });
        if (null == savedInstanceState) {
            RecipeListFragment fragment = RecipeListFragment.newInstance(getBundle());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.recipe_detail_fragments_container_left, fragment, BundleConstants.RECIPE_LIST_FRAGMENT_STEPS)
                    //.addToBackStack(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS)
                    .commit();
        }
    }

    Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS, ((ArrayList<RecipeJson.Steps>) getSteps()));
        bundle.putParcelable(BundleConstants.RECIPE_LIST_FRAGMENT_RECIPE_DETAIL, (getRecipe()));
        return bundle;
    }

    /**
     * Returns the databinding of this activty
     *
     * @return
     */
    public ActivityRecipeDetailBinding getDetailActivityBinding() {
        return recipeDetailBinding;
    }

    /**
     * This method gets list of steps passed to the activity through intent
     *
     * @return List<RecipeJson.Steps>
     */
    List<RecipeJson.Steps> getSteps() {
        RecipeJson recipeJson = getRecipe();
        if(recipeJson == null)return new ArrayList<>();
        return recipeJson.getSteps();
    }

    RecipeJson getRecipe(){
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS)) {
            return intent.getParcelableExtra(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS);
        }
        return null;
    }

    /**
     * This method is called from the list fragment to initialize the callback so as for this activity
     * to get access to it and call its method when the fab is clicked
     * @param fabClickedCallback instance of callback
     */
    public void initOnAddToWidgetFabCallback(OnAddToWidgetFabClickedCallback fabClickedCallback){
        this.fabClickedCallback = fabClickedCallback;
    }


    /**
     * This method is called to hide Fab
     */
    public void hideFab(){recipeDetailBinding.recipeDetailAddToWidgetDatabase.setVisibility(View.GONE);}

    public void showFab(){recipeDetailBinding.recipeDetailAddToWidgetDatabase.setVisibility(View.VISIBLE);}
}
