package com.rentezee.navigation.rent_it_out;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rentezee.navigation.rent_it_out.productview.ProductViewFragment;
import com.rentezee.navigation.rent_it_out.upload_product.UploadProductFragment;

/**
 * Created by John on 11/21/2016.
 */
public class RentAdapter extends FragmentStatePagerAdapter
{

    int mNumOfTabs;

    ProductViewFragment productViewFragment;
    UploadProductFragment uploadProductFragment;

    public RentAdapter(FragmentManager fm, int NumOfTabs)
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
                productViewFragment = new ProductViewFragment();
                return productViewFragment;

            default:
                uploadProductFragment = new UploadProductFragment();
                return uploadProductFragment;


        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}