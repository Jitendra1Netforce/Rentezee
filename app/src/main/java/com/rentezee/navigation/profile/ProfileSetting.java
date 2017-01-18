package com.rentezee.navigation.profile;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.navigation.profile.general.CustomViewPager;
import com.rentezee.navigation.profile.general.WrapContentViewPager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;

public class ProfileSetting extends BaseActivity {

    WrapContentViewPager viewPager;
    DashboardContainer dashboardContainer;
    String device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Profile Setting");
        }

        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


       /* dashboardContainer = new DashboardContainer();
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

        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.profile_inactive)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.home_inactive)));

        final int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.black);
        final int tabIconSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.orange);


        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconSelectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
      

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final CustomViewPager adapter = new CustomViewPager(getSupportFragmentManager() , tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(tabIconSelectedColor, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

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

                        if (result != null)
                        {
                            JsonObject v = result.getAsJsonObject("data");

                            try
                            {

                                String my_cart_c = v.get("my_cart").getAsString();

                                int new_my_cart = Integer.parseInt(my_cart_c);

                                tvCartCount.setText(String.valueOf(new_my_cart));


                            }
                            catch (Exception cart){}

                            //setMenuCounter(R.id.nav_cart, new_my_cart);
                        }
                        else
                        {

                            dismissProgressBar();
                            Log.e("error", e.toString());
                        }
                    }
                });


    }



}