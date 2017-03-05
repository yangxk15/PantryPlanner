package edu.dartmouth.cs.pantryplanner.app.model;

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

    public static String[] getMealTypes() {
        MealType[] mealTypes = MealType.values();
        String[] types = new String[mealTypes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = mealTypes[i].toString();
        }
        return types;
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