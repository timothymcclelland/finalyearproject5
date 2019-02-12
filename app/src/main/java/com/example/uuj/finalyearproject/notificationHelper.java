package com.example.uuj.finalyearproject;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class notificationHelper {

    //method that creates and displays notification based on content entered
    public static void displayNotification(Context context, String title, String body){
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, content.CHANNEL_ID)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(Notification.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, nBuilder.build());
    }
}