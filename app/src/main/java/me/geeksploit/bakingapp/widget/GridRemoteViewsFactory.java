package me.geeksploit.bakingapp.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.geeksploit.bakingapp.R;
import me.geeksploit.bakingapp.data.IngredientEntity;
import me.geeksploit.bakingapp.data.RecipeEntity;
import me.geeksploit.bakingapp.util.NetworkUtils;
import me.geeksploit.bakingapp.util.PrefUtils;
import me.geeksploit.bakingapp.util.StringUtils;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    private List<IngredientEntity> mIngredients = new ArrayList<>();

    public GridRemoteViewsFactory(Context applicationContext) {
        this.mContext = applicationContext;
        mIngredients = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    /**
     * Note: expensive tasks can be safely performed synchronously within this method
     * In the interim, the old data will be displayed within the widget.
     */
    @Override
    public void onDataSetChanged() {
        try {
            URL url = NetworkUtils.buildRecipesUrl(mContext.getString(R.string.recipe_source_url));
            List<RecipeEntity> newRecipes = NetworkUtils.getRecipes(url);
            int favoriteRecipe = PrefUtils.getWidgetRecipeId(mContext);
            for (RecipeEntity recipe : newRecipes)
                if (recipe.getId() == favoriteRecipe) {
                    mIngredients = recipe.getIngredients();
                    break;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        IngredientEntity ingredient = mIngredients.get(i);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_list_content);
        views.setTextViewText(R.id.ingredients_widget_item_text, StringUtils.getIngredientDescription(
                ingredient.getQuantity(),
                ingredient.getMeasure(),
                ingredient.getIngredient())
        );

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
