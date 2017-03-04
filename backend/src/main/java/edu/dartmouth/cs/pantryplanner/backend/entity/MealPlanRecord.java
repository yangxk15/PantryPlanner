package edu.dartmouth.cs.pantryplanner.backend.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yangxk15 on 3/3/17.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanRecord {
    @Id
    Long id;

    String email;
    String date;
    String mealType;
    String recipe;
}
