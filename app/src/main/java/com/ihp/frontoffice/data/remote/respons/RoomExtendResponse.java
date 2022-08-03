package com.ihp.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.ihp.frontoffice.data.entity.Room;

public class RoomExtendResponse extends BaseResponse{
    @SerializedName("data")
    private List<Room> roomOrderExtends;

    public List<Room> getRoomOrderExtends() {
        return roomOrderExtends;
    }
}
