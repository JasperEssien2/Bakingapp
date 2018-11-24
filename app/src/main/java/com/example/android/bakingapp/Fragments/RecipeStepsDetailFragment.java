package com.example.android.bakingapp.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Activities.RecipeDetailActivity;
import com.example.android.bakingapp.Constants.BundleConstants;
import com.example.android.bakingapp.POJO.RecipeJson;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utility.VideoPlayer;
import com.example.android.bakingapp.databinding.FragmentRecipeStepsDetailBinding;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepsDetailFragment extends Fragment {

    private static final String TAG = RecipeStepsDetailFragment.class.getSimpleName();
    FragmentRecipeStepsDetailBinding recipeStepsDetailBinding;
    VideoPlayer videoPlayer;
    private boolean isVideoPlayer = false;
    private int currentClickedPosition = 0;

    public RecipeStepsDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //if(null != savedInstanceState) return null;
        // Inflate the layout for this fragment
        recipeStepsDetailBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_recipe_steps_detail, container, false);
        //return inflater.inflate(R.layout.fragment_recipe_steps_detail, container, false);
        if (savedInstanceState != null && savedInstanceState.containsKey(BundleConstants.CLICKED_ITEM_POSITION)) {
            if (videoPlayer != null) {
                videoPlayer.setCurrentWindow(savedInstanceState.getInt(BundleConstants.CURRENT_WINDOW));
                videoPlayer.setPlayBackPosition(savedInstanceState.getLong(BundleConstants.PLAY_BACK_POSITION));
                videoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(BundleConstants.PLAY_WHEN_READY));
            }
            pos = savedInstanceState.getInt(BundleConstants.CLICKED_ITEM_POSITION);
            Log.e(TAG, "POSITION AFTER VIEW RESTORED " + pos);
            initViews(true);

        } else initViews(false);
        return recipeStepsDetailBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isVideoPlayer && videoPlayer != null) {
            if (Util.SDK_INT > 23) {
                videoPlayer.initializePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        if (isVideoPlayer && videoPlayer != null) {
            videoPlayer.hideSystemUi();
            if ((Util.SDK_INT <= 23 || videoPlayer.getSimpleExoPlayer() == null)) {
                videoPlayer.initializePlayer();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isVideoPlayer && videoPlayer != null) {
            if (Util.SDK_INT <= 23) {
                videoPlayer.releasePlayer();
            }
        }
        super.onPause();
    }

    @Override
    public void onDetach() {
        if (isVideoPlayer && videoPlayer != null) {
            videoPlayer.releasePlayer();
        }
        super.onDetach();
    }

    @Override
    public void onStop() {
        if (isVideoPlayer && videoPlayer != null) {
            if (Util.SDK_INT > 23) {
                videoPlayer.releasePlayer();
            }
        }
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoPlayer != null)
            videoPlayer.releasePlayer();
    }

    public static RecipeStepsDetailFragment newInstance(Bundle args) {
        RecipeStepsDetailFragment fragment = new RecipeStepsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method gets the steps of the item clicked from the bundle passed to the fragment and
     * returns it
     *
     * @return List<RecipeJson.Steps></RecipeJson.Steps>
     */
    private List<RecipeJson.Steps> getStepsList() {
        //TODO: Pass in the list of steps in the prceable instead of a single item
        //TODO: Then control the next and previous buttons right from the fragment here
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BundleConstants.RECIPE_STEPS_DETAIL)) {
            return (bundle.getParcelableArrayList(BundleConstants.RECIPE_STEPS_DETAIL));
        }
        return null;
    }

    /**
     * This method gets the steps of the item clicked from the bundle passed to the fragment and
     * returns it
     *
     * @return RecipeJson.Steps
     */
    private RecipeJson.Steps getSteps() {
        return getStepsList().get(pos);
    }

    /**
     * This methods gets the position of the item clicked from the bundle passed to the fragment
     * and returns it
     *
     * @return int
     */
    private int getClickedPosition() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BundleConstants.CLICKED_ITEM_POSITION)) {
            return bundle.getInt(BundleConstants.CLICKED_ITEM_POSITION);
        }
        return 0;
    }


    /**
     * This method is called when next button is clickrd
     *
     * @param prevPos the previous position of
     */
    public void nextButtonClicked(int prevPos) {
        int newPos = 0;
        if (!(prevPos + 1 > getStepsList().size() - 1)) {
            newPos = prevPos + 1;
            pos = newPos;
            openNextFragment(newPos);
        }
    }

    /**
     * This method is called when previous button is clicked
     *
     * @param prevPos the previous position before previous button clicked
     */
    public void previousButtonClicked(int prevPos) {
        int newPos = 0;
        if (!(prevPos - 1 < 0)) {
            newPos = prevPos - 1;
            pos = newPos;
            openNextFragment(newPos);
        }


    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
        if (videoPlayer == null) return;
        outState.putInt(BundleConstants.CURRENT_WINDOW, videoPlayer.getCurrentWindow());
        outState.putLong(BundleConstants.PLAY_BACK_POSITION, videoPlayer.getPlayBackPosition());
        outState.putBoolean(BundleConstants.PLAY_WHEN_READY, videoPlayer.isPlayWhenReady());
        outState.putInt(BundleConstants.CLICKED_ITEM_POSITION, pos);
    }

    /**
     * This method opens up a new fragment passing in the details according to the position selected
     *
     * @param pos of item
     */
    private void openNextFragment(int pos) {
        if (getActivity().getSupportFragmentManager() == null) return;
        RecipeStepsDetailFragment frag = (RecipeStepsDetailFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag(BundleConstants.RECIPE_STEPS_DETAIL);
        RecipeStepsDetailFragment newFragment = RecipeStepsDetailFragment.newInstance(getBundle(getStepsList(), pos));
        if (frag != null) {

            if (videoPlayer != null) {
                videoPlayer.releasePlayer();
                Log.e(TAG, "VIdeo Player Released ooooooooh");
                videoPlayer = null;
            }
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(frag);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_recipe_steps_detail, newFragment, BundleConstants.RECIPE_STEPS_DETAIL)
                    .commit();
        }
    }

    public void onClicked(View view) {
        //if(view.getId() == id of the x button){ //Where id i
        //Then create a new intent passing in the Extra then in the extra pass in true
        //Which means the the x button was clicked
        //}else if(view.getId == id of 'o' button){
        //Then create a new intent passing in the Extra then in the extra pass in true
        //Which means the the x button was clicked
        //}

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
        return bundle;
    }

    int pos = getClickedPosition();

    private void initViews(boolean containsSavedInstance) {
        if (!containsSavedInstance) pos = getClickedPosition();
        ((RecipeDetailActivity) getActivity()).hideFab();
        currentClickedPosition = pos;
        Log.e(TAG, "POSITION _____ " + pos);
        RecipeJson.Steps steps = getStepsList().get(pos);
        if (steps == null) {
            Log.e(TAG, "STEP IS NULL OOOH _____ ");
            return;
        }
        Log.e(TAG, "STEP IS NOT OOOH _____ ");

        RecipeListFragment frag = (RecipeListFragment) ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                .findFragmentByTag(BundleConstants.RECIPE_LIST_FRAGMENT_STEPS);

//        if (frag == null) Log.e(TAG, "List Fragment is Null");
//        else Log.e(TAG, "List Fragment NOT Null");

        recipeStepsDetailBinding.recipeStepsDetailNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (prevNextButtonsCallback == null) return;
//                prevNextButtonsCallback.nextButtonClicked(pos);
                nextButtonClicked(pos);
            }
        });

        recipeStepsDetailBinding.recipeStepsDetailPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (prevNextButtonsCallback == null) return;
