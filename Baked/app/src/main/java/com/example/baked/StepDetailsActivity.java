package com.example.baked;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.baked.Model.Step;
import com.example.baked.ViewModel.RecipeViewModel;

public class StepDetailsActivity extends AppCompatActivity {

    private StepDetailsFragment mStepDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        Intent intent = getIntent();
        viewModel.setCurrentRecipe(intent.getParcelableExtra(MainActivity.RECIPE_INTENT_KEY));
        viewModel.setCurrentStep(intent.getParcelableExtra(RecipeMasterActivity.STEP_INTENT_KEY));

        Step step = viewModel.getCurrentStep();
        String title;
        int stepId;
        if (step != null) {
            stepId = step.getId();
        } else {
            stepId = 0;
        }

        if (stepId == 0) {
            title = getString(R.string.introduction);
        } else {
            title = getString(R.string.step, String.valueOf(step.getId()));
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

        mStepDetailsFragment = new StepDetailsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container, mStepDetailsFragment)
                .commit();
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "step_details_fragment", mStepDetailsFragment);
    }
}