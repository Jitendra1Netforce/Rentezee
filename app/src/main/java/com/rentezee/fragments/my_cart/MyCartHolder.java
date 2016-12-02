package com.rentezee.fragments.my_cart;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/10/2016.
 */
public class MyCartHolder extends RecyclerView.ViewHolder
{


        View view;
        public  TextView textview_from_date,textview_to_date,tv_rent_duration;
        TextView tvProductName,tvCategoriesName,tvRentPrice,tvSecurytiFee,tvtotal;
        TextView tvRentalHeading,tvsecurityheading,tvrentDurationHeading;
        ImageView imProductImage;
        RelativeLayout relativelayout;
        LinearLayout layoutRemove,layoutMoveToCart;


        public MyCartHolder(View itemView)
        {
            super(itemView);

            relativelayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);

            tvRentalHeading = (TextView) itemView.findViewById(R.id.tv_rental_heading);

            tvsecurityheading = (TextView) itemView.findViewById(R.id.tv_security_fee_heading);

            tvrentDurationHeading = (TextView) itemView.findViewById(R.id.tv_rent_duration_heading);

            textview_from_date = (TextView) itemView.findViewById(R.id.textview_from_date);

            textview_to_date = (TextView) itemView.findViewById(R.id.textview_to_date);

            tv_rent_duration = (TextView) itemView.findViewById(R.id.tv_rent_duration);

            layoutRemove = (LinearLayout) itemView.findViewById(R.id.layoutRemove);

            layoutMoveToCart = (LinearLayout) itemView.findViewById(R.id.layoutMoveToCart);

            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);

            imProductImage = (ImageView) itemView.findViewById(R.id.product_image);

            tvCategoriesName = (TextView) itemView.findViewById(R.id.tv_categories_name);

            tvRentPrice = (TextView) itemView.findViewById(R.id.tv_rental);

            tvSecurytiFee = (TextView) itemView.findViewById(R.id.tv_security_fee);

            tvtotal = (TextView) itemView.findViewById(R.id.tvTotaldata);

            view = itemView;
        }


        }
