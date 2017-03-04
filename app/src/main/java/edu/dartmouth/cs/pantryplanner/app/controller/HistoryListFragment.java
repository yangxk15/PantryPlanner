package edu.dartmouth.cs.pantryplanner.app.controller;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.dartmouth.cs.pantryplanner.app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryListFragment extends Fragment {

    private ArrayList<Fragment> mRecipeFragmentList;
    String[] values = new String[]{"Banana Oatmeal Muffin",
            "Shrimp Pesto Pasta",
            "Broccoli Beef",
            "Honey Mustard Chicken and Avacado Salad ",
            "Healthier Chicken Alfredo Pasta",
            "Easy Breakfast Frittata",
            "Chocolate Strawberry Cream Puffs ",
            "Fluffy Japanese Cheesecake "
    };


    public HistoryListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView_history_list);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this.getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryListFragment.this.getActivity(), RecipeDetailActivity.class);
                intent.putExtra("RecipeName", (String) adapter.getItem(position));
                intent.putExtra("isFromHistory", "true");
                startActivity(intent);
            }
        });
        return view;
    }
}
