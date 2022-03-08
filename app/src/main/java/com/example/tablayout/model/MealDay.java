package com.example.tablayout.model;


import io.realm.RealmList;
import io.realm.RealmObject;

public class MealDay extends RealmObject {

    public static final String PROPERTY_DATE = "date";
    public static final String PROPERTY_MEALS = "meals";

    private String date;
    private RealmList<MealType> meals;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<MealType> getMeals() {
        return meals;
    }

    public void setMeals(RealmList<MealType> meals) {
        this.meals = meals;
    }

    public void addMeal(MealType mealType) {
        meals.add(mealType);
    }
}
