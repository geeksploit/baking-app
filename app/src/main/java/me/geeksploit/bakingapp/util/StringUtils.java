package me.geeksploit.bakingapp.util;

import java.util.Locale;

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
}
