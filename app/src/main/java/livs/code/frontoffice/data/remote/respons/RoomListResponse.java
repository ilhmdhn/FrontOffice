package livs.code.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import livs.code.frontoffice.data.entity.Room;


/**
 * Created by program on 24/10/2017.
 */

public class RoomListResponse extends BaseResponse {

    @SerializedName("data")
    private List<Room> rooms;

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

}
