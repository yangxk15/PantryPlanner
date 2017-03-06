package edu.dartmouth.cs.pantryplanner.app.controller;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Item;
import edu.dartmouth.cs.pantryplanner.app.model.ItemType;
import edu.dartmouth.cs.pantryplanner.app.model.PantryItem;
import edu.dartmouth.cs.pantryplanner.app.util.FragmentUtil;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.pantryRecordApi.PantryRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.pantryRecordApi.model.PantryRecord;
import edu.dartmouth.cs.pantryplanner.backend.entity.shoppingListRecordApi.ShoppingListRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.shoppingListRecordApi.model.ShoppingListRecord;
import me.himanshusoni.quantityview.QuantityView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment implements ImageButton.OnClickListener, FragmentUtil {
    Map<Item, Integer> selectedItems;

    Map<Item, Integer> mShoppingListItems;

    // UI Reference
    ExpandableListView mListView;

    public ShoppingListFragment() {
        selectedItems = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        mListView = (ExpandableListView) view.findViewById(R.id.expandableListView_shopping_list);
        view.findViewById(R.id.complete_shopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItems.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select at least one item", Toast.LENGTH_SHORT).show();
                    return;
                }
                new CompleteShoppingTask().execute();
            }
        });
        return view;
    }


    private void dataProcess() {
        /*  ArrayList<ArrayList<<Item>>
                      Type      Item
         */

        ArrayList<ArrayList<Map.Entry<Item, Integer>>> typeList = new ArrayList<>();
        for (int i = 0; i < ItemType.values().length; ++i) {
            typeList.add(new ArrayList<Map.Entry<Item, Integer>>());
        }

        for (Map.Entry<Item, Integer> entry : mShoppingListItems.entrySet()) {
            typeList.get(entry.getKey().getItemType().ordinal()).add(entry);
        }

        mListView.setAdapter(new ShoppingListAdapter(this.getActivity(), typeList));

        for (int i = ItemType.values().length - 1; i >= 0; --i) {
            mListView.expandGroup(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_shop_add:
                Log.d("Click", "imageButton");
                //TODO: add shopping entry
        }
    }

    private class ShoppingListAdapter extends BaseExpandableListAdapter {
        ArrayList<ArrayList<Map.Entry<Item, Integer>>> groupList;
        Context context;

        public ShoppingListAdapter(Context context,
                               ArrayList<ArrayList<Map.Entry<Item, Integer>>> groupList) {
            this.context = context;
            this.groupList = groupList;
        }

        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return ((ArrayList) getGroup(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return ((ArrayList)getGroup(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_shoping_list_type, parent, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView_shop_image);
            switch (groupPosition) {
                case 0:
                    imageView.setImageResource(R.drawable.meat);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.milk);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.apple);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.vegetable);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.ingredient);
                    break;
                default:
                    imageView.setImageResource(R.drawable.others);
                    break;
            }
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton_shop_add);
            imageButton.setFocusable(false);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click", "imageButton");

                }
            });
            TextView textView = (TextView) view.findViewById(R.id.textView_shop_type);
            textView.setText(ItemType.values()[groupPosition].toString());
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_shoping_list_item, parent, false);

            final Map.Entry<Item, Integer> item = (Map.Entry<Item, Integer>) getChild(groupPosition, childPosition);

            ((TextView) view.findViewById(R.id.textView_shop_item)).setText(item.getKey().getName());
            ((QuantityView) view.findViewById(R.id.quantityView_shop_beaf)).setQuantity(item.getValue());

            CheckBox cBox = (CheckBox) view.findViewById(R.id.checkBox_shop_item_check);
            if (selectedItems.containsKey(item.getKey())) {
                cBox.setChecked(true);
            }

            // TODO: click complete button, reduce pantry storage
            cBox.setTag(Integer.valueOf(groupPosition * ItemType.values().length + childPosition)); // set the tag so we can identify the correct row in the listener
            cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedItems.put(item.getKey(), item.getValue());
                        // Log.d("add item", item.getName());
                    } else {
                        selectedItems.remove(item.getKey());
                        // Log.d("remove item", item.getName());
                    }
                }
            }); // set the listener
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    private class ReadShoppingListTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... params) {
            IOException ex = null;

            try {
                ShoppingListRecordApi shoppingListRecordApi = ServiceBuilderHelper.getBuilder(
                        ShoppingListFragment.this.getActivity(),
                        ShoppingListRecordApi.Builder.class
                ).build();

                List<ShoppingListRecord> shoppingListRecords = shoppingListRecordApi.listWith(
                        new Session(ShoppingListFragment.this.getActivity()).getString("email")
                ).execute().getItems();

                if (shoppingListRecords == null) {
                    mShoppingListItems = new HashMap<>();
                } else {
                    mShoppingListItems = new Gson().fromJson(
                            shoppingListRecords.get(0).getShoppingList(),
                            new TypeToken<Map<Item, Integer>>(){}.getType()
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
                dataProcess();
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            ShoppingListFragment.this.getActivity(),
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            ShoppingListFragment.this.getActivity(),
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
        }
    }

    private class CompleteShoppingTask extends AsyncTask<Void, Void, IOException> {
        @Override
        protected IOException doInBackground(Void... params) {
            String email = new Session(ShoppingListFragment.this.getActivity()).getString("email");
            IOException ex = null;

            try {
                // Update shopping list
                ShoppingListRecordApi shoppingListRecordApi = ServiceBuilderHelper.getBuilder(
                        ShoppingListFragment.this.getActivity(),
                        ShoppingListRecordApi.Builder.class
                ).build();

                List<ShoppingListRecord> shoppingListRecords =
                        shoppingListRecordApi.listWith(email).execute().getItems();

                Map<Item, Integer> tempItems = new HashMap<>(mShoppingListItems);
                tempItems.keySet().removeAll(selectedItems.keySet());

                shoppingListRecords.get(0).setShoppingList(
                        new GsonBuilder().enableComplexMapKeySerialization()
                                .create().toJson(tempItems)
                );
                shoppingListRecordApi.update(shoppingListRecords.get(0).getId(), shoppingListRecords.get(0)).execute();

                Map<PantryItem, Integer> pantryItems = new HashMap<>(selectedItems.size());
                for (Map.Entry<Item, Integer> entry : selectedItems.entrySet()) {
                    pantryItems.put(
                           new PantryItem(Calendar.getInstance().getTime(), entry.getKey()),
                            entry.getValue()
                    );
                }

                // Add to pantry
                PantryRecordApi pantryRecordApi = ServiceBuilderHelper.getBuilder(
                        ShoppingListFragment.this.getActivity(),
                        PantryRecordApi.Builder.class
                ).build();

                List<PantryRecord> pantryRecords =
                        pantryRecordApi.listWith(email).execute().getItems();

                if (pantryRecords == null) {
                    PantryRecord pantryRecord = new PantryRecord();
                    pantryRecord.setEmail(email);
                    pantryRecord.setPantryList(
                            new GsonBuilder().enableComplexMapKeySerialization()
                                    .create().toJson(pantryItems)
                    );
                    pantryRecordApi.insert(pantryRecord).execute();
                    Log.d("ShoppingListFragment", "new pantry record is " + pantryItems.toString());
                } else {
                    PantryRecord pantryRecord = pantryRecords.get(0);
                    Map<PantryItem, Integer> oldList = new Gson().fromJson(
                            pantryRecord.getPantryList(),
                            new TypeToken<Map<PantryItem, Integer>>(){}.getType()
                    );
                    for (Map.Entry<PantryItem, Integer> entry : pantryItems.entrySet()) {
                        if (!oldList.containsKey(entry.getKey())) {
                            oldList.put(entry.getKey(), entry.getValue());
                        } else {
                            oldList.put(entry.getKey(), entry.getValue() + oldList.get(entry.getKey()));
                        }
                    }
                    pantryRecord.setPantryList(
                            new GsonBuilder().enableComplexMapKeySerialization()
                            .create().toJson(oldList)
                    );
                    pantryRecordApi.update(pantryRecord.getId(), pantryRecord).execute();
                    Log.d("ShoppingListFragment", "now pantry record is " + oldList);
                }
                Log.d("ShoppingListFragment", "Current shopping list is " + mShoppingListItems.toString());

            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            if (ex == null) {
                mShoppingListItems.keySet().removeAll(selectedItems.keySet());
                selectedItems.clear();
                Toast.makeText(
                        ShoppingListFragment.this.getActivity(),
                        "Shopping list updated",
                        Toast.LENGTH_SHORT
                ).show();
                dataProcess();
            } else {
                Log.d(this.getClass().getName(), ex.toString());
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    Toast.makeText(
                            ShoppingListFragment.this.getActivity(),
                            error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            ShoppingListFragment.this.getActivity(),
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        }
    }

    @Override
    public String getFragmentName() {
        return "Shop";
    }

    @Override
    public void updateFragment() {
        new ReadShoppingListTask().execute();
    }
}
