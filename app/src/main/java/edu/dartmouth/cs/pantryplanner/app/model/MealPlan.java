package edu.dartmouth.cs.pantryplanner.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by yangxk15 on 3/5/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class MealPlan {
    Date date;
    MealType mealType;
    Recipe recipe;

    @Override
    public String toString() {
        return new GsonBuilder().enableComplexMapKeySerialization().create().toJson(this);
    }

    public static MealPlan fromMealPlanRecord(MealPlanRecord mealPlanRecord) {
        return new Gson().fromJson(mealPlanRecord.getMealPlan(), MealPlan.class);
    }

    public static List<MealPlan> fromMealPlanRecords(List<MealPlanRecord> mealPlanRecords) {
        List<MealPlan> mealPlans = new ArrayList<>(mealPlanRecords.size());

        for (MealPlanRecord mealPlanRecord : mealPlanRecords) {
            mealPlans.add(fromMealPlanRecord(mealPlanRecord));
        }

        return mealPlans;
    }
}
