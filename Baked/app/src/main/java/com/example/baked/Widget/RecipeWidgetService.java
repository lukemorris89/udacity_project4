package com.example.baked.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new RecipeRemoteViewsFactory(this.getApplicationContext(), intent));
    }
}
