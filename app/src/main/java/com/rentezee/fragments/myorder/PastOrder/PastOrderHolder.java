package com.rentezee.fragments.myorder.PastOrder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/21/2016.
 */
public class PastOrderHolder extends RecyclerView.ViewHolder
{

    View view;
    TextView tv_f_date,tv_l_date,tv_time;
    Button buttonPrice;


    public PastOrderHolder(View itemView)
    {
        super(itemView);

        buttonPrice = (Button) itemView.findViewById(R.id.buttonPrice);

        tv_f_date = (TextView) itemView.findViewById(R.id.tv_f_date);

        tv_l_date = (TextView) itemView.findViewById(R.id.tv_l_date);

        tv_time= (TextView) itemView.findViewById(R.id.tv_time);

        view = itemView;
    }



}
