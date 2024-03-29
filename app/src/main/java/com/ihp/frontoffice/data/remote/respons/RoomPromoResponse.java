package com.ihp.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.ihp.frontoffice.data.entity.RoomPromo;


/**
 * Created by program on 24/10/2017.
 */

public class RoomPromoResponse extends BaseResponse {

    @SerializedName("data")
    private List<RoomPromo> roomPromos;

    public List<RoomPromo> getRoomPromos() {
        return roomPromos;
    }

}
