package com.rentezee.fragments.wishlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/18/2016.
 */
public class WishListHolder extends RecyclerView.ViewHolder
{

    View view;
    TextView tvProductName,tvCategoriesName,tvRentPrice;
    ImageView imProductImage;
    LinearLayout linearLayout,layoutRemove,layoutMoveToCart;

    public WishListHolder(View itemView)
    {
        super(itemView);

        linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_wishlist);

        layoutRemove = (LinearLayout) itemView.findViewById(R.id.layoutRemove);

        layoutMoveToCart = (LinearLayout) itemView.findViewById(R.id.layoutMoveToCart);

        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);

        imProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);

        tvCategoriesName = (TextView) itemView.findViewById(R.id.tvProductCategoryName);

        tvRentPrice = (TextView) itemView.findViewById(R.id.tvPrice);

        view = itemView;
    }



}
