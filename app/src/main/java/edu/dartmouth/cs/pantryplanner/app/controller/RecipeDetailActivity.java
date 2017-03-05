package edu.dartmouth.cs.pantryplanner.app.controller;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Item;
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;

public class RecipeDetailActivity extends AppCompatActivity {
    private Button mFinishButton;

    private RecipeRecord recipeRecord;
    private MealPlanRecord mealPlanRecord;

    private TextView mealDateText;
    private TextView mealTypeText;
    private TextView recipeNameText;
    private TextView ingredientText;
    private IngredientAdapter ingredientAdapter;
    private StepsAdapter stepsAdapter;

    private String date;
    private String mealType;
    private String recipeString;
    private Recipe recipe;

    @TargetApi(24)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent i = getIntent();
        String isFromHistory = i.getStringExtra("isFromHistory");


        if (isFromHistory.equals("false")){ // coming from meal plan fragment
            date = i.getStringExtra("mPRDate");
            mealType = i.getStringExtra("mPRMealType");
            recipeString = i.getStringExtra("mPRRecipe");
            recipe = Recipe.fromString(recipeString);
        } else {
            date = i.getStringExtra("rDate");
            mealType = i.getStringExtra("rMealType");
            recipeString = i.getStringExtra("rRecipe");
            recipe = Recipe.fromString(recipeString);
        }
//        Log.d("date: ", date);
//        Log.d("mealType: ", mealType);
//        Log.d("recipeS", recipeString);


//        Map<Item, Integer> items = new HashMap<>();
//        Item item1 = new Item("beef", ItemType.MEAT);
//        Item item2 = new Item("tomato", ItemType.VEGETABLE);
//        items.put(item1, 200);
//        items.put(item2, 3);

        Map<Item, Integer> items = recipe.getItems();
        ingredientAdapter = new IngredientAdapter(items);
        ListView listViewIn = (ListView) findViewById(R.id.list_display_recipe_items);
        listViewIn.setAdapter(ingredientAdapter);
//
//        List<String> steps = new ArrayList<>();
//        steps.add("1.add water");
//        steps.add("2.ba rou qie cheng xiao kuai");
//        steps.add("3.rou fang dao shui li");

        List<String> steps = recipe.getSteps();
        stepsAdapter = new StepsAdapter(steps);
        ListView listViewS = (ListView) findViewById(R.id.list_display_recipe_steps);
        listViewS.setAdapter(stepsAdapter);



        mealDateText = (TextView) findViewById(R.id.textView_recipe_date);
        mealDateText.setText("07 10 2017");
        //mealDateText.setText(date);
        mealTypeText = (TextView) findViewById(R.id.textView_recipe_type);
        mealTypeText.setText("Lunch");
        recipeNameText = (TextView) findViewById(R.id.textView_recipe_name);
        recipeNameText.setText("hongshaorou");
        ingredientText = (TextView) findViewById(R.id.textView_recipe_ingredient);



        mFinishButton = (Button) findViewById(R.id.finish_button);
//        if (isFromHistory.equals("true")){
//            mFinishButton.setVisibility(View.GONE);
//        } else {
//            mFinishButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
    }

    private class IngredientAdapter extends BaseAdapter {
        //private Context mContext;
        private final ArrayList<Map.Entry> mData;

        public IngredientAdapter(Map<Item, Integer> items){
            //this.mContext = context;
            mData = new ArrayList<>();
            mData.addAll(items.entrySet());

        }
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Map.Entry getItem(int position) {
            return (Map.Entry) mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View result;

            result = getLayoutInflater().inflate(R.layout.entry_item, null);

                Map.Entry<Item, Integer> item = getItem(position);
                String material = item.getKey().getName();
                String quantity = item.getValue().toString();

                TextView materialTV = (TextView) result.findViewById(R.id.textView_recipe_ingredient);
                materialTV.setText(material);
                Log.d("setM", material);
                TextView quantityTV = (TextView) result.findViewById(R.id.textView_recipe_quantity);
                Log.d("setQ", quantity);
                quantityTV.setText(quantity);

            return result;

        }
    }

    private class StepsAdapter extends BaseAdapter {
        //private Context mContext;
        private final List<String> mSteps;

        public StepsAdapter(List<String> steps){
            //this.mContext = context;
            this.mSteps = steps;
        }
        @Override
        public int getCount() {
            return mSteps.size();
        }

        @Override
        public String getItem(int position) {
            return mSteps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View result;

            result = getLayoutInflater().inflate(R.layout.entry_step, null);


            String step = getItem(position);

            TextView stepTV = (TextView) result.findViewById(R.id.textView_recipe_step);
            stepTV.setText(step);

            return result;

        }
    }
}
