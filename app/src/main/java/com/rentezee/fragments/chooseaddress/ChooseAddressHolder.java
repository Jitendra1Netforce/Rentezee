package com.rentezee.fragments.chooseaddress;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/28/2016.
 */
public class ChooseAddressHolder extends RecyclerView.ViewHolder
{

    View view;
    TextView txtAddressheading,txtAddress,txtAddressDetails,txtAddressCity;
    ImageView imgEditAddress;
    RelativeLayout relativeLayout;

    public ChooseAddressHolder(View itemView)
    {
        super(itemView);

        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);

        imgEditAddress = (ImageView)  itemView.findViewById(R.id.imgEditAddress);

        txtAddressheading = (TextView) itemView.findViewById(R.id.txtAddressheading);

        txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);

        txtAddressDetails = (TextView) itemView.findViewById(R.id.txtAddressDetails);

        txtAddressCity = (TextView) itemView.findViewById(R.id.txtAddressCity);

        view = itemView;
    }



}