package edu.dartmouth.cs.pantryplanner.app.controller;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.Recipe;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;
import edu.dartmouth.cs.pantryplanner.common.Item;
import edu.dartmouth.cs.pantryplanner.common.ItemType;

public class RecipeDetailActivity extends AppCompatActivity {
    private Button mFinishButton;
    private RecipeRecord recipeRecord;
    private TextView mealDateText;
    private TextView mealTypeText;
    private TextView recipeNameText;
    private TextView ingredientText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        String mealType = recipeRecord.getMealType();
        DateTime mealDate = recipeRecord.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");

        Recipe recipe = recipeRecord.getRecipe();
        String recipeName = recipe.getName();
        Map<Item, Integer> items = new HashMap<>();
        Item item1 = new Item("beef", ItemType.MEAT);
        Item item2 = new Item("tomato", ItemType.VEGETABLE);
        items.put(item1, 200);
        items.put(item2, 3);


        mealDateText = (TextView) findViewById(R.id.textView_recipe_date);
        mealDateText.setText(dateFormat.format(new Date()));
        mealTypeText = (TextView) findViewById(R.id.textView_recipe_type);
        mealTypeText.setText(mealType);
        recipeNameText = (TextView) findViewById(R.id.textView_recipe_name);
        recipeNameText.setText(recipeName);
        ingredientText = (TextView) findViewById(R.id.textView_recipe_ingredient);




        mFinishButton = (Button) findViewById(R.id.finish_button);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private class IngredientAdapter extends BaseAdapter {
        private Context mContext;
        private List<Item> mItems;
        private int size;

        public IngredientAdapter(Context context, List<Item> items){
            this.mContext = context;
            this.mItems = items;
            this.size = items.size();
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text1 = (TextView)(convertView.findViewById(R.id.textView_recipe_ingredient));
            String curItem = mItems.get(position).getName() + "     " + .get();

            text1.setText(mItems.get(position).getName());
            return convertView;
        }
    }
}
