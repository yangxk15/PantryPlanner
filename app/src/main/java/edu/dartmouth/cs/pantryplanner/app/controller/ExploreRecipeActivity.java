package edu.dartmouth.cs.pantryplanner.app.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.dartmouth.cs.pantryplanner.app.R;

/**
 * Created by Lucidity on 17/3/3.
 */

public class ExploreRecipeActivity extends AppCompatActivity {
    private ArrayList<Fragment> mFragmentList;
    public static final String USERNAME = "Username";
    final int[] ICONS = new int[]{
            R.drawable.exploremyrecipe,
            R.drawable.exploreotherrecipe,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_recipe);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ExploreMyRecipeFragment());
        mFragmentList.add(new ExploreOtherRecipeFragment());

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
                        return "My Recipe";
                    case 1:
                        return "Other Recipe";
                }
                return null;
            }
        };

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_activity_explore);
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_activity_explore);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
    }
}
