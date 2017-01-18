package com.rentezee.navigation.myorder;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.Login;
import com.rentezee.navigation.profile.general.WrapContentViewPager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

public class MyOrder extends BaseActivity {


    WrapContentViewPager viewPager;
    DashboardContainer dashboardContainer;
    String device_id;
    public static String id, user_id;
    long userId;
    User user;


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

        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        user = (User) new AppPreferenceManager(getApplicationContext()).getObject(PreferenceKeys.savedUser, User.class);

        if (user != null)
        {


            userId = user.getUserId();
            user_id = Long.toString(userId);


        }
        else
        {

            Intent i = new Intent(MyOrder.this, Login.class);
            startActivity(i);
            finish();

        }




       /*  dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();
       */


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

       // dashboardContainer.count_cart();
        count_cart();

    }




    public  void count_cart()
    {
        // recyclerView.setVisibility(View.GONE);
        // homeDatas.clear();

        System.out.println("device_id-------------" + device_id);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device_id", device_id);


        Ion.with(this)
                .load("https://netforcesales.com/renteeze/webservice/Pages/dashboard.json")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        //                        System.out.println("data================" + result.toString());

                        if (result != null) {
                            JsonObject v = result.getAsJsonObject("data");

                            try {

                                String my_cart_c = v.get("my_cart").getAsString();

                                int new_my_cart = Integer.parseInt(my_cart_c);

                                tvCartCount.setText(String.valueOf(new_my_cart));


                            } catch (Exception cart) {
                            }

                            //setMenuCounter(R.id.nav_cart, new_my_cart);
                        } else {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });


    }





}