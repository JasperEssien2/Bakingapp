package com.example.android.bakingapp.interfaces;

/**
 * This is used to update the Ui when request to get the recipe list or vice versa
 */
public interface ProgressbarCallbacks {

    /**
     * Call back method when a request is being made to get recipes list
     */
    void showLoadingProgressBar();

    /**
     * Call back to stop loading progress bar when request to get recipes fail or is completed
     */
    void stopLoadingProgressBar();

    /**
     * Call back to show a toast
     * @param toast message to be shown in the toast
     */
    void displayToast(String toast);
}
