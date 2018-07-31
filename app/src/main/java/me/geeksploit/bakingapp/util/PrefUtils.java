package me.geeksploit.bakingapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.geeksploit.bakingapp.R;

public final class PrefUtils {

    private static SharedPreferences getPrefs(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static void setWidgetRecipe(Context c, int newId, String newName) {
        getPrefs(c).edit()
                .putInt(c.getString(R.string.pref_widget_recipe_id_key), newId)
                .putString(c.getString(R.string.pref_widget_recipe_name_key), newName)
                .apply();
    }

    public static int getWidgetRecipeId(Context c) {
        return getPrefs(c).getInt(c.getString(R.string.pref_widget_recipe_id_key), 0);
    }

    public static String getWidgetRecipeName(Context c) {
        return getPrefs(c).getString(c.getString(R.string.pref_widget_recipe_name_key),
                c.getString(R.string.appwidget_text));
    }
}
