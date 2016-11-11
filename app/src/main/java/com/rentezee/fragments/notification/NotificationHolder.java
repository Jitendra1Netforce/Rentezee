package com.rentezee.fragments.notification;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/11/2016.
 */
public class NotificationHolder extends RecyclerView.ViewHolder {

    View view;
    TextView tvProductName,tv_product_descricption,tv_date,tv_time;
    ImageView imProductImage;
    RelativeLayout relativelayout;

    public NotificationHolder(View itemView)
    {
        super(itemView);

        relativelayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_container);

        imProductImage = (ImageView) itemView.findViewById(R.id.imv_product);

        tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);

        tv_product_descricption = (TextView) itemView.findViewById(R.id.tv_product_descricption);

        tv_date = (TextView) itemView.findViewById(R.id.tv_date);

        tv_time = (TextView) itemView.findViewById(R.id.tv_time);

        view = itemView;
    }



}
