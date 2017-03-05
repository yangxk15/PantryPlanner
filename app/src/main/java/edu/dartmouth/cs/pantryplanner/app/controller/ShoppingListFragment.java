package edu.dartmouth.cs.pantryplanner.app.controller;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Item;
import edu.dartmouth.cs.pantryplanner.app.model.ItemType;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {
    ShoppingListAdapter mShoppingListAdapter;
    static HashSet<Item> selectedItems;

    public ShoppingListFragment() {
        selectedItems = new HashSet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.expandableListView_shopping_list);

        dataProcess();

        listView.setAdapter(mShoppingListAdapter);

        listView.expandGroup(0);
        return view;
    }


    private void dataProcess() {
        /*  ArrayList<ArrayList<<Item>>
                      Type      Item
         */

        Item apple = new Item("Apple", ItemType.FRUIT);
        Item orange = new Item("Orange", ItemType.FRUIT);
        Item beef = new Item("Beef", ItemType.MEAT);
        Item c1 = new Item("Grape", ItemType.FRUIT);
        Item c2 = new Item("Lemon", ItemType.FRUIT);
        Item c3 = new Item("Tuna", ItemType.MEAT);
        Item c4 = new Item("Apple", ItemType.FRUIT);
        Item c5 = new Item("Chicken", ItemType.MEAT);
        Item c6 = new Item("Pork", ItemType.MEAT);

        ArrayList<Item> items = new ArrayList<>();
        items.add(apple);
        items.add(orange);
        items.add(beef);
        items.add(c1);
        items.add(c2);
        items.add(c3);
        items.add(c4);
        items.add(c5);
        items.add(c6);

        ArrayList<ArrayList<Item>> typeList = new ArrayList<>();
        for (int i = 0; i < ItemType.values().length; ++i) {
            typeList.add(new ArrayList<Item>());
        }

        for (Item item : items) {
            typeList.get(item.getItemType().ordinal()).add(item);
        }

        mShoppingListAdapter = new ShoppingListAdapter(this.getActivity(), typeList);
    }

    private class ShoppingListAdapter extends BaseExpandableListAdapter {
        ArrayList<ArrayList<Item>> groupList;
        Context context;

        public ShoppingListAdapter(Context context,
                               ArrayList<ArrayList<Item>> groupList) {
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
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_shoping_list_item, parent, false);

            final Item item = (Item) getChild(groupPosition, childPosition);

            TextView itemName = (TextView) view.findViewById(R.id.textView_shop_item);
            itemName.setText("" + item.getName());

            CheckBox cBox = (CheckBox) view.findViewById(R.id.checkBox_shop_item_check);
            if (selectedItems.contains(item)) {
                cBox.setChecked(true);
            }
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
}
