package com.rentezee.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentezee.helpers.BaseFragment;
import com.rentezee.main.R;

public class Cart1 extends BaseFragment {

    private final static  String TAG=Cart1.class.getSimpleName();
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cart, container, false);
        context=getActivity();
        return view;
    }

}
