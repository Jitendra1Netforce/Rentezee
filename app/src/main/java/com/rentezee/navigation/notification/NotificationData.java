package com.rentezee.navigation.notification;

/**
 * Created by John on 11/11/2016.
 */
public class NotificationData
{

    public String type,title,image_url,message,order_id,transaction_id,date;

    NotificationData(String type, String title, String message,String order_id,String transaction_id,String date)
    {
        this.type = type;
        this.title = title;
        this.message = message;
        this.order_id = order_id;
        this.transaction_id = transaction_id;
        this.date = date;
    }

}
