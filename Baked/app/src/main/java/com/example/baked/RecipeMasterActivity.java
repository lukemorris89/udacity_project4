package com.example.baked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.baked.Model.Ingredient;
import com.example.baked.Model.Recipe;
import com.example.baked.Model.Step;
import com.example.baked.Utils.RecipeViewModel;
import com.example.baked.Widget.RecipeWidgetProvider;

import java.util.List;

public class RecipeMasterActivity extends AppCompatActivity implements StepsAdapter.StepAdapterOnClickHandler {

    public static Recipe widgetRecipe;
    private boolean mTwoPane;
    private String mRecipeName;
    private List<Ingredient> mIngredientsList;
    private List<Step> mStepsList;
    private Recipe mCurrentRecipe;
    private RecipeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_master);
        mViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        Intent intent = getIntent();
        mViewModel.setCurrentRecipe(intent.getParcelableExtra("recipe"));
        mCurrentRecipe = mViewModel.getCurrentRecipe();

        if (findViewById(R.id.step_container) != null) {
            mTwoPane = true;
        }
        widgetRecipe = mViewModel.getCurrentRecipe();
        if (mCurrentRecipe != null) {
            mRecipeName = mCurrentRecipe.getName();
            mIngredientsList = mCurrentRecipe.getIngredients();
            mStepsList = mCurrentRecipe.getSteps();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipeName);
        }
        setUpRecyclerViews();

        Intent widgetIntent = new Intent(this, RecipeWidgetProvider.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), RecipeWidgetProvider.class));
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        widgetIntent.putExtra("recipe", mCurrentRecipe);
        sendBroadcast(widgetIntent);
        AppWidgetManager.getInstance(getApplication()).notifyAppWidgetViewDataChanged(ids, R.id.widget_list_view);
    }

    private void setUpRecyclerViews() {
        RecyclerView ingredientsRecyclerView = findViewById(R.id.master_list_ingredients_recyclerview);
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(mIngredientsList);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ingredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);

        RecyclerView stepsRecyclerView = findViewById(R.id.master_list_steps_recyclerview);
        StepsAdapter stepsAdapter = new StepsAdapter(this,this, mStepsList);
        stepsRecyclerView.setAdapter(stepsAdapter);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(stepsLayoutManager);
    }

    @Override
    public void onClick(Step step) {
        mViewModel.setCurrentStep(step);
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("step", step);
            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, fragment)
                    .commit();
        } else {
            Intent intentViewStep = new Intent(this, StepDetailsActivity.class);
            intentViewStep.putExtra("step", step);
            intentViewStep.putExtra("recipe", mCurrentRecipe);
            startActivity(intentViewStep);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}