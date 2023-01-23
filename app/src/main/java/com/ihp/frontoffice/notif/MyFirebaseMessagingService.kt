package com.ihp.frontoffice.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.data.local.FrontOfficeDatabase
import com.ihp.frontoffice.data.repository.IhpRepository
import com.ihp.frontoffice.view.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "Room Call Chanel"
    }

    private val ihpRepository = IhpRepository()
    var userFo: User? = null
    private lateinit var frontOfficeDatabase: FrontOfficeDatabase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val url = (this@MyFirebaseMessagingService.applicationContext as MyApp).baseUrl
        val user = (this@MyFirebaseMessagingService.applicationContext as MyApp).userFo

        if (!url.isNullOrEmpty()){
            ihpRepository.insertToken(this@MyFirebaseMessagingService, url, token, user.userId, user.levelUser)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        frontOfficeDatabase = FrontOfficeDatabase.getInstance(this@MyFirebaseMessagingService)
        userFo = frontOfficeDatabase.userDao().userLogin

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("FromPushNotify", "ROOM_CALL")
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL"
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_happy_puppy_group))
            .setContentTitle(message.data.get("title"))
            .setContentText(message.data.get("content"))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setGroup(GROUP_KEY_WORK_EMAIL)
            .setGroupSummary(true)
            .setAutoCancel(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_NAME
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()


        if (userFo != null){
            if(userFo!!.isLogin){
            mNotificationManager.notify(NOTIFICATION_ID, notification)
            }
        }else{
            Log.d("islogin", "Tidak ada user yang login")
        }
    }
}