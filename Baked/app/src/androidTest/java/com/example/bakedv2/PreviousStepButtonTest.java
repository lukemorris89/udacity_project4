package com.example.bakedv2;

import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.baked.MainActivity;
import com.example.baked.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class PreviousStepButtonTest {

    private IdlingResource mIdlingResource;

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mainActivityIntentRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void clickPreviousStep_OpensPreviousStep() {
        int recipeRecyclerViewPosition = 2;
        int stepRecyclerViewPosition = 1;

        onView(withId(R.id.main_activity_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(recipeRecyclerViewPosition, click()));
        onView(withId(R.id.master_list_steps_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(stepRecyclerViewPosition, click()));
        onView(withId(R.id.previous_step_button)).perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(R.string.introduction)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
