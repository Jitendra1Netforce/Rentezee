package com.rentezee.navigation.rentenzee_credit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/28/2016.
 */
public class CreditHolder extends RecyclerView.ViewHolder {

    View view;
    TextView txtCreditVia,tvAmount,tvCreated;

    RelativeLayout relativelayout;


    public CreditHolder(View itemView)
    {
        super(itemView);

        relativelayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);

        txtCreditVia = (TextView) itemView.findViewById(R.id.txtcreditBy);

        tvAmount = (TextView) itemView.findViewById(R.id.txtAmount);

        tvCreated = (TextView) itemView.findViewById(R.id.txtDate);


        view = itemView;
    }



}

