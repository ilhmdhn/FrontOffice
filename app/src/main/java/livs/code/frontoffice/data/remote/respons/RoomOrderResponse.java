package livs.code.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import livs.code.frontoffice.data.entity.RoomOrder;

public class RoomOrderResponse extends BaseResponse{

    @SerializedName("data")
    private RoomOrder roomOrder;

    public RoomOrder getRoomOrder() {
        return roomOrder;
    }

    public void setRoomOrder(RoomOrder roomOrder) {
        this.roomOrder = roomOrder;
    }
}
