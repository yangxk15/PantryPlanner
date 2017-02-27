package edu.dartmouth.cs.pantryplanner.pantryplanner.model;

import lombok.AllArgsConstructor;

/**
 * Created by yangxk15 on 2/27/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
public enum ItemType {
    MEAT("Meat", 7),
    DIARY("Diary", 1),
    FRUIT("Meat", 3),
    VEGETABLE("Meat", 5),
    INGREDIENT("Meat", 30),
    OTHER("Meat", 15);

    String text;
    int freshTime;

    @Override
    public String toString() {
        return text;
    }

    public String[] getItemTypes() {
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

}
