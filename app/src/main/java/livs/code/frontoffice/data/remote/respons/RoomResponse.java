package livs.code.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import livs.code.frontoffice.data.entity.Room;


/**
 * Created by program on 24/10/2017.
 */

public class RoomResponse extends BaseResponse {

    @SerializedName("data")
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
