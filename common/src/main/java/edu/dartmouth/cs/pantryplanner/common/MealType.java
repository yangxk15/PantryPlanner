package edu.dartmouth.cs.pantryplanner.common;

/**
 * Created by yangxk15 on 3/3/17.
 */

public enum MealType {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner");

    String text;
    MealType(String text) {
        this.text = text;
    }

    public static MealType fromString(String s) {
        for (MealType mealType : MealType.values()) {
            if (mealType.toString().equalsIgnoreCase(s)) {
                return mealType;
            }
        }

        return null;
    }
}