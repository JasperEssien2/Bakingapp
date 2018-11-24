package com.example.android.bakingapp.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.bakingapp.POJO.RecipeJson;
import com.example.android.bakingapp.Rest.ApiClient;
import com.example.android.bakingapp.Rest.ApiInterface;
import com.example.android.bakingapp.interfaces.ProgressbarCallbacks;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeViewModel extends ViewModel {
    private String TAG = RecipeViewModel.class.getSimpleName();
    private MutableLiveData<List<RecipeJson>> mutableLiveData;
    private ProgressbarCallbacks progressbarCallbacks;

    /**
     * This method initializes the progressBarCallbacks
     * @param progressbarCallbacks the callback object
     */
    public void setProgressbarCallbacks(ProgressbarCallbacks progressbarCallbacks){
        this.progressbarCallbacks = progressbarCallbacks;
    }

    /**
     * A method that handles the call to get recipes list and update the Ui
     */
    public void getRecipeItems(){
        Log.e(TAG, "getRecipeItems() CAAALLEED!!!");
        progressbarCallbacks.showLoadingProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<RecipeJson>> call = apiService
                .getRecipes();

        call.enqueue(new Callback<List<RecipeJson>>() {
            @Override
            public void onResponse(Call<List<RecipeJson>> call, Response<List<RecipeJson>> response) {
                progressbarCallbacks.stopLoadingProgressBar();
                List<RecipeJson> recipeJsonList = response.body();
                if(recipeJsonList == null){
                    progressbarCallbacks.displayToast("Could not get recipe list");
                    Log.e(TAG, "Response Is Null oooooh");
                    return;
                }
                Log.e(TAG, "Response Is Not Null oooooh");
                mutableLiveData.setValue(recipeJsonList);
            }

            @Override
            public void onFailure(Call<List<RecipeJson>> call, Throwable t) {
                Log.e(TAG, "Failed -- " + t.getMessage());
                progressbarCallbacks.stopLoadingProgressBar();
                progressbarCallbacks.displayToast(t.getMessage());
            }
        });
    }

    public LiveData<List<RecipeJson>> getRecipeListLiveData() {
        if(mutableLiveData == null){
            mutableLiveData = new MutableLiveData<>();
            getRecipeItems();
        }
        return mutableLiveData;
    }
}
