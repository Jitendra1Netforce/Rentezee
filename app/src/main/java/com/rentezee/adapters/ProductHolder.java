package com.rentezee.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 12/6/2016.
 */

public class ProductHolder extends RecyclerView.ViewHolder
{

    View view;
    TextView tvProductName,tvProductCategoryName,tvPrice;
    LinearLayout linearlayout_productView;
    ImageView ivProductImage;

    public ProductHolder(View itemView)
    {
        super(itemView);

        linearlayout_productView = (LinearLayout) itemView.findViewById(R.id.linearlayout_productView);

        ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);

        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);

        tvProductCategoryName = (TextView) itemView.findViewById(R.id.tvProductCategoryName);

        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

        view = itemView;
    }


}

