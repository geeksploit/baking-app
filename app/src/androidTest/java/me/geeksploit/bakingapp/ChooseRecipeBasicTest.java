package me.geeksploit.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ChooseRecipeBasicTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void displayRecipeGallery() {
        onView(withId(R.id.recipe_gallery)).check(matches(isDisplayed()));
    }

    @Test
    public void shownRecipeSteps() {
        onView(withId(R.id.recipe_gallery)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_gallery)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipestep_list)).check(matches(isDisplayed()));
    }

    @Test
    public void showRecipeFavoriteSnackbar() {
        onView(withId(R.id.recipe_gallery)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.snackbar_text)).check(matches(withText("Marked as favorite: Nutella Pie")));
    }
}
