package com.ihp.frontoffice.data.remote;

import com.ihp.frontoffice.data.remote.respons.RoomTypeResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RoomTypeClient {

    @GET("room/jenis-kamar")
    Call<RoomTypeResponse> getRoomTypes();

    @GET("room/all-room-type-ready")
    Call<RoomTypeResponse> getAllRoomTypeReady();

    @GET("room/all-room-type-ready-grouping")
    Call<RoomTypeResponse> getAllGroupingRoomTypeReady();
}
