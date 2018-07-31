package me.geeksploit.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.geeksploit.bakingapp.adapter.RecipeGalleryAdapter;
import me.geeksploit.bakingapp.data.RecipeEntity;
import me.geeksploit.bakingapp.util.NetworkUtils;
import me.geeksploit.bakingapp.widget.IngredientsWidget;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecipeGalleryAdapter mRecipeGalleryAdapter;
    private List<RecipeEntity> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecipes = new ArrayList<>();
        mRecipeGalleryAdapter = new RecipeGalleryAdapter(this, mRecipes);
        RecyclerView recipeGallery = findViewById(R.id.recipe_gallery);
        recipeGallery.setAdapter(mRecipeGalleryAdapter);

        new AsyncTask<Void, Void, List<RecipeEntity>>() {

            @Override
            protected List<RecipeEntity> doInBackground(Void... voids) {
                List<RecipeEntity> recipes = new ArrayList<>();
                try {
                    URL url = NetworkUtils.buildRecipesUrl(getString(R.string.recipe_source_url));
                    recipes.addAll(NetworkUtils.getRecipes(url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return recipes;
            }

            @Override
            protected void onPostExecute(List<RecipeEntity> recipeEntities) {
                mRecipes.clear();
                mRecipes.addAll(recipeEntities);
                mRecipeGalleryAdapter.notifyDataSetChanged();
            }

        }.execute();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_widget_recipe_id_key))) {
            Intent intent = new Intent(this, IngredientsWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            this.sendBroadcast(intent);
        }
    }
}
