package com.rentezee.fragments.profile.general;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rentezee.fragments.profile.ProfileFragment;
import com.rentezee.fragments.profile.general.address.AddressFragment;
import com.rentezee.fragments.rent_it_out.upload_product.UploadProductFragment;

/**
 * Created by hegazy on 2/13/15.
 */
public class CustomViewPager extends FragmentStatePagerAdapter
{

    int mNumOfTabs;
    ProfileFragment profileFragment;
    AddressFragment addressFragment;
    UploadProductFragment uploadProductFragment;


    public CustomViewPager(FragmentManager fm, int NumOfTabs)
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
                profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                addressFragment = new AddressFragment();
                return addressFragment;
            default:
                profileFragment = new ProfileFragment();
                return profileFragment;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}