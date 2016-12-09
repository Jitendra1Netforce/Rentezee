package com.rentezee.fragments.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rentezee.main.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 11/1/2016.
 */
public class ChatWithusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    private List<ChatWithusData> itemList;
    private Context context;
    ChatWithusHolder viewHolder;


    public ChatWithusAdapter(Context context, List<ChatWithusData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_chat, parent, false);
        viewHolder = new ChatWithusHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        ChatWithusHolder homeHolder = (ChatWithusHolder) holder;
        homeHolder.user_message.setText(itemList.get(position).message);
        homeHolder.txtAdminMessage.setText(itemList.get(position).message);

        Glide.with(context)
                .load(itemList.get(position).sender_photo)
                .centerCrop()
                //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(homeHolder.profile_image);


        Glide.with(context)
                .load(itemList.get(position).reciever_photo)
                .centerCrop()
                //.placeholder(R.mipmap.ic_loading)
                .crossFade()
                .into(homeHolder.profile_image2);


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
