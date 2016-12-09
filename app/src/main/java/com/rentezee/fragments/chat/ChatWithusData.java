package com.rentezee.fragments.chat;

/**
 * Created by John on 11/1/2016.
 */
public class ChatWithusData
{
    public String title, price;
    String chat_id,sender_name,sender_photo,message,reciever_name,reciever_photo;
    int image;

    ChatWithusData(String chat_id, String sender_name, String sender_photo,String message,String reciever_name,String reciever_photo)
    {
        this.chat_id = chat_id;
        this.sender_name = sender_name;
        this.sender_photo = sender_photo;
        this.message = message;
        this.reciever_name = reciever_name;
        this.reciever_photo = reciever_photo;
    }
}
