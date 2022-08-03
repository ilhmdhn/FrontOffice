package com.ihp.frontoffice.notif

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.repository.IhpRepository

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "Room Call Chanel"
    }
    private val ihpRepository = IhpRepository()
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val url = (this@MyFirebaseMessagingService.applicationContext as MyApp).baseUrl
        val user = (this@MyFirebaseMessagingService.applicationContext as MyApp).userFo
        ihpRepository.insertToken(this@MyFirebaseMessagingService, url, token, user.userId, user.levelUser)
        Toast.makeText(this@MyFirebaseMessagingService, "TOKEN BARU "+token, Toast.LENGTH_SHORT).show()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val pendingIntent = NavDeepLinkBuilder(this@MyFirebaseMessagingService)
            .setGraph(R.navigation.nav_fragment)
            .setDestination(R.id.navNotificationFragment)
            .createPendingIntent()

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_happy_puppy_group))
            .setContentTitle(message.data.get("title"))
            .setContentText(message.data.get("content"))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)

            val notification = mBuilder.build()

            mNotificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
}