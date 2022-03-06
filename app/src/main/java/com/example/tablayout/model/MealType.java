package com.example.tablayout.model;

import io.realm.RealmObject;

public class MealType extends RealmObject {

    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_RECIPE = "recipe";

    private String type;
    private Recipe recipe;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
