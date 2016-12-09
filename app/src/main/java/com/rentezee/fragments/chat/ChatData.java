package com.rentezee.fragments.chat;

/**
 * Created by John on 12/8/2016.
 */

public class ChatData {

    public String title, price;
    String sender_id,sender_name,sender_photo,message,reciever_name,reciever_photo,date_time;
    int image;

    ChatData(String sender_id, String sender_name, String sender_photo,String message,String reciever_name,String reciever_photo,String date_time)
    {
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.sender_photo = sender_photo;
        this.message = message;
        this.reciever_name = reciever_name;
        this.reciever_photo = reciever_photo;
        this.date_time = date_time;
    }
}
