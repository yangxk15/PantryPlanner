package edu.dartmouth.cs.pantryplanner.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.dartmouth.cs.pantryplanner.backend.entity.historyRecordApi.model.HistoryRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Created by yangxk15 on 3/5/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class MealPlan {
    Long id;
    Date date;
    MealType mealType;
    Recipe recipe;

    @Override
    public String toString() {
        return new GsonBuilder().enableComplexMapKeySerialization().create().toJson(this);
    }

    public static MealPlan fromString(String s) {
        return new Gson().fromJson(s, MealPlan.class);
    }

    public static List<MealPlan> fromMealPlanRecords(List<MealPlanRecord> mealPlanRecords) {
        List<MealPlan> mealPlans = new ArrayList<>(mealPlanRecords.size());

        for (MealPlanRecord mealPlanRecord : mealPlanRecords) {
            mealPlans.add(fromString(mealPlanRecord.getMealPlan()));
            mealPlans.get(mealPlans.size() - 1).setId(mealPlanRecord.getId());
        }

        return mealPlans;
    }

    public static List<MealPlan> fromHistoryRecords(List<HistoryRecord> historyRecords) {
        List<MealPlan> mealPlans = new ArrayList<>(historyRecords.size());

        for (HistoryRecord historyRecord : historyRecords) {
            mealPlans.add(fromString(historyRecord.getHistory()));
        }

        return mealPlans;
    }
}
