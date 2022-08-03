package com.ihp.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.ihp.frontoffice.data.entity.RoomType;

public class RoomTypeResponse extends BaseResponse {
    @SerializedName("data")
    private List<RoomType> roomTypes;

    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }
}
