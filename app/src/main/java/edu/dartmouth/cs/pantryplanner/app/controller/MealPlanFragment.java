package edu.dartmouth.cs.pantryplanner.app.controller;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;
import edu.dartmouth.cs.pantryplanner.common.MealType;
import edu.dartmouth.cs.pantryplanner.common.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealPlanFragment extends Fragment {
    MealPlanAdapter mMealPlanAdapter;
    private Date cur = new Date();
    public MealPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.expandableListView_meal_plan);

        dataProcess();

        listView.setAdapter(mMealPlanAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // jump to display recipe
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(MealPlanFragment.this.getActivity(), DisplayRecipeActivity.class);
                //intent.putExtra("RecipeName", (Recipe) adapter.getItem(position));
                //startActivity(intent);
            }
        });
        return view;
    }

    // TODO:
    private void dataProcess() {
        /*  ArrayList<ArrayList<ArrayList<Meal>>>
                      Day       Tag       Meal
         */
        // TODO: 3/3/17 read from server
        // get List of recipeRecord
        Recipe newrecipe1 = new Recipe("Beacon", new Date(), MealType.BREAKFAST, null, null);
        Recipe newrecipe2 = new Recipe("Milk", new Date(), MealType.LUNCH, null, null);
        Recipe newrecipe3 = new Recipe("Broccoli", new Date(), MealType.LUNCH, null, null);
        ArrayList<Recipe> breakfast = new ArrayList<>();
        ArrayList<Recipe> lunch = new ArrayList<>();
        breakfast.add(newrecipe1);
        lunch.add(newrecipe2);
        lunch.add(newrecipe3);

        ArrayList<ArrayList<Recipe>> mealtype = new ArrayList<>();
        mealtype.add(breakfast);
        mealtype.add(lunch);

        ArrayList<ArrayList<Recipe>> mealtype2 = new ArrayList<>();
        mealtype2.add(lunch);

        ArrayList<ArrayList<ArrayList<Recipe>>> map = new ArrayList<>();

        ArrayList<Date> dateMap = new ArrayList<>();
        dateMap.add(cur);
        Date now = new Date();
        dateMap.add(now);


        map.add(mealtype);
        map.add(mealtype2);

        mMealPlanAdapter = new MealPlanAdapter(this.getActivity(), dateMap, map);
    }



    private class MealPlanAdapter extends BaseExpandableListAdapter {
        ArrayList<ArrayList<ArrayList<Recipe>>> groupMap;
        ArrayList<Date> dateMap;
        Context context;

        public MealPlanAdapter(Context context, ArrayList<Date> dateMap,
                               ArrayList<ArrayList<ArrayList<Recipe>>> groupMap) {
            this.context = context;
            this.groupMap = groupMap;
            this.dateMap = dateMap;
        }

        @Override
        public int getGroupCount() {
            return groupMap.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groupMap.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupMap.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groupMap.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_meal_plan_date, parent, false);

            TextView textView = (TextView) view.findViewById(R.id.textView_meal_plan_date);
            textView.setText((new SimpleDateFormat("MMM dd yyyy")).format(dateMap.get(groupPosition)));
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ArrayList<Recipe> recipes = (ArrayList<Recipe>) getChild(groupPosition, childPosition);
            String[] recipenames = new String[recipes.size()];
            for (int i = 0; i < recipenames.length; ++i) {
                recipenames[i] = recipes.get(i).getName();
            }

            LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.list_meal_plan_mealtype, parent, false);

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_meal_mealtype);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            //TODO: set height suitably
            params.height = recipenames.length * 200 + 15;
            linearLayout.setLayoutParams(params);

            TextView mealType = (TextView) view.findViewById(R.id.textView_meal_type);
            mealType.setText(recipes.get(0).getMealType().toString());

            // ArrayAdapter for each meal type
            ListView listView = (ListView) view.findViewById(R.id.textView_meal_plan_recipe);
            ArrayAdapter<String> adapter = new ArrayAdapter(context,
                    android.R.layout.simple_list_item_1, android.R.id.text1, recipenames);
            listView.setAdapter(adapter);

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
