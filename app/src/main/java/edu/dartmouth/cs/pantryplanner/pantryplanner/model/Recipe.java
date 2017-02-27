package edu.dartmouth.cs.pantryplanner.pantryplanner.model;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;

/**
 * Created by yangxk15 on 2/27/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
public class Recipe {
    Date mealDate;
    MealType mealType;
    Map<Item, Integer> items;
}
