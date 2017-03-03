package edu.dartmouth.cs.pantryplanner.backend.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

import edu.dartmouth.cs.pantryplanner.common.MealType;
import edu.dartmouth.cs.pantryplanner.common.Recipe;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yangxk15 on 3/3/17.
 */

@Entity
@NoArgsConstructor
@Data
public class RecipeRecord {
    @Id
    Long id;

    Date date;
    String mealType;
    Recipe recipe;

    public RecipeRecord(Date date, String mealType, Recipe recipe) {
        this.date = date;
        this.mealType = mealType;
        this.recipe = recipe;
    }
}
