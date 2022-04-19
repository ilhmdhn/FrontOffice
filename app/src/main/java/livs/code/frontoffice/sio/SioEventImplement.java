package livs.code.frontoffice.sio;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.data.entity.Notification;
import livs.code.frontoffice.data.entity.User;

public class SioEventImplement implements SioEvent {
    private static final String TAG = SioEventImplement.class.getSimpleName();


    private static final String EVENT_CONNECT = Socket.EVENT_CONNECT;
    private static final String EVENT_DISCONNECT = Socket.EVENT_DISCONNECT;
    private static final String EVENT_CONNECT_ERROR = Socket.EVENT_CONNECT_ERROR;
    private static final String EVENT_CONNECT_TIMEOUT = Socket.EVENT_CONNECT_TIMEOUT;
    private static final String EVENT_NEW_ORDER = "NEW_ORDER";
    private static final String EVENT_ROOM_CALL = "ROOM_CALL";
    private static final String EVENT_HIDE_ROOM_CALL = "HIDE_ROOM_CALL";
    private static final String ACCEPTED_ROOM_CALL = "ACCEPTED_ROOM_CALL";
    private static final String REJECTED_ROOM_CALL = "REJECTED_ROOM_CALL";
    private static final String ADD_USER = "ADD_USER";
    private static Socket mSocket;
    private static SioEvent INSTANCE;
    private static SioEventListener mEventListener;
    private static  String SOCKET_URL ;
    private static User USER_SIO;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // private for prevent instance object with constructor
    private SioEventImplement(String url, User user) {
        this.SOCKET_URL = url;
        this.USER_SIO = user;
    }

    public static SioEvent getInstance(String url,User user) {
        if (INSTANCE == null) {
            INSTANCE = new SioEventImplement(url+"/fo-rtc",user);
        }
        return INSTANCE;
    }

    @Override
    public void connect() throws URISyntaxException {

        mSocket = IO.socket(SOCKET_URL);

        mSocket.on(EVENT_CONNECT, onConnect);
        mSocket.on(EVENT_DISCONNECT, onDisconnect);
        mSocket.on(EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(EVENT_CONNECT_TIMEOUT, onConnectTimeout);
        mSocket.on(EVENT_NEW_ORDER, onNewOrder);
        mSocket.on(EVENT_ROOM_CALL, onRoomCall);
        mSocket.on(EVENT_HIDE_ROOM_CALL, onHideRoomCall);

        mSocket.connect();

    }

    @Override
    public void disconnect() {
        if (mSocket != null) mSocket.disconnect();
    }

    @Override
    public void setEventListener(SioEventListener listener) {
        mEventListener = listener;
    }

    @Override
    public void acceptedRoomCall(Notification not) {
        String jsonData = gson.toJson(not);
        mSocket.emit(ACCEPTED_ROOM_CALL, jsonData);
    }

    @Override
    public void rejectedRoomCall(Notification not) {
        String jsonData = gson.toJson(not);
        mSocket.emit(REJECTED_ROOM_CALL, jsonData);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "call: onConnect");
            String jsonData = gson.toJson(USER_SIO);
            mSocket.emit(ADD_USER, jsonData,mSocket.id());
            if (mEventListener != null) mEventListener.onConnect(args);
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "call: onDisconnect");
            if (mEventListener != null) mEventListener.onDisconnect(args);
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "call: onConnectError");
            if (mEventListener != null) mEventListener.onConnectError(args);
        }
    };

    private Emitter.Listener onConnectTimeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "call: onConnectTimeout");
            if (mEventListener != null) mEventListener.onConnectTimeout(args);
        }
    };

    private Emitter.Listener onNewOrder = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onNewOrder");
            Notification notification = gson.fromJson(args[0].toString(), Notification.class);
            if (mEventListener != null) mEventListener.onNewOrder(notification);
        }
    };

    private Emitter.Listener onRoomCall = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onRoomCall");
            Notification notification = gson.fromJson(args[0].toString(), Notification.class);
            if (mEventListener != null) mEventListener.onRoomCall(notification);
        }
    };

    private Emitter.Listener onHideRoomCall = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i(TAG, "call: onHideRoomCall");
            Notification notification = gson.fromJson(args[0].toString(), Notification.class);
            if (mEventListener != null) mEventListener.onHideRoomCall(notification);
        }
    };


}
