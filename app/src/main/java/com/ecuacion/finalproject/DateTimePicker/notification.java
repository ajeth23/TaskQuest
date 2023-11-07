package com.ecuacion.finalproject.DateTimePicker;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.ecuacion.finalproject.R;

public class notification extends BroadcastReceiver {
    private static final String channelID = "channel1";
    private static final int notificationID = 1;
    private static final String titleExtra = "titleExtra";
    private static final String messageExtra = "messageExtra";

    Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(titleExtra);
        String message = intent.getStringExtra(messageExtra);

//        createNotificationChannel();

        android.app.Notification notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID, notification);
    }

//    private void createNotificationChannel() {
//        String name = "Notif Channel";
//        String desc = "A Description of the Channel";
//        int importance = NotificationManager.IMPORTANCE_DEFAULT;
//        NotificationChannel channel = new NotificationChannel(channelID, name, importance);
//        channel.setDescription(desc);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.createNotificationChannel(channel);
//    }
}