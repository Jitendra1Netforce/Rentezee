package com.rentezee.fragments.rent_it_out.productview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/21/2016.
 */
public class ProductViewHolder extends RecyclerView.ViewHolder
{

    View view;
    TextView tvProductName,tvProductCategoryName,tvDescription,tvSecurityMoney,tvPerdayRent,tvPriceData,tvPerdayHeading,tvSecurityheading;
    LinearLayout linearLayout;
    ImageView ivProductImage;


    public ProductViewHolder(View itemView)
    {
        super(itemView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_productView);

        ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);

        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);

        tvProductCategoryName = (TextView) itemView.findViewById(R.id.tvProductCategoryName);

        tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);

        tvSecurityMoney = (TextView) itemView.findViewById(R.id.tvSecurityMoney);

        tvPerdayRent = (TextView) itemView.findViewById(R.id.tvPerdayRent);

        tvPriceData = (TextView) itemView.findViewById(R.id.tvPice);

        tvPerdayHeading = (TextView) itemView.findViewById(R.id.txtPerdayHeading);

        tvSecurityheading = (TextView) itemView.findViewById(R.id.txtSecurityheading);


        view = itemView;
    }



}
