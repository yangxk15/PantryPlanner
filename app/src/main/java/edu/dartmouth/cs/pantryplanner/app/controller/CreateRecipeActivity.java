package edu.dartmouth.cs.pantryplanner.app.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Item;
import edu.dartmouth.cs.pantryplanner.app.model.ItemType;
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.RecipeRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;


public class CreateRecipeActivity extends AppCompatActivity{
    public static final String CREATED_RECIPE = "Create_Recipe";

    TextView mRecipeName;
    TextView mSteps;

    private ImageButton mAddButton;
    private Button mSaveButton;
    private Button mCancelButton;

    private String name;
    private Map<Item, Integer> items = new HashMap<>();
    private List<String> steps = new ArrayList<>();



    private ArrayList<TextView> mTextViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        mRecipeName = (EditText) findViewById(R.id.create_recipe_name);
        mSteps = (EditText) findViewById(R.id.create_recipe_steps);

        mAddButton = (ImageButton) findViewById(R.id.add_ingredient);
        mAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /* dynamically add edit text box and spinner */
                final EditText t1 = new EditText(CreateRecipeActivity.this);
                final EditText t2 = new EditText(CreateRecipeActivity.this);
                ArrayList<String> spinnerArray = new ArrayList<>(Arrays.asList(ItemType.getItemTypes()));


                final Spinner spinner = new Spinner(CreateRecipeActivity.this);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateRecipeActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setLayoutParams(new ActionBar.LayoutParams(400, ActionBar.LayoutParams.WRAP_CONTENT));

                t1.setWidth(500);
                t2.setWidth(500);
                t1.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(t1, InputMethodManager.SHOW_IMPLICIT);

                final LinearLayout root = (LinearLayout) findViewById(R.id.my_create_recipe_layout);
                final LinearLayout horizontal = new LinearLayout(CreateRecipeActivity.this);
                horizontal.setOrientation(LinearLayout.HORIZONTAL);
                horizontal.addView(spinner);
                horizontal.addView(t1);
                horizontal.addView(t2);
                root.addView(horizontal,3);

                t1.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                       if (!hasFocus) {
                           Log.d("focus", "focus loosed");
                       } else {
                           Log.d("focus", "focused");
                       }
                    }
                });
                t2.setKeyListener(new DigitsKeyListener());
                t2.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            Log.d("focus2", "focus loosed");
                           /* get user's input */
                            String material = t1.getText().toString();
                            Log.d("material", material);
                            String quantity = t2.getText().toString();
                            Log.d("quantity", quantity);
                            if (!material.equals("") && !quantity.equals("")){

                                TextView newTextView1 = new TextView(CreateRecipeActivity.this);
                                TextView newTextView2 = new TextView(CreateRecipeActivity.this);

                                newTextView1.setText(material);
                                newTextView1.setTypeface(Typeface.DEFAULT_BOLD);
                                newTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                                newTextView1.setPadding(50, 5, 20, 5);
                                newTextView2.setText(quantity);
                                newTextView2.setTypeface(Typeface.DEFAULT_BOLD);
                                newTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                                newTextView2.setPadding(50, 5, 20, 5);

                                LinearLayout horizontal_text = new LinearLayout(CreateRecipeActivity.this);
                                horizontal_text.setOrientation(LinearLayout.HORIZONTAL);
                                root.removeView(horizontal);
                                horizontal_text.addView(newTextView1);
                                horizontal_text.addView(newTextView2);
                                root.addView(horizontal_text, 3);
                                Log.d("spinner position: ", spinner.getSelectedItem().toString());
                                Item item = new Item(material,ItemType.values()[spinner.getSelectedItemPosition()]);
                                items.put(item, Integer.parseInt(quantity));
                            } else {
                                root.removeView(horizontal);
                            }
                        } else {
                            Log.d("focus", "focused");
                        }
                    }
                });
            }
        });
        mSaveButton = (Button) findViewById(R.id.create_recipe_save);
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveBtnSelected(view);
<<<<<<< HEAD
                /* pass the recipe result back to createMealActivity */

                Intent resultIntent = new Intent();
                Recipe recipe = new Recipe(name, items, steps);
                resultIntent.putExtra("recipe", recipe.toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

=======
>>>>>>> origin/master
            }
        });
        mCancelButton = (Button) findViewById(R.id.create_recipe_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);

                cancelBtnSelected(v);
            }

        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER)
        {
            //Nothing
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public void saveBtnSelected(View view){
        new AddRecipeAsyncTask().execute();
    }

    public void cancelBtnSelected(View view){
        Toast.makeText(CreateRecipeActivity.this, "Recipe discarded", Toast.LENGTH_SHORT).show();
        finish();
    }

    private class AddRecipeAsyncTask extends AsyncTask<Void, Void, IOException>{

        Recipe mRecipe;

        @Override
        protected void onPreExecute() {
            name = mRecipeName.getText().toString();
            Log.d("Recipe Name",name);
            steps.add(mSteps.getText().toString());
            Log.d("Steps",steps.get(0));
            mRecipe = new Recipe(name, items, steps);
        }
        @Override
        protected IOException doInBackground(Void... params) {

            IOException ex = null;
            try {
                RecipeRecordApi recipeRecordApi = ServiceBuilderHelper.getBuilder(
                        CreateRecipeActivity.this,
                        RecipeRecordApi.Builder.class
                ).build();

                RecipeRecord recipeRecord = new RecipeRecord();
                recipeRecord.setEmail("testEmail");
                recipeRecord.setRecipe(mRecipe.toString());

                recipeRecordApi.insert(recipeRecord).execute();
            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            if (ex == null) {
                Toast.makeText(getApplicationContext(), "Recipe saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Log.d("return recipe ", mRecipe.toString());
                intent.putExtra(CREATED_RECIPE, mRecipe.toString());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            CreateRecipeActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            CreateRecipeActivity.this,
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
            return;
        }
    }


}
