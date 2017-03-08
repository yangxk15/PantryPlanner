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
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;
import edu.dartmouth.cs.pantryplanner.app.util.RequestCode;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.RecipeRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.model.RecipeRecord;

/**
 * Created by Lucidity on 17/3/5.
 */

public class ExploreMyRecipeFragment extends Fragment {
    private List<Recipe> recipes;
    // UI Reference
    private ListView mListView;

    private ReadMyRecipeListTask mTask = null;

    public ExploreMyRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_my_recipe, container, false);
        mListView = (ListView) view.findViewById(R.id.listView_explore_my_list);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTask = new ReadMyRecipeListTask();
        mTask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTask != null) {
            mTask.cancel(true);
        }
    }

    private void dataProcess() {
        String[] values = new String[recipes.size()];
        for (int i = 0; i < values.length; ++i) {
            values[i] = recipes.get(i).getName();
        }

        // adapter to display recipe names
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this.getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                intent.putExtra(ExploreRecipeActivity.RECIPE_KEY, recipes.get(position).toString());
                intent.putExtra("isFromExplore", true);
                startActivityForResult(intent, RequestCode.IMPORT_RECIPE.ordinal());
            }
        });
    }

    private class ReadMyRecipeListTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... arg0) {
            IOException ex = null;
            try {
                RecipeRecordApi recipeRecordApi = ServiceBuilderHelper.getBuilder(
                        ExploreMyRecipeFragment.this.getActivity(),
                        RecipeRecordApi.Builder.class
                ).build();

                List<RecipeRecord> recipeRecords = recipeRecordApi.listWith(
                        new Session(ExploreMyRecipeFragment.this.getActivity()).getString("email")
                ).execute().getItems();

                if (recipeRecords == null) {
                    recipes = new ArrayList<>();
                    Log.d("no", "record");
                } else {
                    recipes = Recipe.fromRecipeRecordList(recipeRecords);
                }
            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            if (isCancelled()) {
                return;
            }

            if (ex == null) {
                dataProcess();
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            ExploreMyRecipeFragment.this.getActivity(),
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            ExploreMyRecipeFragment.this.getActivity(),
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
            mTask = null;
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
