package com.ihp.frontoffice.view.component;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Notification;
import com.ihp.frontoffice.view.MainActivity;

public class PushNotify {
    private final NotificationManager mNotificationManager;
    private MediaPlayer mp;

    public PushNotify(Application mContext, Notification ntf, int typeRingtone) {
        Uri alarmSound = RingtoneManager.getDefaultUri(typeRingtone);
        mp = MediaPlayer.create(mContext, alarmSound);
        mp.start();

        String title = "";
        String subtitle = "";
        String roomInfo = ntf.getRoomType() + " " + ntf.getRoomCode();
        if (ntf.getNotifType().equals("NEW_ORDER")) {
            title = "ORDER BARU";
            subtitle = "Mohon DO Order Room " + ntf.getRoomCode();
        } else if (ntf.getNotifType().equals("ROOM_CALL")) {
            title = "ROOM " + roomInfo + " MEMANGGIL";
            subtitle = "Mohon Terima Panggilan";
        }


        Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
        intent.putExtra("FromPushNotify", ntf.getNotifType());

        PendingIntent pendingIntent =
                PendingIntent.getActivity(mContext, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(title);
        bigText.setBigContentTitle(subtitle);
        bigText.setSummaryText(roomInfo);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext.getApplicationContext(), ntf.getNotifType());
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(roomInfo);
        mBuilder.setContentText(subtitle);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);


        mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = ntf.getNotifType();
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());


    }

    public void stopPlayer() {
        mp.stop();
    }

}
