package com.rentezee.fragments.rent_it_out.productview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/21/2016.
 */
public class ProductViewHolder extends RecyclerView.ViewHolder
{

    View view;
    TextView tv_first_date,second_date,tv_product_condition;

    RelativeLayout relativelayout;

    public ProductViewHolder(View itemView)
    {
        super(itemView);
        relativelayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_container);

        tv_first_date = (TextView) itemView.findViewById(R.id.tv_first_date);

        second_date = (TextView) itemView.findViewById(R.id.second_date);

        tv_product_condition = (TextView) itemView.findViewById(R.id.tv_product_condition);


        view = itemView;
    }



}
