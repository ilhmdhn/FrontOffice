package livs.code.frontoffice.notif

import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Toast.makeText(this@MyFirebaseMessagingService, "TOKEN BARU "+token, Toast.LENGTH_SHORT).show()
    }
}