package me.geeksploit.bakingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.geeksploit.bakingapp.adapter.RecipeGalleryAdapter;
import me.geeksploit.bakingapp.data.RecipeEntity;

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
    }
}
