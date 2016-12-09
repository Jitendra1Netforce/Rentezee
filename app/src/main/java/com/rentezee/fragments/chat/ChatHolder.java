package com.rentezee.fragments.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 12/8/2016.
 */

public class ChatHolder extends RecyclerView.ViewHolder {

    View view;
    TextView user_message,txtAdminMessage,time_txt,time_txt2,day_txt,day_txt2;
    ImageView profile_image,profile_image2;

    RelativeLayout AdminLayout,UserLayout;

    public ChatHolder(View itemView)
    {
        super(itemView);

        AdminLayout = (RelativeLayout) itemView.findViewById(R.id.AdminLayout) ;


        UserLayout = (RelativeLayout) itemView.findViewById(R.id.UserLayout);

        user_message = (TextView) itemView.findViewById(R.id.user_message);

        txtAdminMessage = (TextView) itemView.findViewById(R.id.user_message2);

        day_txt= (TextView) itemView.findViewById(R.id.day_txt);

        day_txt2= (TextView) itemView.findViewById(R.id.day_txt2);

        time_txt = (TextView) itemView.findViewById(R.id.time_txt);

        time_txt2 = (TextView) itemView.findViewById(R.id.time_txt2);

        profile_image = (ImageView) itemView.findViewById(R.id.profile_image);

        profile_image2 = (ImageView) itemView.findViewById(R.id.profile_image2);



        view = itemView;
    }
}