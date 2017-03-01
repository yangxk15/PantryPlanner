package edu.dartmouth.cs.pantryplanner.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.util.RequestCode;
import edu.dartmouth.cs.pantryplanner.backend.registration.Registration;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
