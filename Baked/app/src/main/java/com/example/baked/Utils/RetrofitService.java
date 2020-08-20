package com.example.baked.Utils;

import com.example.baked.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class RetrofitService {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public interface RecipeAPI {
        @GET
        Call<List<Recipe>> listRecipes(@Url String path);
    }

    public static RecipeAPI getRecipeService() {
        return retrofit.create(RecipeAPI.class);
    }
}
