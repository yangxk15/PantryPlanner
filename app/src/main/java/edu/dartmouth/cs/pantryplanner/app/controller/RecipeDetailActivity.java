package edu.dartmouth.cs.pantryplanner.app.controller;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Item;
import edu.dartmouth.cs.pantryplanner.app.model.MealPlan;
import edu.dartmouth.cs.pantryplanner.app.model.PantryItem;
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.historyRecordApi.HistoryRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.historyRecordApi.model.HistoryRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.MealPlanRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.pantryRecordApi.PantryRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.pantryRecordApi.model.PantryRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;

import static edu.dartmouth.cs.pantryplanner.app.util.Constants.DATE_FORMAT;

public class RecipeDetailActivity extends AppCompatActivity {
    private MealPlan mealPlan;
    @TargetApi(24)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        boolean isFromHistory = getIntent().getBooleanExtra("isFromHistory", false);
        boolean isFromExplore = getIntent().getBooleanExtra("isFromExplore", false);

        Recipe recipe;
        Map<Item, Integer> items;
        List<String> steps;

        if (isFromExplore) {
            recipe = Recipe.fromString(getIntent().getStringExtra(ExploreRecipeActivity.RECIPE_KEY));
            items = recipe.getItems();
            steps = recipe.getSteps();((TextView) findViewById(R. id.textView_recipe_name)).setText(recipe.getName());
        } else {
            String temp = getIntent().getStringExtra(MealPlanFragment.SELECTED_MEAL_PLAN);
            mealPlan = MealPlan.fromString(getIntent().getStringExtra(MealPlanFragment.SELECTED_MEAL_PLAN));
            items = mealPlan.getRecipe().getItems();
            steps = mealPlan.getRecipe().getSteps();
            ((TextView) findViewById(R.id.textView_recipe_date)).setText(DATE_FORMAT.format(mealPlan.getDate()));
            ((TextView) findViewById(R.id.textView_recipe_type)).setText(mealPlan.getMealType().toString());
            ((TextView) findViewById(R. id.textView_recipe_name)).setText(mealPlan.getRecipe().getName());
        }

        IngredientAdapter ingredientAdapter = new IngredientAdapter(items);
        ListView listViewIn = (ListView) findViewById(R.id.list_display_recipe_items);
        listViewIn.setAdapter(ingredientAdapter);

        StepsAdapter stepsAdapter = new StepsAdapter(steps);
        ListView listViewS = (ListView) findViewById(R.id.list_display_recipe_steps);
        listViewS.setAdapter(stepsAdapter);



        Button mFinishButton = (Button) findViewById(R.id.finish_button);
        if (isFromHistory){
            mFinishButton.setVisibility(View.GONE);
        } else if (isFromExplore) {
            mFinishButton.setVisibility(View.GONE);
        } else {
            mFinishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 3/5/17
                    // Finish cooking and update pantry list action
                    new RemoveMealPlanAsyncTask().execute();

                }
            });
        }
    }

    private class RemoveMealPlanAsyncTask extends AsyncTask<Void, Void, IOException>{

        @Override
        protected IOException doInBackground(Void... params) {
            IOException ex = null;
            try {
                // Remove meal plan
                MealPlanRecordApi mealPlanRecordApi = ServiceBuilderHelper.getBuilder(
                        RecipeDetailActivity.this,
                        MealPlanRecordApi.Builder.class
                ).build();
                Log.d("id", mealPlan.getId().toString());
                mealPlanRecordApi.remove(mealPlan.getId()).execute();

                // Add to history
                HistoryRecordApi historyRecordApi = ServiceBuilderHelper.getBuilder(RecipeDetailActivity.this,
                        HistoryRecordApi.Builder.class).build();
                HistoryRecord insert = new HistoryRecord();
                insert.setEmail(new Session(RecipeDetailActivity.this).getString("email"));
                insert.setHistory(mealPlan.toString());
                historyRecordApi.insert(insert).execute();

                // Reduce pantry list
                PantryRecordApi pantryRecordApi = ServiceBuilderHelper.getBuilder(
                        RecipeDetailActivity.this,
                        PantryRecordApi.Builder.class
                ).build();
                List<PantryRecord> pantryRecords = pantryRecordApi.listWith(
                        new Session(RecipeDetailActivity.this).getString("email")
                ).execute().getItems();
                if (pantryRecords != null) {
                    Map<PantryItem, Integer> pantryItems = new Gson().fromJson(
                            pantryRecords.get(0).getPantryList(),
                            new TypeToken<Map<PantryItem, Integer>>(){}.getType()
                    );

                    Map<Item, TreeMap<PantryItem, Integer>> map = new HashMap<>();
                    for (Map.Entry<PantryItem, Integer> entry : pantryItems.entrySet()) {
                        if (!map.containsKey(entry.getKey().getItem())) {
                            map.put(entry.getKey().getItem(), new TreeMap<PantryItem, Integer>());
                        }
                        map.get(entry.getKey().getItem()).put(entry.getKey(), entry.getValue());
                    }

                    for (Map.Entry<Item, Integer> entry : mealPlan.getRecipe().getItems().entrySet()) {
                        TreeMap<PantryItem, Integer> innerMap = map.get(entry.getKey());
                        int quantityNeed = entry.getValue();
                        while (quantityNeed > 0) {
                            Log.d("TreeMap Test:", pantryItems.toString());
                            Map.Entry<PantryItem, Integer> earliestItem = innerMap.firstEntry();
                            if (earliestItem.getValue() > quantityNeed) {
                                pantryItems.put(earliestItem.getKey(), earliestItem.getValue() - quantityNeed);
                                innerMap.put(earliestItem.getKey(), earliestItem.getValue() - quantityNeed);
                            } else {
                                pantryItems.remove(earliestItem.getKey());
                                innerMap.remove(earliestItem.getKey());
                            }
                            quantityNeed -= earliestItem.getValue();
                        }
                    }

                    pantryRecords.get(0).setPantryList(
                            new GsonBuilder().enableComplexMapKeySerialization().create()
                            .toJson(pantryItems)
                    );

                    pantryRecordApi.update(pantryRecords.get(0).getId(), pantryRecords.get(0)).execute();
                }

            } catch (IOException e) {
                ex = e;
            }
            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            if (ex == null) {
                Toast.makeText(RecipeDetailActivity.this, "Cooking finished!", Toast.LENGTH_SHORT);
                setResult(RESULT_OK, new Intent());
                finish();
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            RecipeDetailActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            RecipeDetailActivity.this,
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
        }

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
