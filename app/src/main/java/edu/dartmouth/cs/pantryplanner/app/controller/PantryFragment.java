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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    private Map<PantryItem, Boolean> hasBeenSet = new HashMap<>();
    private Map<PantryItem, Integer> tmpPantryItems;
    private HashSet<PantryItem> selectedItems;
    private ImageButton[] buttons = new ImageButton[4];
    private ReadPantryListTask mTask = null;
    private ChangePantryTask mTask2 = null;

    public PantryFragment() {
        isEdit = false;
        selectedItems = new HashSet<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);
        buttons[0] = (ImageButton) view.findViewById(R.id.imageButton_pantry_edit);
        buttons[1] = (ImageButton) view.findViewById(R.id.imageButton_pantry_complete);
        buttons[2] = (ImageButton) view.findViewById(R.id.imageButton_pantry_cancel);
        buttons[3] = (ImageButton) view.findViewById(R.id.imageButton_pantry_delete);
        buttons[0].setOnClickListener(this);
        buttons[1].setOnClickListener(this);
        buttons[2].setOnClickListener(this);
        buttons[3].setOnClickListener(this);

        mListView = (ListView) view.findViewById(R.id.listView_pantry_list);

        updateFragment(); // load data to adapter
        //PSMScheduler.setSchedule(getContext(), 8,0, 49,0);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_pantry_edit:
                isEdit = true;
                buttons[0].setVisibility(View.GONE);
                buttons[1].setVisibility(View.VISIBLE);
                buttons[2].setVisibility(View.VISIBLE);
                buttons[3].setVisibility(View.VISIBLE);
                updateFragment();
                break;
            case R.id.imageButton_pantry_complete:
                isEdit = false;
                buttons[0].setVisibility(View.VISIBLE);
                buttons[1].setVisibility(View.GONE);
                buttons[2].setVisibility(View.GONE);
                buttons[3].setVisibility(View.GONE);
                for (Iterator<Map.Entry<PantryItem, Integer>> it = tmpPantryItems.entrySet().iterator();
                     it.hasNext();) {
                    Map.Entry<PantryItem, Integer> entry = it.next();
                    if (entry.getValue() == 0) {
                        it.remove();
                    }
                }
                pantryItems = tmpPantryItems;
                new ChangePantryTask().execute();
                break;
            case R.id.imageButton_pantry_cancel:
                isEdit = false;
                Toast.makeText(getActivity(), "Changes discarded", Toast.LENGTH_SHORT).show();
                buttons[0].setVisibility(View.VISIBLE);
                buttons[1].setVisibility(View.GONE);
                buttons[2].setVisibility(View.GONE);
                buttons[3].setVisibility(View.GONE);
                updateFragment();
                break;
            case R.id.imageButton_pantry_delete:
                isEdit = false;
                buttons[0].setVisibility(View.VISIBLE);
                buttons[1].setVisibility(View.GONE);
                buttons[2].setVisibility(View.GONE);
                buttons[3].setVisibility(View.GONE);
                if (pantryItems.size() == 0) break;
                for (PantryItem item : selectedItems) {
                    pantryItems.remove(item);
                }
                new ChangePantryTask().execute();
                break;
        }
    }

    private class PantryListAdapter extends ArrayAdapter<Map.Entry<PantryItem, Integer>> {
        public PantryListAdapter(Context context) {
            super(context, R.layout.list_pantry);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            final Map.Entry<PantryItem, Integer> entry = getItem(position);
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listItemView = convertView;
            if (null == convertView) {
                listItemView = inflater.inflate(R.layout.list_pantry, parent, false);
            }

            final PantryItem curItem = entry.getKey();
            ((TextView) listItemView.findViewById(R.id.pantry_item_name))
                    .setText(curItem.getItem().getName());


            TextView textView = (TextView) listItemView.findViewById(R.id.textView_pantry_list_num);

            if (isEdit) {
                CheckBox cBox = (CheckBox) listItemView.findViewById(R.id.checkBox_list_pantry_item_check);
                final QuantityView quantityView = (QuantityView) listItemView
                        .findViewById(R.id.quantity_pantry_list_item);
                quantityView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
                    @Override
                    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                        tmpPantryItems.put(curItem, quantityView.getQuantity());
                    }

                    @Override
                    public void onLimitReached() {}
                });

                quantityView.setVisibility(View.VISIBLE);
                cBox.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);

                quantityView.setQuantity(entry.getValue());

                if (selectedItems.contains(curItem)) {
                    cBox.setChecked(true);
                }
                cBox.setTag(position); // set the tag so we can identify the correct row in the listener
                cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(curItem);
                        } else {
                            selectedItems.remove(curItem);
                        }
                    }
                }); // set the listener
            } else {
                textView.setText(entry.getValue().toString() + "   Unit  ");
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
        private boolean curEdit;

        public ReadPantryListTask(boolean curEdit) {
            this.curEdit = curEdit;
        }

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
                            new TypeToken<Map<PantryItem, Integer>>() {
                            }.getType()
                    );
                }
                tmpPantryItems = new HashMap<>(pantryItems);

            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            mTask = null;
            if (isCancelled()) {
                return;
            }

            if (ex == null) {
                PantryListAdapter pantryListAdapter = new PantryListAdapter(PantryFragment.this.getActivity());
                List<Map.Entry<PantryItem, Integer>> pantryList = new ArrayList<>(pantryItems.entrySet());

                setNotification(pantryItems);

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

    private class ChangePantryTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... params) {
            String email = new Session(PantryFragment.this.getActivity()).getString("email");
            IOException ex = null;

            try {
                // Update pantry list
                PantryRecordApi pantryRecordApi = ServiceBuilderHelper.getBuilder(
                        PantryFragment.this.getActivity(),
                        PantryRecordApi.Builder.class
                ).build();
                List<PantryRecord> pantryRecords = pantryRecordApi.listWith(
                        new Session(PantryFragment.this.getActivity()).getString("email")
                ).execute().getItems();

                if (pantryRecords == null) {
                    PantryRecord pantryRecord = new PantryRecord();
                    pantryRecord.setEmail(email);
                    pantryRecord.setPantryList(
                            new GsonBuilder().enableComplexMapKeySerialization()
                                    .create().toJson(pantryItems)
                    );
                    pantryRecordApi.insert(pantryRecord).execute();
                } else {
                    PantryRecord pantryRecord = pantryRecords.get(0);
                    pantryRecord.setPantryList(
                            new GsonBuilder().enableComplexMapKeySerialization()
                                    .create().toJson(pantryItems)
                    );
                    pantryRecordApi.update(pantryRecord.getId(), pantryRecord).execute();
                }

            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            mTask2 = null;
            if (isCancelled()) {
                return;
            }

            if (ex == null) {
                Toast.makeText(getActivity(), "Pantry list changed", Toast.LENGTH_SHORT).show();
                PantryListAdapter pantryListAdapter = new PantryListAdapter(PantryFragment.this.getActivity());
                List<Map.Entry<PantryItem, Integer>> pantryList = new ArrayList<>(pantryItems.entrySet());
                setNotification(pantryItems);
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
        if (getActivity() != null) {
            mTask = new ReadPantryListTask(isEdit);
            mTask.execute();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mTask != null) {
            mTask.cancel(true);
        }
        if (mTask2 != null) {
            mTask2.cancel(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedItems.clear();
        if (isEdit) {
            buttons[0].setVisibility(View.VISIBLE);
            buttons[1].setVisibility(View.GONE);
            buttons[2].setVisibility(View.GONE);
            buttons[3].setVisibility(View.GONE);
            isEdit = false;
        }
        updateFragment();
    }

    private void setNotification(Map<PantryItem, Integer> pantryItems){
        if (pantryItems != null && pantryItems.size() != 0) {
            Log.d("pantryitems","!=0");
            Date date = new Date();
            int hour = date.getHours();
            int day = date.getDate();
            Log.d("hour", Integer.toString(hour));
            Log.d("day", Integer.toString(day));
            for (PantryItem pantryItem : pantryItems.keySet()) {
                if ( hasBeenSet.containsKey(pantryItem) && hasBeenSet.get(pantryItem)){
                    continue;
                }
                int minites = pantryItem.getLeftDays();
                if (minites > 0) {
                    int curMin = date.getMinutes();
                    Log.d("min left", Integer.toString(minites));
                    Log.d("curMin", Integer.toString(curMin));
                    PSMScheduler psmScheduler = new PSMScheduler();
                    psmScheduler.setSchedule(getContext(), day, hour,
                            minites + curMin, 0);
                    hasBeenSet.put(pantryItem, true);
                }
            }
        }
    }
}
