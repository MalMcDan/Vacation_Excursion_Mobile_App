package com.example.rmcdad308.UI;


import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.rmcdad308.R;

public class AlarmReceiver extends BroadcastReceiver {


    static int notificationId;


    @Override
    public void onReceive(Context context, Intent intent) {
        String channel_id = "journeys_channel"; // Use a single channel for all notifications

        createNotificationChannel(context, channel_id);

        String message = intent.getStringExtra("key");

        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentText(message)
                .setContentTitle("Journeys")
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, notification);
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        CharSequence name = "Journeys";
        String description = "Journey notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}






