package com.example.android.bakingapp.Fragments;


import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bakingapp.Activities.RecipeDetailActivity;
import com.example.android.bakingapp.Adapters.RecipesAdapter;
import com.example.android.bakingapp.Constants.BundleConstants;
import com.example.android.bakingapp.POJO.RecipeJson;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utility.RecipeDatabase;
import com.example.android.bakingapp.databinding.FragmentRecipeListBinding;
import com.example.android.bakingapp.interfaces.OnAddToWidgetFabClickedCallback;
import com.example.android.bakingapp.interfaces.RecipeDatabaseCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment implements OnAddToWidgetFabClickedCallback,
        RecipeDatabaseCallback {

    private static final String TAG = RecipeListFragment.class.getSimpleName();
    private FragmentRecipeListBinding fragmentRecipeListBinding;
    private RecyclerView stepsRecyclerView, ingredientsRecyclerView;
    private RecipesAdapter.RecipesStepsListAdapter stepsListAdapter;
    private RecipesAdapter.RecipeIngredientListAdapter ingredientListAdapter;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    public static RecipeListFragment newInstance(Bundle args) {
        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    List<RecipeJson.Steps> steps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentRecipeListBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_recipe_list, container, false);
        ((RecipeDetailActivity) getActivity())
                .initOnAddToWidgetFabCallback(this);
        ((RecipeDetailActivity) getActivity()).showFab();
        setUpStepsRecyclerView();
        setUpIngredientsRecyclerView();
        if (getArguments() != null && getArguments().containsKey(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS)) {
            steps = getArguments().<RecipeJson.Steps>getParcelableArrayList(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS);
            stepsListAdapter.setList(getArguments().<RecipeJson.Steps>getParcelableArrayList(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS));
        } else {
        }
        return fragmentRecipeListBinding.getRoot();
    }


    /**
     * Getting the bundle, that is required by the RecipeStepsDetailFragments
     *
     * @param steps the object that contains steps to be used in the detail fragment
     * @param pos   the current position of the fragments, This is useful to keep track of the
     *              fragments opened when the user clicks previous or next button
     * @return a bundle
     */
    public Bundle getBundle(RecipeJson.Steps steps, int pos) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstants.RECIPE_STEPS_DETAIL, steps);
        bundle.putInt(BundleConstants.CLICKED_ITEM_POSITION, pos);
        //TODO:Learn how to pass via Parcelable
        //bundle.putSerializable(BundleConstants.PREV_NEXT_BUTTON_CALL_BACK_KEY, this);
        //bundle.putBoolean(BundleConstants.IS_VIDEO, steps.getVideoUrl() != null);
        return bundle;
    }

//    @Override
//    public void onResume() {
//        ((RecipeDetailActivity) getActivity()).showFab();
//        super.onResume();
//    }
//
//    @Override
//    public void onStart() {
//        ((RecipeDetailActivity) getActivity()).showFab();
//        super.onStart();
//    }

    /**
     * This method sets up the recycler view that shows the ingredients of the recipe
     */
    void setUpIngredientsRecyclerView() {
        setIngredientListAdapter();
        ingredientsRecyclerView = fragmentRecipeListBinding.recipeIngredientsListRecyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        ingredientsRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientsRecyclerView.setAdapter(ingredientListAdapter);
    }

    /**
     * This method sets up the recycler view that shows the steps of the recipe
     */
    void setUpStepsRecyclerView() {
        stepsListAdapter = new RecipesAdapter.RecipesStepsListAdapter(getActivity(), new ArrayList<RecipeJson.Steps>());
        setStepListAdapter();
        stepsRecyclerView = fragmentRecipeListBinding.recipeCardListRecyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        stepsRecyclerView.setLayoutManager(linearLayoutManager);
        stepsRecyclerView.setAdapter(stepsListAdapter);

    }

    public void setList(List<RecipeJson.Steps> stepsList) {
        stepsListAdapter.setList(stepsList);
    }

    /**
     * This method is responsible for setting up the step list adapter
     * passing in the list of steps to the adapter
     */
    void setStepListAdapter() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS)) {
            List<RecipeJson.Steps> steps = bundle.getParcelableArrayList
                    (BundleConstants.RECIPE_LIST_FRAGMENT_STEPS);
            if (steps == null)
                steps = new ArrayList<>();
            stepsListAdapter = new RecipesAdapter.RecipesStepsListAdapter(getActivity(), steps);
        }
    }

    /**
     * This method is responsible for setting up the ingredient list adapter
     * passing in the list of ingredient to the adapter
     */
    void setIngredientListAdapter() {
        List<RecipeJson.Ingredients> ingredients = new ArrayList<>();
        if (getRecipe() == null) ingredients = new ArrayList<>();
        else ingredients = getRecipe().getIngredients();
        ingredientListAdapter = new RecipesAdapter.RecipeIngredientListAdapter(ingredients);

    }

    /**
     * Gets the recipe from the bundle passed to this fragment
     *
     * @return RecipeJson object contains all necessary info about the recipe
     */
    RecipeJson getRecipe() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BundleConstants.RECIPE_LIST_FRAGMENT_RECIPE_DETAIL)) {
            return bundle.getParcelable(BundleConstants.RECIPE_LIST_FRAGMENT_RECIPE_DETAIL);
        } else return null;
    }

    /**
     * A call back method that is called when add to widget button is clicked from the RecipeDetailActivity
     */
    @Override
    public void onAddToWidgetFabClicked() {
        if (getRecipe() == null) return;
        new AddRecipeToWidgetDBAsyncTask(RecipeDatabase.getDatabase(getContext().getApplicationContext()),
                this)
                .execute(getRecipe());
    }

    @Override
    public void recipesGotten(List<RecipeJson> recipeJsonList) {

    }

    /**
     * Call back method called when the ingredient list is added to the database for displaying in widget
     */
    @Override
    public void recipeAdded() {
        Toast.makeText(getContext(), "Ingredients of Recipe Added to widget", Toast.LENGTH_SHORT).show();
    }

    /**
     * Class to perform async task work of putting the recipe into the database to be used when displaying
     * ingredients in the widget
     */
    private static class AddRecipeToWidgetDBAsyncTask extends AsyncTask<RecipeJson, Void, Void> {
        private RecipeDatabase recipeDatabase;
        private RecipeDatabaseCallback callback;

        public AddRecipeToWidgetDBAsyncTask(RecipeDatabase recipeDatabase, RecipeDatabaseCallback callback) {
            super();
            this.recipeDatabase = recipeDatabase;

            this.callback = callback;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            callback.recipeAdded();
        }

        @Override
        protected Void doInBackground(RecipeJson... recipeJsons) {
            recipeDatabase.getRecipeDao().insertRecipe(recipeJsons[0]);
            return null;
        }
    }
}
