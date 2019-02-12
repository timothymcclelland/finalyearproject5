package com.example.uuj.finalyearproject;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//client application service to receive notification
//followed https://www.youtube.com/watch?v=4tssr7HPPeI&index=6&list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //checks if notification is null or not
        if(remoteMessage.getNotification() !=null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            //notificationHelper class and displayNotification method called from notificationHelper.java
            notificationHelper.displayNotification(getApplicationContext(), title, body);
        }
    }
}
