package com.example.baked;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.baked.Model.Recipe;
import com.example.baked.Utils.NetworkUtils;
import com.example.baked.ViewModel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    public static final String RECIPE_INTENT_KEY = "recipe";

    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private RecipeAdapter mRecipeAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorTextView;
    private ProgressBar mLoadingBar;
    private RecipeViewModel mViewModel;

    @Nullable public static SimpleIdlingResource mIdlingResource;

    @Nullable
    @VisibleForTesting
    public static IdlingResource getIdlingResource() {
        if (mIdlingResource == null)
            mIdlingResource = new SimpleIdlingResource();
        return mIdlingResource;
    }


    @Nullable
    @VisibleForTesting
    public List<Recipe> getRecipesForTesting() {
        return mRecipeList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.main_activity_recyclerview);
        mErrorTextView = findViewById(R.id.error_textview);
        mLoadingBar = findViewById(R.id.main_loading_bar);

        getIdlingResource();

        if (NetworkUtils.isOnline(this)) {
            mViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
            mViewModel.init();
            mIdlingResource.setIdleState(false);
            final Observer<List<Recipe>> recipeObserver = recipes -> {
                mRecipeList.addAll(recipes);
                mRecipeAdapter.notifyDataSetChanged();
            };
            mViewModel.getRecipeRepository().observe(this, recipeObserver);
            setUpRecyclerView();
        } else {
            showErrorMessage();
        }
    }

    private void setUpRecyclerView() {
        mLoadingBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        if (mRecipeAdapter == null) {
            mRecipeAdapter = new RecipeAdapter(this, this, mRecipeList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mRecipeAdapter);
        } else {
            mRecipeAdapter.notifyDataSetChanged();
        }
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(getText(R.string.no_network_connection));
    }

    @Override
    public void onClick(Recipe recipe) {
        mViewModel.setCurrentRecipe(recipe);
        Intent intentViewRecipe = new Intent(this, RecipeMasterActivity.class);
        intentViewRecipe.putExtra(RECIPE_INTENT_KEY, recipe);
        startActivity(intentViewRecipe);
    }

}