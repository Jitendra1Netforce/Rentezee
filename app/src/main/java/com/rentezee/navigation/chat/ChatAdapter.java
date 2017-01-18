package com.rentezee.navigation.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rentezee.main.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 12/8/2016.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    private List<ChatData> itemList;
    private Context context;
    ChatHolder viewHolder;


    public ChatAdapter(Context context, List<ChatData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_chat2, parent, false);
        viewHolder = new ChatHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        ChatHolder homeHolder = (ChatHolder) holder;

        //  System.out.println("sender id ===="+itemList.get(position).sender_id.toString());

        if(itemList.get(position).sender_id.toString().equals(ChatActivity.user_id.toString())){

            homeHolder.UserLayout.setVisibility(View.VISIBLE);
            homeHolder.AdminLayout.setVisibility(View.GONE);

            homeHolder.txtAdminMessage.setText(itemList.get(position).message);

            String dtStart = itemList.get(position).date_time;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = format.parse(dtStart);
                System.out.println(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            homeHolder.day_txt2.setText(dtStart.substring(0,10));

            homeHolder.time_txt2.setText(dtStart.substring(11,16));


            Glide.with(context)
                    .load(itemList.get(position).sender_photo)
                    .centerCrop()
                    //.placeholder(R.mipmap.ic_loading)
                    .crossFade()
                    .into(homeHolder.profile_image2);
        }
        else
        {

            homeHolder.UserLayout.setVisibility(View.GONE);
            homeHolder.AdminLayout.setVisibility(View.VISIBLE);

            homeHolder.user_message.setText(itemList.get(position).message);

            String dtStart = itemList.get(position).date_time;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = format.parse(dtStart);
                System.out.println(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            homeHolder.day_txt.setText(dtStart.substring(0,10));

            homeHolder.time_txt.setText(dtStart.substring(11,16));


            Glide.with(context)
                    .load(itemList.get(position).sender_photo)
                    .centerCrop()
                    //.placeholder(R.mipmap.ic_loading)
                    .crossFade()
                    .into(homeHolder.profile_image);

        }


    }

    private void showMessage(String s)
    {

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount()
    {
        return itemList.size();
        // return itemList.size();
    }

    public String getCurrentTimeStamp()
    {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }
}