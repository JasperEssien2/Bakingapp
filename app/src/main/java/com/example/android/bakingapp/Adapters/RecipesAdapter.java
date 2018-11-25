package com.example.android.bakingapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.Activities.RecipeDetailActivity;
import com.example.android.bakingapp.Constants.BundleConstants;
import com.example.android.bakingapp.Fragments.RecipeStepsDetailFragment;
import com.example.android.bakingapp.POJO.RecipeJson;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.ItemRecipeCardBinding;
import com.example.android.bakingapp.databinding.ItemRecipeStepsBinding;
import com.example.android.bakingapp.databinding.ItemWidgetIngredientsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {
    private ItemRecipeCardBinding recipeCardBinding;
    private AnimationDrawable animationDrawable;

    private List<RecipeJson> recipeJsonList;
    private Activity activity;

    public RecipesAdapter(List<RecipeJson> recipeJsonList, Activity activity) {
        this.recipeJsonList = recipeJsonList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recipeCardBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_recipe_card, parent, false);
        animationDrawable = (AnimationDrawable) recipeCardBinding.itemRecipeCardBack.getBackground();
        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(5000);
        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        return new RecipesViewHolder(recipeCardBinding.getRoot());
    }


    @Override
    public void onBindViewHolder(@NonNull final RecipesViewHolder holder, int position) {
        RecipeJson recipe = recipeJsonList.get(holder.getAdapterPosition());
        recipeCardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationDrawable.stop();
                Intent intent = new Intent(activity, RecipeDetailActivity.class);
                intent.putExtra(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS,
                        recipeJsonList.
                                get(holder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
        holder.nameView.setText(recipe.getName());
        holder.ingredientsView.setText(String.valueOf(recipe.getIngredients().size()));
        holder.servingsView.setText(String.valueOf(recipe.getServings()));
        holder.stepsView.setText(String.valueOf(recipe.getSteps().size() + ""));
        holder.imageView.setCircleBackgroundColor(ContextCompat.getColor(holder.imageView.getContext(), R.color.colorPrimary));
        holder.imageView.setBorderWidth(2);
        holder.imageView.setBorderColor(ContextCompat.getColor(holder.imageView.getContext(), R.color.colorAccent));
        if (!recipe.getImageUrl().isEmpty())
            Picasso.get()
                    .load(recipe.getImageUrl())
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    //.error(R.drawable.image_url_broken)
                    .into(holder.imageView);
        else {
            recipeJsonList.get(holder.getAdapterPosition()).setImageUrl(getImageUrl(holder.getAdapterPosition()));
            recipe.setImageUrl(getImageUrl(holder.getAdapterPosition()));
            //recipeJsonList.(holder.getAdapterPosition())
            Picasso.get()
                    .load(getImageUrl(holder.getAdapterPosition()))
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    //.error(R.drawable.image_url_broken)
                    .into(holder.imageView);
        }
    }

    private String getImageUrl(int pos) {
        switch (pos) {
            case 0:
                return "http://arlingtonhousewife.files.wordpress.com/2011/02/nutella-pie-square.jpg";
            case 1:
                return "https://image.shutterstock.com/image-photo/delicious-chocolate-cakes-on-table-260nw-275513057.jpg";
            case 2:
                return "https://d2gk7xgygi98cy.cloudfront.net/14-3-large.jpg";
            case 3:
                return "http://assets.kraftfoods.com/recipe_images/PHILADELPHIA_Classic_Cheesecake.jpg";
            default:
                return "https://i.imgur.com/8XpQ6yP.jpg";
        }
    }

    @Override
    public int getItemCount() {
        if (recipeJsonList == null) return 0;
        return recipeJsonList.size();
    }

//    String getImageUrl(int index) {
//        recipeJsonList.get(index).setImageUrl("https://i.imgur.com/8XpQ6yP.jpg");
//        return "https://i.imgur.com/8XpQ6yP.jpg";
//    }

    public void addRecipeList(List<RecipeJson> recipeJsons) {
        for (int i = 0; i < recipeJsons.size(); i++) {
            add(recipeJsons.get(i));
        }
    }

    public void add(RecipeJson recipeJson) {
        recipeJsonList.add(recipeJson);
        notifyItemInserted(recipeJsonList.size() - 1);
    }

    public static class RecipesStepsListAdapter extends RecyclerView.Adapter<RecipesStepsListAdapter.RecipeStepsListViewHolder> {
//            implements PrevNextButtonsCallback {

        private static final String TAG = RecipesStepsListAdapter.class.getSimpleName();
        private Activity activity;
        private List<RecipeJson.Steps> stepsList;
        //private int adapterPos;

        private ItemRecipeStepsBinding recipeStepsBinding;
        private boolean navBottonClicked = false;
        private int clickedItemPos = 0;

        public RecipesStepsListAdapter(Activity activity, List<RecipeJson.Steps> stepsList) {
            this.activity = activity;
            this.stepsList = stepsList;
        }

        @NonNull
        @Override
        public RecipeStepsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            recipeStepsBinding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recipe_steps, parent, false);
            return new RecipeStepsListViewHolder(recipeStepsBinding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull final RecipeStepsListViewHolder holder, int position) {
            RecipeJson.Steps steps = stepsList.get(holder.getAdapterPosition());
            holder.textView.setText(steps.getShortDescription());
            recipeStepsBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //openStepsDetailFragment(stepsList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                    openStepsDetailFragment(stepsList, holder.getAdapterPosition());
                }
            });
        }

        /**
         * this method is responsible displaying the fragment depending on if its a tablet size device
         * or not
         *
         * @param steps the steps detail object
         */
        private void openStepsDetailFragment(RecipeJson.Steps steps, int pos) {
            clickedItemPos = pos;
            RecipeStepsDetailFragment fragment = RecipeStepsDetailFragment.newInstance(getBundle(steps, pos));
            fragment.setRetainInstance(true);
            if (isTabletLayoutUsed()) {
                ((AppCompatActivity) activity).getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.recipe_detail_fragments_container_right, fragment, BundleConstants.RECIPE_STEPS_DETAIL)
                        .commit();
            } else {
                FragmentTransaction fragmentTransaction =
                        ((AppCompatActivity) activity).getSupportFragmentManager()
                                .beginTransaction();
                RecipeStepsDetailFragment frag = (RecipeStepsDetailFragment) ((AppCompatActivity) activity).getSupportFragmentManager()
                        .findFragmentByTag(BundleConstants.RECIPE_STEPS_DETAIL);
                if (frag != null) {
                    frag.onStop();
                    frag.onDestroy(); //To release player
                    try {
                        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_recipe_steps_detail, fragment, BundleConstants.RECIPE_STEPS_DETAIL)
                                .commit();

                    } catch (IllegalStateException e) {
                    }
                } else {
                    //Only add the first item the user clicks to backStack so that even when the user
                    //has clicked on Previous or Next button multiple times, pressing back button goes back
                    //to the list of instructions
                    ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction()
                            .addToBackStack(BundleConstants.RECIPE_STEPS_DETAIL)
                            .add(R.id.fragment_recipe_list, fragment, BundleConstants.RECIPE_STEPS_DETAIL)
                            .commit();
                }
                //fragmentTransaction.commit();
            }
        }

        /**
         * this method is responsible displaying the fragment depending on if its a tablet size device
         * or not
         *
         * @param steps the steps detail object
         */
        private void openStepsDetailFragment(List<RecipeJson.Steps> steps, int pos) {
            clickedItemPos = pos;
            RecipeStepsDetailFragment fragment = RecipeStepsDetailFragment.newInstance(getBundle(steps, pos));
            fragment.setRetainInstance(true);
            if (isTabletLayoutUsed()) {
                hideNoClickItemEmptyView();
                ((AppCompatActivity) activity).getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.recipe_detail_fragments_container_right, fragment, BundleConstants.RECIPE_STEPS_DETAIL)
                        .commit();
            } else {
                RecipeStepsDetailFragment frag = (RecipeStepsDetailFragment) ((AppCompatActivity) activity).getSupportFragmentManager()
                        .findFragmentByTag(BundleConstants.RECIPE_STEPS_DETAIL);
                ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_recipe_list, fragment, BundleConstants.RECIPE_STEPS_DETAIL)
                        .commit();
                if (frag != null) {
                    ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction()
                            .remove(frag);
                    frag.onDestroy();
                }

            }
        }

        /**
         * This method checks if the layout resource file used is a for tablet size
         *
         * @return true if tablet resource used else false
         */
        boolean isTabletLayoutUsed() {
            return activity.findViewById(R.id.recipe_detail_fragments_container_right) != null;
        }

        /**
         * Getting the bundle, that is required by the RecipeStepsDetailFragments
         *
         * @param steps the object that contains steps to be used in the detail fragment
         * @param pos   the current position of the fragments, This is useful to keep track of the
         *              fragments opened when the user clicks previous or next button
         * @return a bundle
         */
        public Bundle getBundle(List<RecipeJson.Steps> steps, int pos) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(BundleConstants.RECIPE_STEPS_DETAIL, (ArrayList<? extends Parcelable>) steps);
            bundle.putInt(BundleConstants.CLICKED_ITEM_POSITION, pos);
            //TODO:Learn how to pass via Parcelable
            //bundle.putSerializable(BundleConstants.PREV_NEXT_BUTTON_CALL_BACK_KEY, this);
            //bundle.putBoolean(BundleConstants.IS_VIDEO, steps.getVideoUrl() != null);
            return bundle;
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

        @Override
        public int getItemCount() {
            if (stepsList == null) return 0;
            return stepsList.size();
        }

        public void setList(List<RecipeJson.Steps> list) {
            this.stepsList = list;
            notifyDataSetChanged();
        }

        /**
         * This method hides the view telling the user to click on an item to see steps detail
         * when in tablet devices
         */
        private void hideNoClickItemEmptyView() {
            ((RecipeDetailActivity) activity).getDetailActivityBinding()
                    .fragmentRecipeStepsNotClickedView.setVisibility(View.GONE);
        }

        /**
         * This method shows the view telling the user to click on an item to see steps detail
         * when in tablet devices
         */
        private void showNoClickItemEmptyView() {
            ((RecipeDetailActivity) activity).getDetailActivityBinding()
                    .fragmentRecipeStepsNotClickedView.setVisibility(View.VISIBLE);
        }

