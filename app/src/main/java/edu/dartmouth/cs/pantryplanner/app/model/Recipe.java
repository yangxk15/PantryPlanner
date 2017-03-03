package edu.dartmouth.cs.pantryplanner.app.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by yangxk15 on 2/27/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class Recipe {
    String name;
    Date mealDate;
    MealType mealType;
    Map<Item, Integer> items;
    List<String> steps;
}
