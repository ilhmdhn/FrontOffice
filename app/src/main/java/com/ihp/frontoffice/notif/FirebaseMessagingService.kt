package com.ihp.frontoffice.notif

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.data.repository.IhpRepository

class MyFirebaseMessagingService : FirebaseMessagingService() {
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
        Log.d("isi notif",message.data.toString())
        Log.d("isi notif",message.notification?.body.toString())
    }
}