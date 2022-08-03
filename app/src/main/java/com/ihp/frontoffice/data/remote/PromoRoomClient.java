package com.ihp.frontoffice.data.remote;

import com.ihp.frontoffice.data.remote.respons.RoomPromoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PromoRoomClient {

    @GET("promo/promo-room/{jenis_kamar}")
    Call<RoomPromoResponse> getRoomPromoResponseCall(@Path("jenis_kamar") String roomType);
}
