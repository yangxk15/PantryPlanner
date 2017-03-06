package edu.dartmouth.cs.pantryplanner.app.controller;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.AbstractMap;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Item;
import edu.dartmouth.cs.pantryplanner.app.model.ItemType;
import me.himanshusoni.quantityview.QuantityView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PantryFragment extends Fragment implements Button.OnClickListener {

    public PantryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView_pantry_list);
        PantryListAdapter adapter = new PantryListAdapter(getActivity());

        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Beef", ItemType.MEAT), 5));
        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Apple", ItemType.FRUIT), 4));
        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Soy sauce", ItemType.INGREDIENT), 1));
        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Skim milk", ItemType.DIARY), 15));
        adapter.add(new AbstractMap.SimpleEntry<>(new Item("Spinach", ItemType.VEGETABLE), 35));

<<<<<<< HEAD
        
=======
>>>>>>> origin/master
        listView.setAdapter(adapter);

        // set buttons listener
        ImageButton btn = (ImageButton) view.findViewById(R.id.button_pantry_add);
        btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_pantry_add:
                MyDialogFragment dialogFragment = MyDialogFragment.newInstance(0);
                dialogFragment.show(getFragmentManager(), "DIALOG_FRAGMENT");
        }
    }

    private class PantryListAdapter extends ArrayAdapter<Map.Entry<Item, Integer>> {
        public PantryListAdapter(Context context) {

            super(context, R.layout.list_pantry);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Map.Entry<Item, Integer> entry = getItem(position);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listItemView = convertView;
            if (null == convertView) {
                listItemView = inflater.inflate(

                        R.layout.list_pantry, parent, false);

            }

//            ((QuantityView) listItemView.findViewById(R.id.pantry_item_countdown_days))
//                    .setQuantity(entry.getKey().getCountdownDays());
            ((TextView) listItemView.findViewById(R.id.pantry_item_name))
                    .setText(entry.getKey().getName());
            ((QuantityView) listItemView.findViewById(R.id.pantry_item_number))
                    .setQuantity(entry.getValue());

            return listItemView;
        }
    }
}
