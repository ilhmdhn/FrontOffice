package livs.code.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import livs.code.frontoffice.data.entity.RoomPromo;


/**
 * Created by program on 24/10/2017.
 */

public class RoomPromoResponse extends BaseResponse {

    @SerializedName("data")
    private List<RoomPromo> roomPromos;

    public List<RoomPromo> getRoomPromos() {
        return roomPromos;
    }

    public void setRoomPromos(List<RoomPromo> roomPromos) {
        this.roomPromos = roomPromos;
    }
}
