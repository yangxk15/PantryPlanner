package edu.dartmouth.cs.pantryplanner.app.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ItemType {
    MEAT("Meat", 7),
    DIARY("Diary", 1),
    FRUIT("Fruit", 3),
    VEGETABLE("Vegetable", 5),
    INGREDIENT("Ingredient", 30),
    OTHER("Other", 15);

    String text;
    int freshTime;

    @Override
    public String toString() {
        return text;
    }

    public static String[] getItemTypes() {
        ItemType[] itemTypes = ItemType.values();
        String[] types = new String[itemTypes.length];

        for (int i = 0; i < types.length; i++) {
            types[i] = itemTypes[i].toString();
        }

        return types;
    }

    public int getFreshTime() {
        return freshTime;
    }

    public static ItemType fromString(String s) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.toString().equalsIgnoreCase(s)) {
                return itemType;
            }
        }

        return null;
    }


}