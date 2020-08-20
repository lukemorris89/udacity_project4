package com.example.baked.Utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.baked.Model.Recipe;
import com.example.baked.Model.Step;
import com.example.baked.Repositories.RecipeRepository;

import java.util.List;

public class RecipeViewModel  extends ViewModel {

    private MutableLiveData<List<Recipe>> mutableLiveData;
    private RecipeRepository recipeRepository;
    private Recipe currentRecipe;
    private Step currentStep;
    private int countCurrentRecipeSteps;
    private long playerPosition;
    private boolean playerPlayWhenReady;

    public void init() {
        if (mutableLiveData != null) {
            return;
        }
        recipeRepository = RecipeRepository.getInstance();
        mutableLiveData = recipeRepository.getRecipeData();
    }

    public LiveData<List<Recipe>> getRecipeRepository() {
        return mutableLiveData;
    }

    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(Recipe recipe) {
        currentRecipe = recipe;
    }

    public Step getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Step step) {
        currentStep = step;
    }

    public int getCountCurrentRecipeSteps() {
        return countCurrentRecipeSteps;
    }

    public void setCountCurrentRecipeSteps(int count) {
        countCurrentRecipeSteps = count;
    }

    public long getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(long position) {
        playerPosition = position;
    }

    public boolean getPlayerPlayWhenReady() {
        return playerPlayWhenReady;
    }

    public void setPlayerPlayWhenReady(boolean playWhenReady) {
        playerPlayWhenReady = playWhenReady;
    }
}
