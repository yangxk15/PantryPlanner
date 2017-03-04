package edu.dartmouth.cs.pantryplanner.app.controller;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import com.google.api.client.util.DateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import edu.dartmouth.cs.pantryplanner.common.MealType;
import edu.dartmouth.cs.pantryplanner.common.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealPlanFragment extends Fragment {
    MealPlanAdapter mMealPlanAdapter;
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
        listView.expandGroup(0);
        return view;
    }

    // TODO:
    private void dataProcess() {
        /*  HashMap<String, ArrayList<ArrayList<Meal>>>
                    Day       Tag       Meal
         */

        MealPlanRecord newrecipe1 = new MealPlanRecord();
        //Log.d(MealType.BREAKFAST.name(), "mealtype");
        //MealPlanRecord newrecipe2 = new Recipe("Milk", new Date(), MealType.DINNER, null, null);
        //MealPlanRecord newrecipe3 = new Recipe("Broccoli", new Date(), MealType.DINNER, null, null);

        ArrayList<MealPlanRecord> recipes = new ArrayList<>();
        recipes.add(newrecipe1);
        //recipes.add(newrecipe2);
        //recipes.add(newrecipe3);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        HashMap<String, ArrayList<ArrayList<MealPlanRecord>>> dateMap = new HashMap<>();
        for (int i = 0; i < 7; ++i) {
            ArrayList<ArrayList<MealPlanRecord>> mealTypeList = new ArrayList<>();
            for (int j = 3; j > 0; --j) {
                mealTypeList.add(new ArrayList<MealPlanRecord>());
            }
            dateMap.put(formatter.format(calendar.getTime()), mealTypeList);
            calendar.add(Calendar.DATE, 1);
        }

        //for (MealPlanRecord recipe: recipes) {
        //Log.d("time", formatter.format(Calendar.getInstance().getTime()));
            ArrayList<ArrayList<MealPlanRecord>> mealTypeList = dateMap.get(formatter.format(Calendar.getInstance().getTime()));
            mealTypeList.get(0).add(newrecipe1);
        //Log.d("meal", "" + mealTypeList.get(0).get(0).getRecipe().getName());
        //}

        // remove all empty meal type list
        Iterator it = dateMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            ArrayList<ArrayList<Recipe>> mealTypeList2 = (ArrayList)pair.getValue();
            for (int i = MealType.values().length - 1; i >=0; --i) {
                if (mealTypeList2.get(i).size() == 0) {
                    mealTypeList2.remove(i);
                }
            }
        }

        mMealPlanAdapter = new MealPlanAdapter(this.getActivity(), dateMap);
    }



    private class MealPlanAdapter extends BaseExpandableListAdapter {
        HashMap<String, ArrayList<ArrayList<MealPlanRecord>>> groupMap;
        Context context;

        public MealPlanAdapter(Context context,
                               HashMap<String, ArrayList<ArrayList<MealPlanRecord>>> groupMap) {
            this.context = context;
            this.groupMap = groupMap;
        }

        @Override
        public int getGroupCount() {
            return groupMap.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return ((ArrayList) getGroup(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupMap.get(getDate(groupPosition));
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return ((ArrayList)getGroup(groupPosition)).get(childPosition);
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
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageView_meal_add);
            imageButton.setFocusable(false);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "imageButton");
                }
            });
            TextView textView = (TextView) view.findViewById(R.id.textView_meal_plan_date);
            textView.setText(getDate(groupPosition));
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_meal_plan_mealtype, parent, false);

            ArrayList<MealPlanRecord> recipes = (ArrayList<MealPlanRecord>) getChild(groupPosition, childPosition);
            String[] recipenames = new String[recipes.size()];
            for (int i = 0; i < recipenames.length; ++i) {
                recipenames[i] = recipes.get(i).getRecipe();
            }

            // set layout height
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_meal_mealtype);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            //TODO: set height suitably
            params.height = recipenames.length * 200 + 15;
            linearLayout.setLayoutParams(params);

            TextView mealType = (TextView) view.findViewById(R.id.textView_meal_type);
            mealType.setText(recipes.get(0).getMealType());

            // ArrayAdapter for each meal type
            ListView listView = (ListView) view.findViewById(R.id.textView_meal_plan_recipe);
            ArrayAdapter<String> adapter = new ArrayAdapter(context,
                    android.R.layout.simple_list_item_1, android.R.id.text1, recipenames);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("position", "" + position);
                }
            });
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private String getDate(int groupPosition) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, groupPosition);
            return new SimpleDateFormat("MMM dd yyyy").format(calendar.getTime());
        }
    }
}
