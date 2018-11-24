package com.example.android.bakingapp.Utility;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.Adapters.IngredientsRemoveViewService;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoveViewService(this.getApplicationContext(), intent);
    }
}