package com.rentezee.fragments.profile.general.address;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/25/2016.
 */
public class AddressHolder extends RecyclerView.ViewHolder
{

    View view;
    TextView txtAddressheading,txtAddress,txtAddressDetails,txtAddressCity;
    LinearLayout layoutRemove,layoutMoveToCart;
    RelativeLayout relativeLayout;

    public AddressHolder(View itemView)
    {
        super(itemView);

        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);

        layoutRemove = (LinearLayout) itemView.findViewById(R.id.layoutRemove);

        layoutMoveToCart = (LinearLayout) itemView.findViewById(R.id.layoutMoveToCart);

        txtAddressheading = (TextView) itemView.findViewById(R.id.txtAddressheading);

        txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);

        txtAddressDetails = (TextView) itemView.findViewById(R.id.txtAddressDetails);

        txtAddressCity = (TextView) itemView.findViewById(R.id.txtAddressCity);

        view = itemView;
    }



}
