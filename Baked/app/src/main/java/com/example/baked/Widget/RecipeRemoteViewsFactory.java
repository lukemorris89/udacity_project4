package com.example.baked.Widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.baked.Model.Ingredient;
import com.example.baked.Model.Recipe;
import com.example.baked.R;
import com.example.baked.RecipeMasterActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static Recipe mRecipe;

    private List<Ingredient> mIngredientsList;
    private Context mContext;
    private ArrayList<String> mIngredientsStrings = new ArrayList<>();

    public RecipeRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mRecipe = RecipeMasterActivity.widgetRecipe;
        if (mRecipe != null) {
            mIngredientsList = mRecipe.getIngredients();
            populateList();
        }
    }

    @Override
    public void onDestroy() {
        if (mIngredientsList != null) {
            mIngredientsList.clear();
        }
    }

    @Override
    public int getCount() {
        if (mIngredientsList == null) {
            return 0;
        }
        return mIngredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        String ingredient = mIngredientsStrings.get(position);
        remoteViews.setTextViewText(R.id.widget_ingredient_text, ingredient);

        Intent openActivityIntent = new Intent(mContext, RecipeMasterActivity.class);
        openActivityIntent.putExtra("recipe", mRecipe);
        remoteViews.setOnClickFillInIntent(R.id.widget_ingredient_text, openActivityIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void populateList() {
        for (Ingredient ingredient : mIngredientsList) {
            String ingredientName = ingredient.getIngredient();
            String ingredientMeasure = ingredient.getMeasure();
            double ingredientQuantity = ingredient.getQuantity();
            String widgetListText = ingredientName + ": " + ingredientQuantity + " " + ingredientMeasure;
            mIngredientsStrings.add(widgetListText);
        }
    }
}

