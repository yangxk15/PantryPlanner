package edu.dartmouth.cs.pantryplanner.app.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Item;
import edu.dartmouth.cs.pantryplanner.app.model.MealPlan;
import edu.dartmouth.cs.pantryplanner.app.model.MealType;
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;
import edu.dartmouth.cs.pantryplanner.app.util.RequestCode;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.MealPlanRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.shoppingListRecordApi.ShoppingListRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.shoppingListRecordApi.model.ShoppingListRecord;


public class CreateMealActivity extends AppCompatActivity{

    private Spinner spinner;
    private Button addMealButton;
    private Button saveButton;
    private Button cancelButton;

    private MealPlan mMealPlan;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);

        spinner = (Spinner) findViewById(R.id.spinner_meal_plan_type_select);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, MealType.getMealTypes());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        addMealButton = (Button) findViewById(R.id.button_meal_plan_add);
        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateRecipeActivity.class),
                        RequestCode.CREATE_RECIPE.ordinal()
                );
            }
        });
        saveButton = (Button) findViewById(R.id.create_meal_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtnSelected(v);
            }
        });
        cancelButton = (Button) findViewById(R.id.create_meal_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cancelBtnSelected(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.CREATE_RECIPE.ordinal()) {
                mRecipe = Recipe.fromString(data.getStringExtra(CreateRecipeActivity.CREATED_RECIPE));
            }
        }
    }

    public void saveBtnSelected(View view){
        if (mRecipe == null) {
            Toast.makeText(this, "Please add a recipe", Toast.LENGTH_SHORT).show();
            return;
        }
        mMealPlan = new MealPlan(
                null,
                (Date) getIntent().getExtras().getSerializable(MealPlanFragment.SELECTED_DATE),
                MealType.values()[spinner.getSelectedItemPosition()],
                mRecipe
        );
        new AddMealAsyncTask().execute();
    }

    public void cancelBtnSelected(View view){
        Toast.makeText(CreateMealActivity.this, "Meal plan discarded", Toast.LENGTH_SHORT).show();
        finish();
    }



    private class AddMealAsyncTask extends AsyncTask<Void, Void, IOException>{

        @Override
        protected IOException doInBackground(Void... params) {
            IOException ex = null;
            try {
                MealPlanRecord mealPlanRecord = new MealPlanRecord();
                mealPlanRecord.setEmail(new Session(CreateMealActivity.this).getString("email"));
                mealPlanRecord.setMealPlan(mMealPlan.toString());
                MealPlanRecordApi mealPlanRecordApi = ServiceBuilderHelper.getBuilder(
                        CreateMealActivity.this,
                        MealPlanRecordApi.Builder.class
                ).build();
                mealPlanRecordApi.insert(mealPlanRecord).execute();

                ShoppingListRecordApi shoppingListRecordApi = ServiceBuilderHelper.getBuilder(
                        CreateMealActivity.this,
                        ShoppingListRecordApi.Builder.class
                ).build();
                List<ShoppingListRecord> shoppingListRecords = shoppingListRecordApi.listWith(
                        new Session(CreateMealActivity.this).getString("email")
                ).execute().getItems();

                if (shoppingListRecords != null) {
                    ShoppingListRecord shoppingListRecord = shoppingListRecords.get(0);
                    Map<Item, Integer> oldList = new Gson().fromJson(
                            shoppingListRecord.getShoppingList(),
                            new TypeToken<Map<Item, Integer>>(){}.getType()
                    );
                    for (Map.Entry<Item, Integer> entry : mMealPlan.getRecipe().getItems().entrySet()) {
                        if (!oldList.containsKey(entry.getKey())) {
                            oldList.put(entry.getKey(), entry.getValue());
                        } else {
                            oldList.put(entry.getKey(), entry.getValue() + oldList.get(entry.getKey()));
                        }
                    }
                    shoppingListRecord.setShoppingList(
                            new GsonBuilder().enableComplexMapKeySerialization()
                            .create().toJson(oldList)
                    );
                    shoppingListRecordApi.update(shoppingListRecord.getId(), shoppingListRecord).execute();
                } else {
                    ShoppingListRecord shoppingListRecord = new ShoppingListRecord();
                    shoppingListRecord.setEmail(
                            new Session(CreateMealActivity.this).getString("email")
                    );
                    shoppingListRecord.setShoppingList(
                            new GsonBuilder().enableComplexMapKeySerialization()
                                    .create().toJson(mMealPlan.getRecipe().getItems())
                    );
                    shoppingListRecordApi.insert(shoppingListRecord).execute();
                }

            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex){
            if (ex == null) {
                Toast.makeText(CreateMealActivity.this, "Meal plan saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            CreateMealActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            CreateMealActivity.this,
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
        }
    }


}
