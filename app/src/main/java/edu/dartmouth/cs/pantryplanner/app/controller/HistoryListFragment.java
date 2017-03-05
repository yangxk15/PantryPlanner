package edu.dartmouth.cs.pantryplanner.app.controller;


import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.MealPlan;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.MealPlanRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryListFragment extends Fragment {

    private ArrayList<MealPlanRecord> mealPlanRecords;

//    String[] values = new String[]{"Banana Oatmeal Muffin",
//            "Shrimp Pesto Pasta",
//            "Broccoli Beef",
//            "Honey Mustard Chicken and Avacado Salad ",
//            "Healthier Chicken Alfredo Pasta",
//            "Easy Breakfast Frittata",
//            "Chocolate Strawberry Cream Puffs ",
//            "Fluffy Japanese Cheesecake "
//    };
    private String[] recipeStrings = new String[mealPlanRecords.size()];


    public HistoryListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView_history_list);
        for (int i = 0; i < mealPlanRecords.size(); i++){
            recipeStrings[i] = mealPlanRecords.get(i).getMealPlan()
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this.getActivity(), R.layout.list_recipe, android.R.id.text1, recipeStrings);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryListFragment.this.getActivity(), RecipeDetailActivity.class);
                intent.putExtra("RecipeName", (String) adapter.getItem(position));
                intent.putExtra("isFromHistory", "true");
                startActivity(intent);
            }
        });
        return view;
    }

}
