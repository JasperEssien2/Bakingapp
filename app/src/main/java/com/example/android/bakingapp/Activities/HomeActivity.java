package com.example.android.bakingapp.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.bakingapp.Adapters.RecipesAdapter;
import com.example.android.bakingapp.POJO.RecipeJson;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ViewModel.RecipeViewModel;
import com.example.android.bakingapp.databinding.ActivityHomeBinding;
import com.example.android.bakingapp.interfaces.ProgressbarCallbacks;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ProgressbarCallbacks{
    private static final String TAG = HomeActivity.class.getSimpleName();
    private ActivityHomeBinding activityHomeBinding;
    private RecyclerView mRecyclerView;
    private RecipesAdapter mRecipesAdapter;
    //For Tablet screen size view
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private RecipeViewModel mRecipeViewModel;
    private LiveData<List<RecipeJson>> recipeListLiveData;
    private Observer<List<RecipeJson>> recipeListLveDataObserver;
    //TODO: Remember to add a code that check this
    private boolean isTabletSize = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_home);
        //setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecipeViewModel = ViewModelProviders
                .of(this)
                .get(RecipeViewModel.class);
        mRecipeViewModel.setProgressbarCallbacks(this);
        //initLiveData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUpRecyclerView();
        initLiveData();
    }

    void initLiveDataObserver(){
        recipeListLveDataObserver = new Observer<List<RecipeJson>>() {
            @Override
            public void onChanged(@Nullable List<RecipeJson> recipeJsons) {
                mRecipesAdapter.addRecipeList(recipeJsons);
                Log.e(TAG, "Data Observed ooooooh");
                Log.e(TAG, "New List ooh ----- " + recipeJsons.toString());
            }
        };
    }

    /**
     * This method initializes and observe the liveData
     */
    void initLiveData(){
        initLiveDataObserver();
        mRecipeViewModel.getRecipeListLiveData()
                .observe(this, recipeListLveDataObserver);
    }

    /**
     * This method sets up the recycleView, check to see if its a tablet size or not,
     * then use gridLayout if tablet size else it use LinearLayoutManager
     */
    void setUpRecyclerView(){
        mRecyclerView = activityHomeBinding.appBar.content.recipeCardListRecyclerView;
        mRecipesAdapter = new RecipesAdapter(new ArrayList<RecipeJson>(), this);
        mRecyclerView.setAdapter(mRecipesAdapter);
        Log.e(TAG, "RecyclerView Setup ooooooh ");
        if(!isTabletSize){
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }else{
            mGridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
        }
    }

    @Override
    public void showLoadingProgressBar() {

    }

    @Override
    public void stopLoadingProgressBar() {

    }

    @Override
    public void displayToast(String toast) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
