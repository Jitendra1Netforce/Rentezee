package com.rentezee.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rentezee.navigation.chat.ChatActivity;
import com.rentezee.navigation.notification.NotificationActivity;
import com.rentezee.helpers.Debugger;
import com.rentezee.main.R;

import java.util.Map;


/**
 * Created by JitendraSingh on 12,Oct 2016.
 * Email jitendra@live.in
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String type,title,message,transaction_id,order_id,message_body;
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String txnId;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

         txnId = "1" + System.currentTimeMillis();

        String json =remoteMessage.getData().toString();

        Debugger.i(TAG, remoteMessage.getData().toString());

        Map<String, String> map = remoteMessage.getData();

        type = map.get("type").toString();

        title = map.get("title").toString();

        message = map.get("message").toString();


        System.out.println("type========"+type);

       // message_body  = message+"\n" +"Your Transaction id = "+ transaction_id +"\n+"+"Order Id="+ order_id.toString();

       /* Debugger.i(TAG, "Token " + FirebaseInstanceId.getInstance().getToken());

        Debugger.i(TAG, "From " + remoteMessage.getFrom() + ", " + remoteMessage.getNotification().getTitle());
        Debugger.i(TAG, "body " + remoteMessage.getNotification().getBody());
        Debugger.i(TAG, map.get("imageUrl"));

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String imageUrl = map.get("imageUrl");

        Debugger.i(TAG, "Token " + FirebaseInstanceId.getInstance().getToken());
        Debugger.i(TAG, "Title " + remoteMessage.getNotification().getTitle());
        Debugger.i(TAG, "body " + notification.getBody());
        Debugger.i(TAG, imageUrl);*/

        if(type.equals("message"))
        {

            showNotification_chat(title, message, "");
        }
        else
        {

            showNotification(title, message, "");
        }


    }

    private void showNotification_chat(String title, String message, String s)
    {

        if (title == null) {
            title = getApplicationContext().getString(R.string.app_name);
        }
        if (message == null) {
            message = "";
        }

        NotificationCompat.Builder mBuilder=null;

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.ic_launcher);

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);



       /* else {
            Bitmap bmp = getImage(imageUrl);
            mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bmp)
                                    .setSummaryText(body)
                            )
                            .setContentText(body);
        }*/
        // Creates an explicit intent for an Activity in your app



        Intent resultIntent = new Intent(this, ChatActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        // stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        Long f= Long.parseLong(txnId);

        Integer intValue = f.intValue();

        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

        System.out.println("intValue-------------"+intValue);

        mNotificationManager.notify(intValue,mBuilder.build());




    }

    private Bitmap getImage(String url)
    {
        Bitmap bitmap = null;
        try {
            bitmap = Glide
                    .with(this)
                    .load(url)
                    .asBitmap()
                    .into(200, 150) // Width and height
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void showNotification(String title, String body, String imageUrl) {
        if (title == null) {
            title = getApplicationContext().getString(R.string.app_name);
        }
        if (body == null) {
            body = "";
        }

        NotificationCompat.Builder mBuilder=null;

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.ic_launcher);

            mBuilder = new NotificationCompat.Builder(this)
                          .setSmallIcon(R.mipmap.ic_launcher)
                           .setLargeIcon(icon)
                            .setContentTitle(title)
                            .setContentText(body)
                           .setAutoCancel(true);

       /* else {
            Bitmap bmp = getImage(imageUrl);
            mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bmp)
                                    .setSummaryText(body)
                            )
                            .setContentText(body);
        }*/
        // Creates an explicit intent for an Activity in your app


        Intent resultIntent = new Intent(this, NotificationActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        // stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        Long f= Long.parseLong(txnId);

        Integer intValue = f.intValue();

        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

        System.out.println("intValue-------------"+intValue);

        mNotificationManager.notify(intValue,mBuilder.build());


    }
}
