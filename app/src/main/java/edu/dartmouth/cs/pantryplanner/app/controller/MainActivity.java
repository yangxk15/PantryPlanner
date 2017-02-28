package edu.dartmouth.cs.pantryplanner.app.controller;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.util.RequestCode;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Fragment> mFragmentList;

    public static final String USERNAME = "Username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 2/27/17 Login logic
        String username = this.getSharedPreferences(getString(R.string.app_domain), MODE_PRIVATE)
                .getString(USERNAME, null);
        if (username == null) {
            startActivityForResult(new Intent(this, LoginActivity.class), RequestCode.LOGIN);
        } else {
            setup();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.LOGIN) {
            switch (resultCode) {
                case RESULT_OK:
                    this.getSharedPreferences(getString(R.string.app_domain), MODE_PRIVATE)
                            .edit().putString(USERNAME, data.getStringExtra(USERNAME)).apply();
                    setup();
                    break;
                case RESULT_CANCELED:
                    finish();
            }
        }
    }

    private void setup() {
        setContentView(R.layout.activity_main);

        mFragmentList = new ArrayList<>();

        mFragmentList.add(new PantryFragment());
        mFragmentList.add(new MealPlanFragment());
        mFragmentList.add(new ShoppingListFragment());
        mFragmentList.add(new RecipeListFragment());
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
                        return "Recipe";
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
    }
}
