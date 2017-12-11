package com.example.mradmin.androidnavapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mradmin.androidnavapp.Fragments.SignInFragment;
import com.example.mradmin.androidnavapp.Fragments.SignUpFragment;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;

public class AuthActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //for extra------------------
        final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        if (sharedPreferencesUser.contains("token")) {

            if (sharedPreferencesUser.contains("user_id")) {

                startActivity(new Intent(AuthActivity.this, MainActivity.class));
            }
        }
        //--------------------------

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: {
                    SignInFragment signInFragment = new SignInFragment();
                    return signInFragment;
                }
                case 1: {
                    SignUpFragment signUpFragment = new SignUpFragment();
                    return signUpFragment;
                }
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SIGN IN";
                case 1:
                    return "SIGN UP";
            }
            return null;
        }
    }
}
