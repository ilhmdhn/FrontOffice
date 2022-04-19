package livs.code.frontoffice.sio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.net.URISyntaxException;

import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.repository.LocalRepository;
import livs.code.frontoffice.view.MainActivity;
import livs.code.frontoffice.view.SplashActivity;

public class SioBackgroundService extends Service {
    SioBackgroundThread sioBackgroundThread;
    private static String BASE_URL;
    private static User USER_SIO;
    private static final String TAG="BACKGROUND_SERVICE";
    private boolean startedService = false;
    private static LocalRepository localRepository;
    private static NotificationManager mNotificationManager;
    private static MediaPlayer mp;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        BASE_URL = ((MyApp) getApplicationContext()).getBaseUrl();
        USER_SIO =  ((MyApp) getApplicationContext()).getUserFo();
        mContext = getApplicationContext();
        localRepository = LocalRepository.getInstance(getApplicationContext());

        sioBackgroundThread = SioBackgroundThread.getInstance();
        Log.i(TAG, "onCreate");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            Log.i(TAG, "startMyOwnForeground()");
            startMyOwnForeground();
        } else {
            Log.i(TAG, "startForeground()");
            startForeground(1, new Notification());
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.i(TAG, "onStartCommand");
        if(sioBackgroundThread.startThread()){
            startedService = true;
            Log.i(TAG, "Thread Started");
        }

        return START_STICKY;
    }





    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sioBackgroundThread.endThread();
       /* if(!startedService){

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, SioBackgroundReceiver.class);
            this.sendBroadcast(broadcastIntent);

        }
*/
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved()");
        if(!startedService){
            sioBackgroundThread.endThread();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, SioBackgroundReceiver.class);
            this.sendBroadcast(broadcastIntent);
        }

        stopSelf();
    }

    public static class SioBackgroundThread extends Thread implements SioEventListener {

        private static SioBackgroundThread mSocketThread;
        SioEvent sioEvent;

        private SioBackgroundThread() {
        }

        // create single instance of socket thread class
        public static SioBackgroundThread getInstance() {
            if (mSocketThread == null) {
                mSocketThread = new SioBackgroundThread();
            }
            return mSocketThread;
        }

        public boolean startThread() {
            sioEvent = SioEventImplement.getInstance(BASE_URL,USER_SIO);
            sioEvent.setEventListener(this);
            try {
                sioEvent.connect();
                return true;
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public void run() {
            super.run();
            startThread();
        }

        public void endThread() {
            sioEvent.disconnect();
            currentThread().interrupt();
        }

        @Override
        public void onConnect(Object... args) {

        }

        @Override
        public void onDisconnect(Object... args) {

        }

        @Override
        public void onConnectError(Object... args) {

        }

        @Override
        public void onConnectTimeout(Object... args) {

        }

        @Override
        public void onNewOrder(livs.code.frontoffice.data.entity.Notification notification) {
            notification.setRead(false);
            localRepository.insertNotification(notification);
            String title = "";
            String subtitle = "";
            String roomInfo = notification.getRoomType() + " " + notification.getRoomCode();
            if (notification.getNotifType().equals("NEW_ORDER")) {
                title = "ORDER BARU";
                subtitle = "Mohon DO Order Room " + notification.getRoomCode();
            } else if (notification.getNotifType().equals("ROOM_CALL")) {
                title = "ROOM " + roomInfo + " MEMANGGIL";
                subtitle = "Mohon Terima Panggilan";
            }

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mp = MediaPlayer.create(mContext, alarmSound);
            mp.start();

            Intent intent = new Intent(mContext, SplashActivity.class);
            intent.putExtra("FromBackgroundService", notification.getNotifType());

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(mContext, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(title);
            bigText.setBigContentTitle(subtitle);
            bigText.setSummaryText(roomInfo);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext, notification.getNotifType());
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
                String channelId = notification.getNotifType();
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }
            mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
        }

        @Override
        public void onRoomCall(livs.code.frontoffice.data.entity.Notification notification) {
            notification.setRead(false);
            localRepository.insertNotification(notification);
            String title = "";
            String subtitle = "";
            String roomInfo = notification.getRoomType() + " " + notification.getRoomCode();
            if (notification.getNotifType().equals("NEW_ORDER")) {
                title = "ORDER BARU";
                subtitle = "Mohon DO Order Room " + notification.getRoomCode();
            } else if (notification.getNotifType().equals("ROOM_CALL")) {
                title = "ROOM " + roomInfo + " MEMANGGIL";
                subtitle = "Mohon Terima Panggilan";
            }

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mp = MediaPlayer.create(mContext, alarmSound);
            mp.start();

            Intent intent = new Intent(mContext, SplashActivity.class);
            intent.putExtra("FromBackgroundService", notification.getNotifType());

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(mContext, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(title);
            bigText.setBigContentTitle(subtitle);
            bigText.setSummaryText(roomInfo);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext, notification.getNotifType());
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
                String channelId = notification.getNotifType();
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }
            mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());


        }

        @Override
        public void onHideRoomCall(livs.code.frontoffice.data.entity.Notification notification) {

        }


    }


}