package edu.dartmouth.cs.pantryplanner.pantryplanner.controller;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.pantryplanner.R;
import edu.dartmouth.cs.pantryplanner.pantryplanner.model.Item;
import edu.dartmouth.cs.pantryplanner.pantryplanner.model.ItemType;
import me.himanshusoni.quantityview.QuantityView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PantryFragment extends Fragment {


    public PantryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        ListView listView = (ListView) view.findViewById(R.id.pantry_list);
        PantryListAdapter adapter = new PantryListAdapter(getActivity());

        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Beef", ItemType.MEAT), 5));
        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Apple", ItemType.FRUIT), 4));
        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Soy sauce", ItemType.INGREDIENT), 1));
        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Skim milk", ItemType.DIARY), 15));
        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Spinach", ItemType.VEGETABLE), 35));

        listView.setAdapter(adapter);

        return view;
    }


    private class PantryListAdapter extends ArrayAdapter<Map.Entry<Item, Integer>> {
        public PantryListAdapter(Context context) {
            super(context, R.layout.pantry_list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            Map.Entry<Item, Integer> entry = getItem(position);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View listItemView = convertView;
            if (null == convertView) {
                // we need to check if the convertView is null. If it's null,
                // then inflate it.
                listItemView = inflater.inflate(
                        R.layout.pantry_list, parent, false);
            }

            ((QuantityView) listItemView.findViewById(R.id.pantry_item_countdown_days))
                    .setQuantity(entry.getKey().getCountdownDays());
            ((TextView) listItemView.findViewById(R.id.pantry_item_name))
                    .setText(entry.getKey().getName());
            ((QuantityView) listItemView.findViewById(R.id.pantry_item_number))
                    .setQuantity(entry.getValue());

            return listItemView;
        }
    }


}
