package com.rentezee.fragments.rent_it_out.upload_product.rentitData;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rentezee.main.R;

/**
 * Created by John on 11/22/2016.
 */
public class RentItHolder extends RecyclerView.ViewHolder {
    ImageView imageView, imageViewClose;

    public RentItHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        imageView= (ImageView) itemView.findViewById(R.id.imageView);
        imageViewClose = (ImageView) itemView.findViewById(R.id.imageViewClose);
    }
}