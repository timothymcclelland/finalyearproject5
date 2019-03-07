package com.example.uuj.finalyearproject;

//firebase imports
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//used https://www.youtube.com/watch?v=B-G9283Ssd4&list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh in the creation of all notification related methods

//followed https://www.youtube.com/watch?v=4tssr7HPPeI&index=6&list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh in the creation of this java class

//client application service to receive notification
public class my_firebase_messaging_service extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //checks if notification is null or not
        if(remoteMessage.getNotification() !=null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            //notification_helper class and displayNotification method called from notification_helper.java
            notification_helper.displayNotification(getApplicationContext(), title, body);
        }
    }
}