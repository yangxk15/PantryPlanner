package edu.dartmouth.cs.pantryplanner.app.controller;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.MealPlan;
import edu.dartmouth.cs.pantryplanner.app.util.FragmentUtil;
import me.himanshusoni.quantityview.QuantityView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements QuantityView.OnQuantityChangeListener, FragmentUtil {
    public static final String MEAL_DAY_KEY = "get_meal_plan_day";
    public static final String PREFER_KEY = "my_prefer";
    public static final int MODE_PRIVATE = 0;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageview_setting_photo);
        imageView.setImageResource(R.drawable.default_photo);

        QuantityView quantityView = (QuantityView) view.findViewById(R.id.quantityView_plan_dates);
        quantityView.setOnQuantityChangeListener(this);

        // load quantity
        quantityView.setQuantity(MealPlanFragment.mMealNumber);

        TextView textView = (TextView) view.findViewById(R.id.textView_setting_username);
        textView.setText("Welcome :) " + "Foo!");
    }

    @Override
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        MealPlanFragment.mMealNumber = newQuantity;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFER_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putInt(MEAL_DAY_KEY, newQuantity);
        editor.apply();
    }

    @Override
    public void onLimitReached() {}

    @Override
    public String getFragmentName() {
        return "Setting";
    }

    @Override
    public void updateFragment() {

    }
}
