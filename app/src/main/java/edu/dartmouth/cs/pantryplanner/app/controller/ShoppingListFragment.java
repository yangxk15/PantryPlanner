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
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Item;
import edu.dartmouth.cs.pantryplanner.app.model.ItemType;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.shoppingListRecordApi.ShoppingListRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.shoppingListRecordApi.model.ShoppingListRecord;
import me.himanshusoni.quantityview.QuantityView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment implements ImageButton.OnClickListener {
    HashSet<Map.Entry<Item, Integer>> selectedItems;

    Map<Item, Integer> mShoppingListItems;

    // UI Reference
    ExpandableListView mListView;

    public ShoppingListFragment() {
        selectedItems = new HashSet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        mListView = (ExpandableListView) view.findViewById(R.id.expandableListView_shopping_list);
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
            textView.setText("" + ItemType.values()[groupPosition]);
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
            if (selectedItems.contains(item)) {
                cBox.setChecked(true);
            }

            // TODO: click complete button, reduce pantry storage
            cBox.setTag(Integer.valueOf(groupPosition * ItemType.values().length + childPosition)); // set the tag so we can identify the correct row in the listener
            cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedItems.add(item);
                        // Log.d("add item", item.getName());
                    } else {
                        selectedItems.remove(item);
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

    @Override
    public void onResume() {
        super.onResume();
        new ReadShoppingListTask().execute();
    }
}
