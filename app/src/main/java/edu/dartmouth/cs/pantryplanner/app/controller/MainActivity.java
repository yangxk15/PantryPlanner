package edu.dartmouth.cs.pantryplanner.app.controller;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.util.FragmentUtil;

public class MainActivity extends AppCompatActivity {

    private List<FragmentUtil> mFragmentList;
    public static final String USERNAME = "Username";

    final int[] ICONS = new int[]{
            R.drawable.box3,
            R.drawable.calendar3,
            R.drawable.cart3,
            R.drawable.history3,
            R.drawable.setting3

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentList = new ArrayList<>();

        mFragmentList.add(new PantryFragment());
        mFragmentList.add(new MealPlanFragment());
        mFragmentList.add(new ShoppingListFragment());
        mFragmentList.add(new HistoryListFragment());
        mFragmentList.add(new SettingFragment());

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return (Fragment) mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentList.get(position).getFragmentName();
            }
        };

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < mFragmentList.size(); i++) {
            tabLayout.getTabAt(i).setIcon(ICONS[i]);
        }

        loadSetting();

        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
                    @Override
                    public void onPageSelected(int position) {
                        mFragmentList.get(position).updateFragment();
                    }
                }
        );
    }

    private void loadSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences(SettingFragment.PREFER_KEY, MODE_PRIVATE);
        MealPlanFragment.mMealNumber = sharedPreferences.getInt(SettingFragment.MEAL_DAY_KEY, 7);
    }
}
