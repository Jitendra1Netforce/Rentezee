package com.rentezee.navigation;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rentezee.helpers.Constants;
import com.rentezee.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardSliderImage extends Fragment {

    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_slider_image, container, false);

        context=getActivity();

        ImageView ivSliderImage = (ImageView) view.findViewById(R.id.ivSliderImage);

        System.out.println("image============="+getArguments().getString(Constants.URL));

        Glide.with(context)
                .load(getArguments().getString(Constants.URL))
                .centerCrop()
                //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(ivSliderImage);
        return view;
    }

}
