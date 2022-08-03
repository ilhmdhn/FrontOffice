package com.ihp.frontoffice.data.remote;

import com.ihp.frontoffice.data.remote.respons.FoodPromoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PromoInventoryClient {

    @GET("promo/promo-food/{jenis_kamar}/{kamar}")
    Call<FoodPromoResponse> getFoodPromoResponseCall(@Path("jenis_kamar") String roomType,
                                                     @Path("kamar") String roomCode);
}
