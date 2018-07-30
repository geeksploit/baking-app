package me.geeksploit.bakingapp.data;

import java.io.Serializable;

public final class IngredientEntity implements Serializable {
    private float quantity;
    private String measure;
    private String ingredient;

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
