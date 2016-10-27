package com.rentezee.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rentezee.helpers.Debugger;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;

import java.util.Map;


/**
 * Created by JitendraSingh on 12,Oct 2016.
 * Email jitendra@live.in
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Debugger.i(TAG, remoteMessage.getData().toString());
        Map<String, String> map = remoteMessage.getData();
        Debugger.i(TAG, "Token " + FirebaseInstanceId.getInstance().getToken());
        Debugger.i(TAG, "From " + remoteMessage.getFrom() + ", " + remoteMessage.getNotification().getTitle());
        Debugger.i(TAG, "body " + remoteMessage.getNotification().getBody());
        Debugger.i(TAG, map.get("imageUrl"));

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String imageUrl = map.get("imageUrl");

        Debugger.i(TAG, "Token " + FirebaseInstanceId.getInstance().getToken());
        Debugger.i(TAG, "Title " + remoteMessage.getNotification().getTitle());
        Debugger.i(TAG, "body " + notification.getBody());
        Debugger.i(TAG, imageUrl);

        showNotification(notification.getTitle(), notification.getBody(), imageUrl);
    }

    private Bitmap getImage(String url) {
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
        if (imageUrl == null) {
            mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(body);
        } else {
            Bitmap bmp = getImage(imageUrl);
            mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bmp)
                                    .setSummaryText(body)
                            )
                            .setContentText(body);
        }
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, DashboardContainer.class);

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
        mNotificationManager.notify(1, mBuilder.build());
    }
}
