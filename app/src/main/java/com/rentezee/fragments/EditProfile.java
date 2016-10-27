package com.rentezee.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentezee.helpers.BaseFragment;
import com.rentezee.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends BaseFragment {
    private static final String TAG = EditProfile.class.getSimpleName();
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_edit_profile, container, false);
        context=getActivity();
        return view;
    }

}