//                prevNextButtonsCallback.previousButtonClicked(pos);
                previousButtonClicked(pos);
            }
        });
        recipeStepsDetailBinding.recipeLabelStepsTv.setText(steps.getDescription());
        if (steps.getVideoUrl() == null || steps.getVideoUrl().isEmpty()) {
            hideVideoAndShowImageLayout();
            videoPlayer = null;
            isVideoPlayer = false;
            if (getSteps().getThumbnailUrl() != null && !getSteps().getThumbnailUrl().isEmpty()) {
                Picasso.get()
                        .load(getSteps().getThumbnailUrl())
                        .into(recipeStepsDetailBinding.recipeStepsMediaPlay);
            } else
                Picasso.get()
                        .load("https://i.imgur.com/8XpQ6yP.jpg")
                        .into(recipeStepsDetailBinding.recipeStepsMediaPlay);
        } else if (steps.getVideoUrl() != null) {
            if (!steps.getVideoUrl().isEmpty()) {
                videoPlayer = new VideoPlayer(recipeStepsDetailBinding.getRoot().getContext(), getSteps().getVideoUrl(), (PlayerView)
                        recipeStepsDetailBinding.recipeStepsMediaPlayVideo.findViewById(R.id.video_player),
                        null);
                hideImageAndShowVideoLayout();
                isVideoPlayer = true;
            } else {
                hideVideoAndShowImageLayout();
                isVideoPlayer = false;
                videoPlayer = null;
            }
        }
    }

//    private String getImageUrl(int pos) {
//        switch (pos) {
//            case 0:
//                return "http://arlingtonhousewife.files.wordpress.com/2011/02/nutella-pie-square.jpg";
//            case 1:
//                return "https://image.shutterstock.com/image-photo/delicious-chocolate-cakes-on-table-260nw-275513057.jpg";
//            case 2:
//                return "https://d2gk7xgygi98cy.cloudfront.net/14-3-large.jpg";
//            case 3:
//                return "http://assets.kraftfoods.com/recipe_images/PHILADELPHIA_Classic_Cheesecake.jpg";
//            default:
//                return "https://i.imgur.com/8XpQ6yP.jpg";
//        }
//    }

    private void hideImageAndShowVideoLayout() {
        recipeStepsDetailBinding.recipeStepsMediaPlay.setVisibility(View.GONE);
        recipeStepsDetailBinding.recipeStepsMediaPlayVideo.setVisibility(View.VISIBLE);
    }

    private void hideVideoAndShowImageLayout() {
        recipeStepsDetailBinding.recipeStepsMediaPlay.setVisibility(View.VISIBLE);
        recipeStepsDetailBinding.recipeStepsMediaPlayVideo.setVisibility(View.GONE);
    }
}
