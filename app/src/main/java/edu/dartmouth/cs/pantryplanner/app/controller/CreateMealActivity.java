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

<<<<<<< HEAD
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.RecipeRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;
import edu.dartmouth.cs.pantryplanner.common.Recipe;
=======
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.MealPlan;
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;
import edu.dartmouth.cs.pantryplanner.app.util.RequestCode;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.MealPlanRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import edu.dartmouth.cs.pantryplanner.app.model.MealType;

>>>>>>> origin/master


public class CreateMealActivity extends AppCompatActivity{

    private Spinner spinner;
    private Button addMealButton;
    private Button saveButton;
    private Button cancelButton;

<<<<<<< HEAD
    private Recipe recipe;
=======
    private MealPlan mMealPlan;
    private Recipe mRecipe;
>>>>>>> origin/master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);

        spinner = (Spinner) findViewById(R.id.meal_type_select);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, MealType.getMealTypes());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        addMealButton = (Button) findViewById(R.id.add_meal_button);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.CREATE_RECIPE.ordinal()) {
                Log.d("CreateMeal", "save recipe " + data.getStringExtra(CreateRecipeActivity.CREATED_RECIPE));
                mRecipe = Recipe.fromString(data.getStringExtra(CreateRecipeActivity.CREATED_RECIPE));
            }
        }
    }

    public void saveBtnSelected(View view){
        mMealPlan = new MealPlan(
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        recipe = Recipe.fromString(getIntent().getStringExtra("recipe"));
    }


<<<<<<< HEAD
    private class AddMealAsycTask extends AsyncTask<Void, Void, IOException >{

        @Override
        protected IOException doInBackground(Void... params) {

            IOException ex = null;
            try {
                RecipeRecordApi mealRecordApi = ServiceBuilderHelper.setup(CreateMealActivity.this,
                        new MealRecordApi.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new AndroidJsonFactory(),
                                null
                        )
                ).build();
                MealPlanRecord mealPlanRecord = new MealPlanRecord();
                mealPlanRecord.setId((long) 123);
                mealPlanRecord.setDate("07 08 2017");
                mealPlanRecord.setEmail("test");
                mealPlanRecord.setMealType("Lunch");
                mealPlanRecord.setRecipe(recipe.toString());

                mealRecordApi.insert(recipeRecord).execute().getId();
=======
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
>>>>>>> origin/master
            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
<<<<<<< HEAD
        protected void onPostExecute(IOException ex) {
            if (ex == null) {
                Toast.makeText(getApplicationContext(), "Meal saved!", Toast.LENGTH_SHORT).show();
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
=======
        protected void onPostExecute(IOException ex){
            Toast.makeText(CreateMealActivity.this, "Meal plan saved", Toast.LENGTH_SHORT).show();
            finish();
>>>>>>> origin/master
            return;
        }
    }


}
