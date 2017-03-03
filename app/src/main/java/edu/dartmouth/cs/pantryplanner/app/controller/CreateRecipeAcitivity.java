package edu.dartmouth.cs.pantryplanner.app.controller;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.dartmouth.cs.pantryplanner.app.R;


/**
 * Created by litianqi on 3/1/17.
 */

public class CreateRecipeAcitivity extends AppCompatActivity{

    TextView mRecipeName;
    TextView mSteps;

    private Button mAddButton;
    private Button mSaveButton;
    private Button mCancelButton;

    private ArrayList<TextView> mTextViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        mRecipeName = (EditText) findViewById(R.id.create_recipe_name);
        mSteps = (EditText) findViewById(R.id.create_recipe_steps);

        mAddButton = (Button) findViewById(R.id.add_ingredient);
        mAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /* dynamically add edit text box and spinner */
                final EditText t1 = new EditText(CreateRecipeAcitivity.this);
                final EditText t2 = new EditText(CreateRecipeAcitivity.this);
                ArrayList<String> spinnerArray = new ArrayList<>();
                spinnerArray.add("MEAT");
                spinnerArray.add("DIARY");
                spinnerArray.add("FRUIT");
                spinnerArray.add("VEGETABLE");
                spinnerArray.add("INGREDIENT");
                spinnerArray.add("OTHER");

                final Spinner spinner = new Spinner(CreateRecipeAcitivity.this);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateRecipeAcitivity.this,
                        android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setLayoutParams(new ActionBar.LayoutParams(400, ActionBar.LayoutParams.WRAP_CONTENT));

                t1.setWidth(500);
                t2.setWidth(500);
                t1.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(t1, InputMethodManager.SHOW_IMPLICIT);

                final LinearLayout root = (LinearLayout) findViewById(R.id.my_create_recipe_layout);
                final LinearLayout horizontal = new LinearLayout(CreateRecipeAcitivity.this);
                horizontal.setOrientation(LinearLayout.HORIZONTAL);
                horizontal.addView(spinner);
                horizontal.addView(t1);
                horizontal.addView(t2);
                root.addView(horizontal,2);

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

                t2.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            Log.d("focus2", "focus loosed");
                           /* get user's input */
                            String newString1 = t1.getText().toString();
                            Log.d("String1", newString1);
                            String newString2 = t2.getText().toString();
                            Log.d("String2", newString2);
                            if (!newString1.equals("") && !newString2.equals("")){

                                TextView newTextView1 = new TextView(CreateRecipeAcitivity.this);
                                TextView newTextView2 = new TextView(CreateRecipeAcitivity.this);

                                newTextView1.setText(newString1);
                                newTextView2.setText(newString2);
                                LinearLayout horizontal_text = new LinearLayout(CreateRecipeAcitivity.this);
                                horizontal_text.setOrientation(LinearLayout.HORIZONTAL);
                                root.removeView(horizontal);
                                horizontal_text.addView(newTextView1);
                                horizontal_text.addView(newTextView2);
                                root.addView(horizontal_text, 2);
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
            }
        });
        mCancelButton = (Button) findViewById(R.id.create_recipe_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cancelBtnSelected(v);
            }

        });

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
        AddRecipeAsyncTask addRecipeAsyncTask = new AddRecipeAsyncTask();
        addRecipeAsyncTask.execute();
        finish();
    }

    public void cancelBtnSelected(View view){
        Toast.makeText(CreateRecipeAcitivity.this, "Recipe discarded", Toast.LENGTH_SHORT).show();
        finish();
    }

    private class AddRecipeAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }


}
