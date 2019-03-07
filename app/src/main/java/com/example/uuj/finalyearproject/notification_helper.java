package com.example.uuj.finalyearproject;

//android imports
import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class notification_helper {

    //used https://www.youtube.com/watch?v=B-G9283Ssd4&list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh video series in the creation of all notification related methods

    //method that creates and displays notification
    public static void displayNotification(Context context, String title, String body){
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, content_screen.CHANNEL_ID)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(Notification.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, nBuilder.build());
    }
}