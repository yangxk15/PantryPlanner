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

import java.util.ArrayList;
import java.util.List;

import edu.dartmouth.cs.pantryplanner.app.R;

/**
 * Created by litianqi on 3/3/17.
 */

public class CreateMealActivity extends AppCompatActivity{
    private Spinner spinner;
    private Button addMealButton;
    private Button saveButton;
    private Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);
        spinner = (Spinner) findViewById(R.id.meal_type_select);

        List<String> list = new ArrayList<String>();
        list.add("BREAKFAST");
        list.add("LUNCH");
        list.add("DINNER");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        final String mealType = spinner.getSelectedItem().toString();

        addMealButton = (Button) findViewById(R.id.add_meal_button);
        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateRecipeActivity.class);
                Log.d("mealType",mealType);
                i.putExtra("mealType", mealType);
                startActivity(i);
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
    public void saveBtnSelected(View view){
        AddMealAsycTask addMealAsycTask = new AddMealAsycTask();
        addMealAsycTask.execute();
        finish();
    }

    public void cancelBtnSelected(View view){
        Toast.makeText(CreateMealActivity.this, "Recipe discarded", Toast.LENGTH_SHORT).show();
        finish();
    }


    private class AddMealAsycTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void param){

            return;
        }
    }


}
