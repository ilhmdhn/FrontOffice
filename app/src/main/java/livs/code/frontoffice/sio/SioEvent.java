package livs.code.frontoffice.sio;

import java.net.URISyntaxException;

import livs.code.frontoffice.data.entity.Notification;
import livs.code.frontoffice.data.entity.User;

public interface SioEvent {
    void connect() throws URISyntaxException;

    void disconnect();

    void setEventListener(SioEventListener listener);

    void acceptedRoomCall(Notification not);

    void rejectedRoomCall(Notification not);

    //Flowable<ChatMessage> sendMessage(ChatMessage chatMessage);
}