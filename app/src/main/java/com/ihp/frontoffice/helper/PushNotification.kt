package com.ihp.frontoffice.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.ihp.frontoffice.R
import com.ihp.frontoffice.view.fragment.operasional.firebase.UdpService

class PushNotification {

    companion object{
        private const val NOTIF_ORDER_FAIL_ID = 3
        private const val CHANNEL_NOTIF_ORDER_FAIL_ID = "CHANNEL_TIGA"
        private const val CHANNEL_NOTIF_ORDER_FAIL_NAME = "CHANNEL_ORDER_FAIL"
    }

    fun pushNotifOrderFailed(title: String, message: String, context: Context){
        val mNotificationManager = NotificationManagerCompat.from(context)
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_NOTIF_ORDER_FAIL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setGroupSummary(true)
            .setAutoCancel(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_NOTIF_ORDER_FAIL_ID, CHANNEL_NOTIF_ORDER_FAIL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_NOTIF_ORDER_FAIL_NAME
            mBuilder.setChannelId(CHANNEL_NOTIF_ORDER_FAIL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        mNotificationManager.notify(NOTIF_ORDER_FAIL_ID, notification as Notification)
    }
}