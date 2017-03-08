package edu.dartmouth.cs.pantryplanner.app.controller;


import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.MealPlan;
import edu.dartmouth.cs.pantryplanner.app.util.FragmentUtil;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.historyRecordApi.HistoryRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.historyRecordApi.model.HistoryRecord;

import static edu.dartmouth.cs.pantryplanner.app.controller.MealPlanFragment.SELECTED_MEAL_PLAN;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryListFragment extends Fragment implements FragmentUtil {

    private List<MealPlan> mealPlans;
    private ArrayList<String> recipeStrings;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    public HistoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        listView = (ListView) view.findViewById(R.id.listView_history_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                intent.putExtra(SELECTED_MEAL_PLAN, mealPlans.get(position).toString());
                intent.putExtra("isFromHistory", true);
                startActivity(intent);
            }
        });

        updateFragment();
        return view;
    }



    private class ReadHistoryListTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... arg0) {
            IOException ex = null;
            try {
                HistoryRecordApi.Builder builder = ServiceBuilderHelper.getBuilder(
                        HistoryListFragment.this.getActivity(),
                        HistoryRecordApi.Builder.class
                );
                if (builder == null) {
                    return null;
                }

                HistoryRecordApi historyRecordApi = builder.build();

                List<HistoryRecord> historyRecords = historyRecordApi.listWith(
                        new Session(HistoryListFragment.this.getActivity()).getString("email")
                ).execute().getItems();

                if (historyRecords == null) {
                    Log.d("HISTORYRECORDS","empty");
                    mealPlans = new ArrayList<>();

                } else {
                    mealPlans = MealPlan.fromHistoryRecords(historyRecords);
                }
                recipeStrings = new ArrayList<>();
                for (int i = 0; i < mealPlans.size(); i++){
                    recipeStrings.add(mealPlans.get(i).getRecipe().getName());
                }

            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            if (ex == null) {
                if (recipeStrings != null) {
                    adapter = new ArrayAdapter<>
                            (getActivity(), R.layout.list_recipe, android.R.id.text1, recipeStrings);
                    listView.setAdapter(adapter);
                }
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            HistoryListFragment.this.getActivity(),
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            HistoryListFragment.this.getActivity(),
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
        }
    }

    @Override
    public String getFragmentName() {
        return "History";
    }

    @Override
    public void updateFragment() {
        new ReadHistoryListTask().execute();
    }
}
