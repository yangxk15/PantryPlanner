package edu.dartmouth.cs.pantryplanner.pantryplanner.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.dartmouth.cs.pantryplanner.pantryplanner.R;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView textView = (TextView) findViewById(R.id.textView_recipe_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        textView.setText(dateFormat.format(new Date()));

        textView = (TextView) findViewById(R.id.textView_recipe_ingredient);
        textView.setText("Oil 0.1 gal\n" +
                "Onion 1\n" +
                "Salted chicken 1 lb");

        textView = (TextView) findViewById(R.id.textView_recipe_step);
        textView.setText("1. Add some oil,\n" +
                "2. Fry the onions,\n" +
                "3. Add the salted chicken.");
    }
}
