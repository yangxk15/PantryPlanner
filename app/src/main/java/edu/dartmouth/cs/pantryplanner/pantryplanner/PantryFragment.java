package edu.dartmouth.cs.pantryplanner.pantryplanner;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


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
        adapter.add("hehe");
        listView.setAdapter(adapter);
        return view;
    }


    private class PantryListAdapter extends ArrayAdapter<String> {


        public PantryListAdapter(Context context) {

            super(context, R.layout.pantry_list);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View listItemView = convertView;
            if (null == convertView) {
                // we need to check if the convertView is null. If it's null,
                // then inflate it.
                listItemView = inflater.inflate(
                        R.layout.pantry_list, parent, false);
            }

            ((TextView) listItemView.findViewById(R.id.textView_days)).setText("Days");
            ((TextView) listItemView.findViewById(R.id.textView_item_name)).setText("Item name");
            ((TextView) listItemView.findViewById(R.id.textView_amount_unit)).setText("Unit");

//            TextView item_name = (TextView) listItemView.findViewById(R.id.textView_item_name);
//            TextView item_unit = (TextView) listItemView.findViewById(R.id.textView_amount_unit);



            return listItemView;
        }
    }


}
