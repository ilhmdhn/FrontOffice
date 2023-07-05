package com.ihp.frontoffice.view.fragment.operasional.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ihp.frontoffice.R
import com.ihp.frontoffice.events.DataBusEvent
import com.ihp.frontoffice.events.GlobalBus
import java.net.DatagramPacket
import java.net.DatagramSocket


class UdpService: Service(){

    companion object {
        private const val NOTIFICATION_ID = 2
        private const val CHANNEL_ID = "channel_02"
        private const val CHANNEL_NAME = "channel_so_do"
     }

    private lateinit var thread: Thread

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        startThread()
    }

    override fun onDestroy() {
        super.onDestroy()

        thread.interrupt()
    }

    private fun startThread(){
        thread = Thread(Runnable {
            val socket = DatagramSocket(7072)
            val buffer = ByteArray(1024)

            while (!Thread.currentThread().isInterrupted) {
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                val message = String(packet.data, 0, packet.length)
                createNotif(message)
            }

            socket.close()
        })
        thread.start()
    }

    private fun createNotif(message: String){
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_happy_puppy_group))
                .setContentTitle(message)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
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
        mNotificationManager.notify(NOTIFICATION_ID, notification as Notification)

        GlobalBus.getBus().post(DataBusEvent.soDo(message))
    }
}