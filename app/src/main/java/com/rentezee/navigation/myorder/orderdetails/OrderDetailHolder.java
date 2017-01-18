package com.rentezee.navigation.myorder.orderdetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 12/12/2016.
 */

public class OrderDetailHolder extends RecyclerView.ViewHolder
{


    View view;
    public TextView textview_from_date,textview_to_date,tv_rent_duration;
    TextView tvProductName,tvCategoriesName,tvRentPrice,tvSecurityfeeHeading,tvItemTotal,tvProductType,tvSellername;
    TextView tvRentalHeading,tvrentDurationHeading,tv_to,tvItemTotalHeading,tvSecurity,tvRentalSecurityHeading;
    ImageView imProductImage;
    RelativeLayout relativelayout,relativeProductdetails,relativeDescription;


    public OrderDetailHolder(View itemView)
    {
        super(itemView);

        relativelayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);

        relativeDescription = (RelativeLayout) itemView.findViewById(R.id.relativeDescription);

        relativeProductdetails = (RelativeLayout) itemView.findViewById(R.id.relativeProductdetails);

        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);

        tvProductType = (TextView) itemView.findViewById(R.id.tvProductType);

        tvSellername = (TextView) itemView.findViewById(R.id.tvSellername);

        tvCategoriesName = (TextView) itemView.findViewById(R.id.tvCategoriesName);

        tvRentalHeading = (TextView) itemView.findViewById(R.id.tvRentHeading);

        tvSecurityfeeHeading = (TextView) itemView.findViewById(R.id.tvSecurityfeeHeading);

        tvrentDurationHeading = (TextView) itemView.findViewById(R.id.tvRentDurationHeading);

        textview_from_date = (TextView) itemView.findViewById(R.id.tvFromDate);

        textview_to_date = (TextView) itemView.findViewById(R.id.tvToDate);

        tv_rent_duration = (TextView) itemView.findViewById(R.id.tvRentDuration);

        imProductImage = (ImageView) itemView.findViewById(R.id.product_image);

        tvRentPrice = (TextView) itemView.findViewById(R.id.tvRent);

        tv_to = (TextView) itemView.findViewById(R.id.tvto);

        tvItemTotalHeading = (TextView) itemView.findViewById(R.id.tvItemTotalHeading);

        tvSecurity = (TextView) itemView.findViewById(R.id.tvSecurity);

        tvItemTotal = (TextView) itemView.findViewById(R.id.tvItemTotal);

        tvRentalSecurityHeading = (TextView) itemView.findViewById(R.id.tvRentalSecurityHeading);

        view = itemView;
    }


}
