package edu.dartmouth.cs.pantryplanner.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by yangxk15 on 2/27/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class Recipe {
    String name;
    Map<Item, Integer> items;
    List<String> steps;

    @Override
    public String toString() {
        return new GsonBuilder().enableComplexMapKeySerialization().create().toJson(this);
    }

    public static Recipe fromString(String s) {
        return new Gson().fromJson(s, Recipe.class);
    }

    public static Recipe fromRecord(RecipeRecord recipeRecord) {
        return fromString(recipeRecord.getRecipe());
    }

    public static List<Recipe> fromRecipeRecordList(List<RecipeRecord> recipeRecords) {
        List<Recipe> recipes = new ArrayList<>(recipeRecords.size());

        for (RecipeRecord recipeRecord : recipeRecords) {
            recipes.add(fromRecord(recipeRecord));
        }

        return recipes;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + items.hashCode() + steps.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Recipe other = (Recipe) o;
        return name.equals(other.name) && items.equals(items) && steps.equals(steps);
    }

}
