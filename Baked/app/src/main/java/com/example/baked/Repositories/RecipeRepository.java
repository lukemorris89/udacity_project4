package com.example.baked.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.baked.Model.Recipe;
import com.example.baked.Utils.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private static RecipeRepository recipeRepository;

    public static RecipeRepository getInstance() {
        if (recipeRepository == null) {
            recipeRepository = new RecipeRepository();
        }
        return recipeRepository;
    }

    private RetrofitService.RecipeAPI recipeAPI;

    public RecipeRepository() {
        recipeAPI = RetrofitService.getRecipeService();
    }

    public MutableLiveData<List<Recipe>> getRecipeData() {
        final MutableLiveData<List<Recipe>> recipeData = new MutableLiveData<>();
        String path = "baking.json";

        recipeAPI.listRecipes(path).enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    recipeData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                recipeData.setValue(null);
            }
        });
        return recipeData;
    }
}
