package edu.dartmouth.cs.pantryplanner.app.controller;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.MealPlan;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.MealPlanRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import edu.dartmouth.cs.pantryplanner.app.model.MealType;
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;

import static edu.dartmouth.cs.pantryplanner.app.util.Constants.DATE_FORMAT;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealPlanFragment extends Fragment {
    public static final String SELECTED_DATE = "Selected Date";
    public static final String SELECTED_MEAL_PLAN = "Selected Meal Plan";

    private List<MealPlan> mealPlans;

    // UI Reference
    private ExpandableListView mExpandableListView;
    MealPlanAdapter mMealPlanAdapter;
    public static int mMealNumber = 7;

    public MealPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView_meal_plan);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new ReadMealPlanListTask().execute();
    }

    // TODO:
    private void dataProcess() {
        /*  HashMap<String, ArrayList<ArrayList<Meal>>>
                    Day       Tag       Meal
         */

        Calendar calendar = Calendar.getInstance();

        HashMap<String, ArrayList<ArrayList<MealPlan>>> dateMap = new HashMap<>();
        for (int i = 0; i < mMealNumber; ++i) {
            ArrayList<ArrayList<MealPlan>> mealTypeList = new ArrayList<>();
            for (int j = 3; j > 0; --j) {
                mealTypeList.add(new ArrayList<MealPlan>());
            }
            dateMap.put(DATE_FORMAT.format(calendar.getTime()), mealTypeList);
            calendar.add(Calendar.DATE, 1);
        }

        for (MealPlan mealPlan: mealPlans) {
//            Log.d("time", formatter.format(Calendar.getInstance().getTime()));
            ArrayList<ArrayList<MealPlan>> mealTypeList = dateMap.get(DATE_FORMAT.format(mealPlan.getDate()));
            mealTypeList.get(mealPlan.getMealType().ordinal()).add(mealPlan);
//            Log.d("meal", "" + mealTypeList.get(0).get(0).getRecipe().getName());
        }

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

        mExpandableListView.setAdapter(new MealPlanAdapter(this.getActivity(), dateMap));
        mExpandableListView.expandGroup(0);
    }



    private class MealPlanAdapter extends BaseExpandableListAdapter {
        HashMap<String, ArrayList<ArrayList<MealPlan>>> groupMap;
        Context context;

        public MealPlanAdapter(Context context,
                               HashMap<String, ArrayList<ArrayList<MealPlan>>> groupMap) {
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
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_meal_plan_date, parent, false);
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageView_meal_add);
            imageButton.setFocusable(false);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), CreateMealActivity.class);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, groupPosition);
                    i.putExtra(SELECTED_DATE, calendar.getTime());
                    startActivity(i);
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

            final ArrayList<MealPlan> mealPlans = (ArrayList<MealPlan>) getChild(groupPosition, childPosition);
            String[] recipeNames = new String[mealPlans.size()];
            for (int i = 0; i < recipeNames.length; ++i) {
                recipeNames[i] = mealPlans.get(i).getRecipe().getName();
            }

            // set layout height
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_meal_mealtype);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            //TODO: set height suitably
            params.height = recipeNames.length * 200 + 15;
            linearLayout.setLayoutParams(params);

            TextView mealType = (TextView) view.findViewById(R.id.textView_meal_type);
            mealType.setText(mealPlans.get(0).getMealType().toString());

            // ArrayAdapter for each meal type
            ListView listView = (ListView) view.findViewById(R.id.textView_meal_plan_recipe);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, android.R.id.text1, recipeNames);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                    intent.putExtra(SELECTED_MEAL_PLAN, mealPlans.get(position).toString());
                    startActivity(intent);
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
            return DATE_FORMAT.format(calendar.getTime());
        }
    }

    private class ReadMealPlanListTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... arg0) {
            IOException ex = null;
            try {
                MealPlanRecordApi mealPlanRecordApi = ServiceBuilderHelper.getBuilder(
                        MealPlanFragment.this.getActivity(),
                        MealPlanRecordApi.Builder.class
                ).build();

                List<MealPlanRecord> mealPlanRecords = mealPlanRecordApi.listWith(
                        new Session(MealPlanFragment.this.getActivity()).getString("email")
                ).execute().getItems();

                if (mealPlanRecords == null) {
                    mealPlans = new ArrayList<>();
                } else {
                    mealPlans = MealPlan.fromMealPlanRecords(mealPlanRecords);
                }
            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            if (ex == null) {
                dataProcess();
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            MealPlanFragment.this.getActivity(),
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            MealPlanFragment.this.getActivity(),
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
        }

    }
}