//        /**
//         * This method is called when next button is clickrd
//         *
//         * @param prevPos the previous position of
//         */
//        @Override
//        public void nextButtonClicked(int prevPos) {
//            navBottonClicked = true;
//            int newPos = 0;
//            if (!(prevPos + 1 > stepsList.size() - 1)) {
//                newPos = prevPos + 1;
//                openStepsDetailFragment(stepsList.get(newPos), newPos);
//            }
//        }
//
//        /**
//         * This method is called when previous button is clicked
//         *
//         * @param prevPos the previous position before previous button clicked
//         */
//        @Override
//        public void previousButtonClicked(int prevPos) {
//            navBottonClicked = true;
//            int newPos = 0;
//            if (!(prevPos - 1 < 0)) {
//                newPos = prevPos - 1;
//                openStepsDetailFragment(stepsList.get(newPos), newPos);
//            }
//        }

        //protected


        class RecipeStepsListViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            ImageView imageView;

            public RecipeStepsListViewHolder(View itemView) {
                super(itemView);
                textView = recipeStepsBinding.itemRecipeStepsText;
                imageView = recipeStepsBinding.itemRecipeStepsImage;
            }
        }
    }

    public static class RecipeIngredientListAdapter extends
            RecyclerView.Adapter<RecipeIngredientListAdapter.RecipeIngredientsListViewHolder> {
        private static final String TAG = RecipeIngredientListAdapter.class.getSimpleName();
        private List<RecipeJson.Ingredients> ingredientsList = new ArrayList<>();
        private ItemWidgetIngredientsBinding ingredientsBinding;

        public RecipeIngredientListAdapter(List<RecipeJson.Ingredients> ingredients) {
            super();
            ingredientsList = ingredients;
        }

        @NonNull
        @Override
        public RecipeIngredientsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ingredientsBinding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_widget_ingredients,
                            parent, false);
            return new RecipeIngredientsListViewHolder(ingredientsBinding.getRoot());
        }


        @Override
        public void onBindViewHolder(@NonNull RecipeIngredientsListViewHolder holder, int position) {
            RecipeJson.Ingredients ingredients = ingredientsList.get(holder.getAdapterPosition());
            holder.textView.setText(ingredients.getIngredient());
            Log.e(TAG, "Ingredient ---->>> " + ingredients.getIngredient());
        }

        @Override
        public int getItemCount() {
            return ingredientsList.size();
        }

        public void setList(List<RecipeJson.Ingredients> ingredients) {
            ingredientsList = ingredients;
            notifyDataSetChanged();
        }

        public class RecipeIngredientsListViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;

            public RecipeIngredientsListViewHolder(View itemView) {
                super(itemView);
                initViews();
            }

            void initViews() {
                ingredientsBinding.appwidgetFrameLayout.setLayoutParams(new FrameLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView = ingredientsBinding.appwidgetListItem;
            }
        }
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, ingredientsView, stepsView, servingsView;
        CircleImageView imageView;

        public RecipesViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            nameView = recipeCardBinding.itemRecipeTvName;
            ingredientsView = recipeCardBinding.itemRecipeTvIngredients;
            stepsView = recipeCardBinding.itemRecipeTvSteps;
            servingsView = recipeCardBinding.itemRecipeTvServings;
            imageView = recipeCardBinding.itemRecipeImage;
        }
    }
}
