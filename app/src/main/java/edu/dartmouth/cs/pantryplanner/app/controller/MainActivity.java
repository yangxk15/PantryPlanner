package edu.dartmouth.cs.pantryplanner.app.controller;

import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import edu.dartmouth.cs.pantryplanner.app.R;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Fragment> mFragmentList;
    public static final String USERNAME = "Username";

    final int[] ICONS = new int[]{
            R.drawable.pantry,
            R.drawable.calendar,
            R.drawable.shop,
            R.drawable.kitchen,
            R.drawable.icon_setting_color

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
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Pantry";
                    case 1:
                        return "Meal";
                    case 2:
                        return "Shop";
                    case 3:
                        return "History";
                    case 4:
                        return "Setting";
                }
                return null;
            }
        };

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
        tabLayout.getTabAt(3).setIcon(ICONS[3]);
        tabLayout.getTabAt(4).setIcon(ICONS[4]);
    }
}
