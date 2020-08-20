package com.example.baked.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.example.baked.MainActivity;
import com.example.baked.Model.Ingredient;
import com.example.baked.Model.Recipe;
import com.example.baked.R;
import com.example.baked.RecipeMasterActivity;

import java.util.List;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static Recipe mRecipe;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            if (mRecipe != null) {
                views.setTextViewText(R.id.widget_recipe_name, mRecipe.getName());
                views.setViewVisibility(R.id.widget_list_view, View.VISIBLE);
                views.setViewVisibility(R.id.widget_home_textview, View.GONE);

                Intent intentListView = new Intent(context, RecipeWidgetService.class);
                intentListView.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                intentListView.setData(Uri.parse(intentListView.toUri(Intent.URI_INTENT_SCHEME)));
                views.setRemoteAdapter(appWidgetId, R.id.widget_list_view, intentListView);
                views.setEmptyView(R.id.widget_list_view, R.id.empty_list_view);


                Intent openActivityIntent = new Intent(context, RecipeMasterActivity.class);
                openActivityIntent.putExtra("recipe", mRecipe);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widget_recipe_linear_layout, pendingIntent);
                views.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);
            } else {
                setWidgetToHomeScreen(context, views);
            }
            // Instruct the widget manager to update the widget
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mRecipe = intent.getParcelableExtra("recipe");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        for (int appWidgetId: appWidgetIds) {
            updateListView(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
        }
        super.onReceive(context, intent);
    }

    public RemoteViews updateListView(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        Intent serviceIntent = new Intent(context, RecipeWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
        return remoteViews;
    }

    private void setWidgetToHomeScreen(Context context, RemoteViews views) {
        views.setViewVisibility(R.id.widget_list_view, View.GONE);
        views.setViewVisibility(R.id.widget_home_textview, View.VISIBLE);

        views.setTextViewText(R.id.widget_recipe_name, context.getText(R.string.app_name));
        views.setTextViewText(R.id.widget_home_textview, context.getString(R.string.widget_view_recipes));

        Intent openActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openActivityIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_recipe_linear_layout, pendingIntent);
    }
}

