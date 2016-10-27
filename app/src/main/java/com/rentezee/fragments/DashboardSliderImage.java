package com.rentezee.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rentezee.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardSliderImage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_slider_image, container, false);
        ImageView ivDashboardSlider = (ImageView) view.findViewById(R.id.ivDashboardSlider);
        return view;
    }

}
