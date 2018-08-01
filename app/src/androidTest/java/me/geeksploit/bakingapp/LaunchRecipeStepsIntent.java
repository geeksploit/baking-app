package me.geeksploit.bakingapp;


import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LaunchRecipeStepsIntent {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);


    @Test
    public void validateIntentSentTo_RecipeStepListActivity() {
        Matcher<Intent> expectedIntent = IntentMatchers.hasExtraWithKey(RecipeStepListActivity.EXTRA_RECIPE);

        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));

        onView(withId(R.id.recipe_gallery)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_gallery))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(expectedIntent);
    }
}
