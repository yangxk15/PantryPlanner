package edu.dartmouth.cs.pantryplanner.app.controller;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.PantryItem;
import edu.dartmouth.cs.pantryplanner.app.util.FragmentUtil;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.pantryRecordApi.PantryRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.pantryRecordApi.model.PantryRecord;
import me.himanshusoni.quantityview.QuantityView;


/**
 * A simple {@link Fragment} subclass.
 */

public class PantryFragment extends Fragment implements Button.OnClickListener, FragmentUtil {
    private boolean isEdit;
    private ListView mListView;
    private Map<PantryItem, Integer> pantryItems;

    private ImageButton[] buttons = new ImageButton[3];

    public PantryFragment() {
        isEdit = false;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);
        buttons[0] = (ImageButton) view.findViewById(R.id.imageButton_pantry_edit);
        buttons[1] = (ImageButton) view.findViewById(R.id.imageButton_pantry_complete);
        buttons[2] = (ImageButton) view.findViewById(R.id.imageButton_pantry_delete);
        buttons[0].setOnClickListener(this);
        buttons[1].setOnClickListener(this);
        buttons[2].setOnClickListener(this);
        buttons[1].setVisibility(View.GONE);
        buttons[2].setVisibility(View.GONE);

        mListView = (ListView) view.findViewById(R.id.listView_pantry_list);

        updateFragment();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.imageButton_pantry_add:
            //    MyDialogFragment dialogFragment = MyDialogFragment.newInstance(0);
            //    dialogFragment.show(getFragmentManager(), "DIALOG_FRAGMENT");
            //    break;
            case R.id.imageButton_pantry_edit:
                isEdit = true;
                buttons[0].setVisibility(View.GONE);
                buttons[1].setVisibility(View.VISIBLE);
                buttons[2].setVisibility(View.VISIBLE);
                //updateFragment();
                break;
            case R.id.imageButton_pantry_complete:
                isEdit = false;
                buttons[0].setVisibility(View.VISIBLE);
                buttons[1].setVisibility(View.GONE);
                buttons[2].setVisibility(View.GONE);
                //updateFragment();
                break;
            case R.id.imageButton_pantry_delete:
                isEdit = false;
                buttons[0].setVisibility(View.VISIBLE);
                buttons[1].setVisibility(View.GONE);
                buttons[2].setVisibility(View.GONE);
                //updateFragment();
                break;
        }
        updateFragment();
    }

    private class PantryListAdapter extends ArrayAdapter<Map.Entry<PantryItem, Integer>> {
        public PantryListAdapter(Context context) {
            super(context, R.layout.list_pantry);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Map.Entry<PantryItem, Integer> entry = getItem(position);
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listItemView = convertView;
            if (null == convertView) {
                listItemView = inflater.inflate(R.layout.list_pantry, parent, false);
            }

            ((TextView) listItemView.findViewById(R.id.pantry_item_name))
                    .setText(entry.getKey().getItem().getName());

            QuantityView quantityView = (QuantityView) listItemView
                    .findViewById(R.id.quantity_pantry_list_item);
            TextView textView = (TextView) listItemView.findViewById(R.id.textView_pantry_list_num);

            if (isEdit) {
                quantityView.setQuantity(entry.getValue());
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(entry.getValue().toString());
                quantityView.setVisibility(View.GONE);
            }

            if (entry.getKey().getLeftDays() > 0) {
                ((TextView) listItemView.findViewById(R.id.pantry_item_left_days))
                        .setText(String.valueOf(entry.getKey().getLeftDays()));
            } else {
                ((TextView) listItemView.findViewById(R.id.expires_in)).setText("EXPIRED");
                ((TextView) listItemView.findViewById(R.id.pantry_item_left_days)).setText("");
                ((TextView) listItemView.findViewById(R.id.days)).setText("");
            }
            return listItemView;
        }
    }

    private class ReadPantryListTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... params) {

            IOException ex = null;
            try {
                PantryRecordApi pantryRecordApi = ServiceBuilderHelper.getBuilder(
                        PantryFragment.this.getActivity(),
                        PantryRecordApi.Builder.class
                ).build();
                List<PantryRecord> pantryRecords = pantryRecordApi.listWith(
                        new Session(PantryFragment.this.getActivity()).getString("email")
                ).execute().getItems();
                if (pantryRecords == null) {
                    pantryItems = new HashMap<>();
                } else {
                    pantryItems = new Gson().fromJson(
                            pantryRecords.get(0).getPantryList(),
                            new TypeToken<Map<PantryItem, Integer>>(){}.getType()
                    );
                }
            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            if (ex == null) {
                PantryListAdapter pantryListAdapter = new PantryListAdapter(PantryFragment.this.getActivity());
                List<Map.Entry<PantryItem, Integer>> pantryList = new ArrayList<>(pantryItems.entrySet());
                Collections.sort(pantryList, new Comparator<Map.Entry<PantryItem, Integer>>() {
                    @Override
                    public int compare(Map.Entry<PantryItem, Integer> o1, Map.Entry<PantryItem, Integer> o2) {
                        return o1.getKey().getLeftDays() - o2.getKey().getLeftDays();
                    }
                });
                pantryListAdapter.addAll(pantryList);
                mListView.setAdapter(pantryListAdapter);
                pantryListAdapter.notifyDataSetChanged();
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            PantryFragment.this.getActivity(),
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            PantryFragment.this.getActivity(),
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
        return "Pantry";
    }

    @Override
    public void updateFragment() {
        new ReadPantryListTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isEdit != false) {
            buttons[0].setVisibility(View.VISIBLE);
            buttons[1].setVisibility(View.GONE);
            buttons[2].setVisibility(View.GONE);
            isEdit = false;
            updateFragment();
        }
    }
}
