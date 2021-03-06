package me.geeksploit.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.geeksploit.bakingapp.data.IngredientEntity;
import me.geeksploit.bakingapp.util.StringUtils;

/**
 * An activity representing a Recipe Ingredients detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepListActivity}.
 */
public class RecipeIngredientsActivity extends AppCompatActivity {

    List<IngredientEntity> mIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIngredients != null && mIngredients.size() > 0) {
                    String groceryList = StringUtils.getIngredientsGroceryList(getApplicationContext(), getTitle().toString(), mIngredients);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, groceryList);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    return;
                }
                Snackbar.make(view, R.string.message_error_no_ingredients, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            mIngredients = (List<IngredientEntity>) getIntent().getSerializableExtra(IngredientFragment.ARG_INGREDIENTS_LIST);
            Bundle arguments = new Bundle();
            arguments.putSerializable(IngredientFragment.ARG_INGREDIENTS_LIST,
                    getIntent().getSerializableExtra(IngredientFragment.ARG_INGREDIENTS_LIST));
            IngredientFragment fragment = new IngredientFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipestep_detail_container, fragment)
                    .commit();
        }

        if (getIntent().hasExtra(Intent.EXTRA_TITLE)) {
            String recipeName = getIntent().getStringExtra(Intent.EXTRA_TITLE);
            setTitle(getString(R.string.title_recipe_ingredients_template, recipeName));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, RecipeStepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
