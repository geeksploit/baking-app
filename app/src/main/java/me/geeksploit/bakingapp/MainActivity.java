package me.geeksploit.bakingapp;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

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
    }
}
