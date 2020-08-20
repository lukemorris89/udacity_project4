package com.example.baked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.baked.Model.Recipe;
import com.example.baked.Utils.NetworkUtils;
import com.example.baked.Utils.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private RecipeAdapter mRecipeAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorTextView;
    private ProgressBar mLoadingBar;
    private RecipeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.main_activity_recyclerview);
        mErrorTextView = findViewById(R.id.error_textview);
        mLoadingBar = findViewById(R.id.main_loading_bar);

        if (NetworkUtils.isOnline(this)) {
            mViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
            mViewModel.init();
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
        mViewModel.setCountCurrentRecipeSteps(recipe.getSteps().size());
        Intent intentViewRecipe = new Intent(this, RecipeMasterActivity.class);
        intentViewRecipe.putExtra("recipe", recipe);
        startActivity(intentViewRecipe);
    }
}