package com.rentezee.fragments.myorder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.rentezee.fragments.profile.general.CustomViewPager;
import com.rentezee.fragments.profile.general.WrapContentViewPager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;

public class MyOrder extends BaseActivity {


    WrapContentViewPager viewPager;
    DashboardContainer dashboardContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Order");
        }

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();

        setupTab();

    }

    private void setupTab()
    {
        viewPager = (WrapContentViewPager) findViewById(R.id.pager);
        viewPager.setPagingEnabled(true);
        viewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Active Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Past Order"));
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#EF7609"));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MyOrderViewPager adapter = new MyOrderViewPager(getSupportFragmentManager() , tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();


        try {
            invalidateOptionsMenu();
        } catch (Exception e) {

        }

        dashboardContainer.count_cart();

    }



}