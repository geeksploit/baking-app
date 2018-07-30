package me.geeksploit.bakingapp.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import me.geeksploit.bakingapp.data.RecipeEntity;

public final class NetworkUtils {

    public static URL buildRecipesUrl(String recipesUrl) throws MalformedURLException {
        return new URL(recipesUrl);
    }

    public static List<RecipeEntity> getRecipes(URL url) throws IOException {
        Type listType = new TypeToken<List<RecipeEntity>>() {
        }.getType();
        return new Gson().fromJson(new InputStreamReader(url.openStream()), listType);
    }
}
