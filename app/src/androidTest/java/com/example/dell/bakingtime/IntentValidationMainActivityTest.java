package com.example.dell.bakingtime;


import android.app.Activity;
import android.app.Instrumentation;
import android.os.Parcelable;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.dell.bakingtime.main.MainActivity;
import com.example.dell.bakingtime.recipe.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class IntentValidationMainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;
    private IdlingRegistry idlingRegistry;
    private static final String TAG = IntentValidationMainActivityTest.class.getSimpleName();


    @Before
    public void registerIdlingResource(){
        idlingResource = intentsTestRule.getActivity().getIdlingResource();
        idlingRegistry = IdlingRegistry.getInstance();
        idlingRegistry.register(idlingResource);
    }

    @Before
    public void stubAllExternalIntents(){
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void intentValidationTest(){
        onView(withId(R.id.baking_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Recipe recipe = intentsTestRule.getActivity().getRecipeClicked();
        intended(allOf(hasExtra(MainActivity.INTENT_RECIPE, (Parcelable) recipe)));
    }

    @After
    public void unregisterIdlingResource(){
        idlingRegistry.unregister(idlingResource);
    }
}
