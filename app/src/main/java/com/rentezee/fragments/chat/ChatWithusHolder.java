package com.rentezee.fragments.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rentezee.main.R;

/**
 * Created by John on 11/1/2016.
 */
public class ChatWithusHolder extends RecyclerView.ViewHolder {

    View view;
    TextView user_message,txtAdminMessage;
    ImageView profile_image,profile_image2;
    LinearLayout chat_layout;

    public ChatWithusHolder(View itemView) {
        super(itemView);

        chat_layout = (LinearLayout) itemView.findViewById(R.id.chat_layout);

        user_message = (TextView) itemView.findViewById(R.id.user_message);

        txtAdminMessage = (TextView) itemView.findViewById(R.id.txtAdminMessage);

        profile_image = (ImageView) itemView.findViewById(R.id.profile_image);

        profile_image2 = (ImageView) itemView.findViewById(R.id.profile_image2);



        view = itemView;
    }
}
