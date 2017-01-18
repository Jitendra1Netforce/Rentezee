package com.rentezee.navigation.myorder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.rentezee.navigation.myorder.PastOrder.PastOrderFragment;


/**
 * Created by John on 11/21/2016.
 */
public class MyOrderViewPager extends FragmentStatePagerAdapter
{

    int mNumOfTabs;

    PastOrderFragment pastOrderFragment,pastOrderFragment3;

    public MyOrderViewPager(FragmentManager fm, int NumOfTabs)
    {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {

            case 0:
                pastOrderFragment3 = new PastOrderFragment();
                return pastOrderFragment3;

            default:
                pastOrderFragment = new PastOrderFragment();
                return pastOrderFragment;


        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}