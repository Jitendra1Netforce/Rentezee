package com.rentezee.navigation.myorder.PastOrder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/21/2016.
 */
public class PastOrderHolder extends RecyclerView.ViewHolder
{

    View view;
    TextView tv_f_date,tv_l_date,tv_time,tvPoint1,tvPoint2,tvPoint3,tvPoint4,tvLine3,tvLine2,tvLine1;
    Button buttonPrice,buttonViewDetail;
    RelativeLayout relativeMyorder,relativeDescription;



    public PastOrderHolder(View itemView)
    {
        super(itemView);

        relativeMyorder = (RelativeLayout) itemView.findViewById(R.id.relativeMyorder);

        relativeDescription = (RelativeLayout) itemView.findViewById(R.id.relativeDescription);

        buttonPrice = (Button) itemView.findViewById(R.id.buttonPrice);

        tv_f_date = (TextView) itemView.findViewById(R.id.tv_f_date);

        tv_l_date = (TextView) itemView.findViewById(R.id.tv_l_date);

        tv_time= (TextView) itemView.findViewById(R.id.tv_time);

        tvPoint1 = (TextView) itemView.findViewById(R.id.tvPoint1);

        tvPoint2 = (TextView) itemView.findViewById(R.id.tvPoint2);

        tvPoint3 = (TextView) itemView.findViewById(R.id.tvPoint3);

        tvPoint4 = (TextView) itemView.findViewById(R.id.tvPoint4);

        tvLine1 = (TextView) itemView.findViewById(R.id.tvLine1);

        tvLine2 = (TextView) itemView.findViewById(R.id.tvLine2);

        tvLine3 = (TextView) itemView.findViewById(R.id.tvLine3);

        buttonViewDetail = (Button) itemView.findViewById(R.id.buttonViewDetail);

        view = itemView;
    }



}
