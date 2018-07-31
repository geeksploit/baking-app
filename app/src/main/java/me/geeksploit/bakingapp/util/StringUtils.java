package me.geeksploit.bakingapp.util;

import android.content.Context;

import java.util.List;
import java.util.Locale;

import me.geeksploit.bakingapp.R;
import me.geeksploit.bakingapp.data.IngredientEntity;

public final class StringUtils {

    public static String getIngredientId(int position) {
        return String.format(Locale.getDefault(),
                "%d) ",
                position + 1
        );
    }

    public static String getStepId(int position) {
        return String.format(Locale.getDefault(),
                "%d) ",
                position + 1
        );
    }

    public static String getIngredientDescription(float quantity, String measure, String ingredient) {
        if (quantity == (int) quantity) {
            return String.format(Locale.getDefault(),
                    "%d %s of %s",
                    (int) quantity,
                    measure,
                    ingredient);
        } else {
            return String.format(Locale.getDefault(),
                    "%s %s of %s",
                    quantity,
                    measure,
                    ingredient);
        }
    }

    public static String getIngredientsGroceryList(Context c, String title, List<IngredientEntity> ingredients) {
        StringBuilder sb = new StringBuilder();
        sb.append(c.getString(R.string.text_grocery_list, title));
        for (IngredientEntity item : ingredients) {
            String ingredientDescription = StringUtils.getIngredientDescription(item.getQuantity(), item.getMeasure(), item.getIngredient());
            sb.append(System.getProperty("line.separator"));
            sb.append(ingredientDescription);
        }
        return sb.toString();
    }
}
