package edu.dartmouth.cs.pantryplanner.app.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.dartmouth.cs.pantryplanner.app.R;

/**
 * Created by Lucidity on 17/3/3.
 */

public class ExploreRecipeActivity extends AppCompatActivity {
    private ArrayList<Fragment> mExploreFragmentList;
    String[] values = new String[]{"Banana Oatmeal Muffin",
            "Shrimp Pesto Pasta",
            "Broccoli Beef",
            "Honey Mustard Chicken and Avacado Salad ",
            "Healthier Chicken Alfredo Pasta",
            "Easy Breakfast Frittata",
            "Chocolate Strawberry Cream Puffs ",
            "Fluffy Japanese Cheesecake "
    };


    public ExploreRecipeActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_recipe);

        ListView listView = (ListView) findViewById(R.id.listView_explore_list);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExploreRecipeActivity.this, RecipeDetailActivity.class);
                intent.putExtra("RecipeName", (String) adapter.getItem(position));
                startActivity(intent);
            }
        });
    }
}
