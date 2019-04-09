package com.example.taylor.hardwarespecs;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);




    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CPUSpecs(), "CPU");
        adapter.addFragment(new SystemSpecs(), "System");
        adapter.addFragment(new BatterySpecs(), "Battery");

        viewPager.setAdapter(adapter);
    }

}