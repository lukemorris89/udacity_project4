package com.example.bakedv2;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.baked.MainActivity;
import com.example.baked.Model.Recipe;
import com.example.baked.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static org.hamcrest.Matchers.allOf;

public class MainActivityOpensRecipeMasterActivityTest {

    private List<Recipe> mRecipes;
    private IdlingResource mIdlingResource;

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mainActivityIntentRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Before
    public void getRecipes() {
        mRecipes = mainActivityIntentRule.getActivity().getRecipesForTesting();
    }

    @Test
    public void clickRecipe_OpensRecipeDetailActivity() {
        int recipeRecyclerViewPosition = 1;
        onView(ViewMatchers.withId(R.id.main_activity_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(recipeRecyclerViewPosition, click()));
        intended(allOf(hasExtra(MainActivity.RECIPE_INTENT_KEY, mRecipes.get(recipeRecyclerViewPosition))));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
