package livs.code.frontoffice.sio;

import livs.code.frontoffice.data.entity.Notification;

public interface SioEventListener {
    void onConnect(Object... args);

    void onDisconnect(Object... args);

    void onConnectError(Object... args);

    void onConnectTimeout(Object... args);

    void onNewOrder(Notification notification);

    void onRoomCall(Notification notification);

    void onHideRoomCall(Notification notification);
}
