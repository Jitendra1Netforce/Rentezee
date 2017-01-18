package com.rentezee.navigation.rent_it_out.upload_product.rentitData;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rentezee.main.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by John on 11/22/2016.
 */
public class RentItHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    CircleImageView imageViewClose;

    public RentItHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        imageView= (ImageView) itemView.findViewById(R.id.imageView);
        imageViewClose = (CircleImageView) itemView.findViewById(R.id.imageViewClose);
    }
}