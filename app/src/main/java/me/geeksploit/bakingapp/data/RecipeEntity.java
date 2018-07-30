package me.geeksploit.bakingapp.data;

import java.io.Serializable;
import java.util.List;

public final class RecipeEntity implements Serializable {

    private int id;
    private String name;
    private List<IngredientEntity> ingredients;
    private List<StepEntity> steps;
    private int servings;
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public List<StepEntity> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
