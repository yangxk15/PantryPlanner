package edu.dartmouth.cs.pantryplanner.pantryplanner.controller;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

import edu.dartmouth.cs.pantryplanner.pantryplanner.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

    TabHost tabHost;

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


    public RecipeListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.my_recipe_list);
//        RecipeListAdapter adapter = new RecipeListAdapter(getActivity());
//        adapter.add("hehe");
//        listView.setAdapter(adapter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this.getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);
        return view;

    }

//    private class RecipeListAdapter extends ArrayAdapter<String> {
//
//
//        public RecipeListAdapter(Context context) {
//
//            super(context, R.layout.);
//
//        }

//

}
