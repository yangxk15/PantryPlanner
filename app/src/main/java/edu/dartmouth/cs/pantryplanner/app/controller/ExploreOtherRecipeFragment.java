package edu.dartmouth.cs.pantryplanner.app.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.MealPlan;
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;
import edu.dartmouth.cs.pantryplanner.app.util.RequestCode;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.MealPlanRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.mealPlanRecordApi.model.MealPlanRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.RecipeRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.user.User;

/**
 * Created by Lucidity on 17/3/5.
 */

public class ExploreOtherRecipeFragment extends Fragment {
    private Map<Recipe, List<String>> recipes = new HashMap<>();
    // UI Reference
    private ListView mListView;
    private EditText inputSearch;
    private HashMap<String, Integer> map = new HashMap<>();
    private ArrayAdapter<String> adapter;

    public ExploreOtherRecipeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_other_recipe, container, false);
        mListView = (ListView) view.findViewById(R.id.listView_explore_other_list);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new ReadOtherRecipeListTask().execute();
    }

    private void dataProcess() {
        final List<Map.Entry<Recipe, List<String>>> list = new ArrayList<>(recipes.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Recipe, List<String>>>() {
            @Override
            public int compare(Map.Entry<Recipe, List<String>> o1, Map.Entry<Recipe, List<String>> o2) {
                if (o1.getValue().size() < o2.getValue().size()) {
                    return 1;
                } else if (o1.getValue().size() > o2.getValue().size()) {
                    return -1;
                } else {
                    return o1.getKey().getName().compareTo(o2.getKey().getName());
                }
            }
        });

        String[] values = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = list.get(i).getKey().getName() + ", created by " + list.get(i).getValue().get(0);
            int imported = list.get(i).getValue().size() - 1;
            if (imported > 0) {
                values[i] += ", imported by " + imported + " people";
            }
            map.put(values[i], i);
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this.getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                intent.putExtra(ExploreRecipeActivity.RECIPE_KEY, list.get(position).getKey().toString());
                intent.putExtra("isFromExplore", true);
                startActivityForResult(intent, RequestCode.IMPORT_RECIPE.ordinal());
            }
        });

        inputSearch = (EditText) getView().findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs);
                adapter.notifyDataSetChanged();

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Integer origin = map.get(adapter.getItem(position));
                        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                        intent.putExtra(ExploreRecipeActivity.RECIPE_KEY, list.get(origin).getKey().toString());
                        intent.putExtra("isFromExplore", true);
                        startActivityForResult(intent, RequestCode.IMPORT_RECIPE.ordinal());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

        });


    }



    private class ReadOtherRecipeListTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... arg0) {
            IOException ex = null;
            try {
                RecipeRecordApi recipeRecordApi = ServiceBuilderHelper.getBuilder(
                        ExploreOtherRecipeFragment.this.getActivity(),
                        RecipeRecordApi.Builder.class
                ).build();

                List<RecipeRecord> recipeRecords = recipeRecordApi.list()
                        .execute().getItems();

                for (RecipeRecord recipeRecord : recipeRecords) {
                    Recipe recipe = Recipe.fromRecord(recipeRecord);
                    recipes.put(recipe, new ArrayList<String>());
                    recipes.get(recipe).add(recipeRecord.getEmail());
                }

                MealPlanRecordApi mealPlanRecordApi = ServiceBuilderHelper.getBuilder(
                        ExploreOtherRecipeFragment.this.getActivity(),
                        MealPlanRecordApi.Builder.class
                ).build();

                List<MealPlanRecord> mealPlanRecords = mealPlanRecordApi.list()
                        .execute().getItems();

                for (MealPlanRecord mealPlanRecord : mealPlanRecords) {
                    Recipe recipe = MealPlan.fromString(mealPlanRecord.getMealPlan()).getRecipe();
                    if (recipes.containsKey(recipe) && !recipes.get(recipe).get(0).equals(mealPlanRecord.getEmail())) {
                        recipes.get(recipe).add(mealPlanRecord.getEmail());
                    }
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
                            ExploreOtherRecipeFragment.this.getActivity(),
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            ExploreOtherRecipeFragment.this.getActivity(),
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ExploreRecipeActivity.RESULT_OK) {
            if (requestCode == RequestCode.IMPORT_RECIPE.ordinal()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ExploreRecipeActivity.IMPORT_RECIPE,
                        data.getStringExtra(ExploreRecipeActivity.IMPORT_RECIPE));
                getActivity().setResult(ExploreRecipeActivity.RESULT_OK, resultIntent);
                getActivity().finish();
            }
        }
    }
}
