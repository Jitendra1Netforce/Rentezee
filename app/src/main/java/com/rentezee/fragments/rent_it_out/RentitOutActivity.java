package com.rentezee.fragments.rent_it_out;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.rentezee.fragments.myorder.MyOrderViewPager;
import com.rentezee.fragments.profile.general.WrapContentViewPager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;

public class RentitOutActivity extends BaseActivity
{

    WrapContentViewPager viewPager;
    DashboardContainer dashboardContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentit_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rent It Out");
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

        tabLayout.addTab(tabLayout.newTab().setText("Product View"));
        tabLayout.addTab(tabLayout.newTab().setText("Upload Product"));
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#EF7609"));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final RentAdapter adapter = new RentAdapter(getSupportFragmentManager() , tabLayout.getTabCount());
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