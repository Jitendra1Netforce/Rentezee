package com.rentezee.main;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rentezee.fragments.EditProfile;
import com.rentezee.fragments.SavedAddress;
import com.rentezee.helpers.BaseActivity;

public class EditProfileAddress extends BaseActivity {

    private final static String TAG=EditProfileAddress.class.getSimpleName();
    private Context context;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_address);

        context=this;
        fragmentManager=getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //find views
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        setUpTabIcons(tabLayout);
        //tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    replaceFragment(new EditProfile());
                }else{
                    replaceFragment(new SavedAddress());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        replaceFragment(new EditProfile());
    }


    private void replaceFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.profileFrame, fragment)
                .commit();
    }

    private void setUpTabIcons(TabLayout tabLayout) {

        View view1 = LayoutInflater.from(this).inflate(R.layout.custom_tab, tabLayout, false);
        TextView tabOne= (TextView) view1.findViewById( R.id.tab);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.profile_tab_icon1, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View view2 = LayoutInflater.from(this).inflate(R.layout.custom_tab, tabLayout, false);
        TextView tabTwo= (TextView) view2.findViewById( R.id.tab);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.profile_tab_icon2, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

}
