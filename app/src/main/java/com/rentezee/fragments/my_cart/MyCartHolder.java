package com.rentezee.fragments.my_cart;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/10/2016.
 */
public class MyCartHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tvProductName,tvCategoriesName,tvRentPrice,tvSecurytiFee;
       ImageView imProductImage;
        RelativeLayout relativelayout;

        public MyCartHolder(View itemView)
        {
            super(itemView);

            relativelayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);

            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);


            tvCategoriesName = (TextView) itemView.findViewById(R.id.tv_categories_name);

            tvRentPrice = (TextView) itemView.findViewById(R.id.tv_rental);

            tvSecurytiFee = (TextView) itemView.findViewById(R.id.tv_security_fee);

            view = itemView;
        }



        }
