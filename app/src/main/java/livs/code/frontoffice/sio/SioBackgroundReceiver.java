package livs.code.frontoffice.sio;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class SioBackgroundReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BX Broadcast Listened", "Service tried to stop");

        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("BACKGROUND_SERVICE", "startMyOwnForeground()");
            context.startForegroundService(new Intent(context, SioBackgroundService.class));
        } else {
            Log.i("BACKGROUND_SERVICE", "startForeground()");
            context.startService(new Intent(context, SioBackgroundService.class));
        }
    }
}