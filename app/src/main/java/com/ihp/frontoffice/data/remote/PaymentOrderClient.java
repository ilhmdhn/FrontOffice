package com.ihp.frontoffice.data.remote;

import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.remote.respons.BaseResponse;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentOrderClient {

    @POST("/payment/add")
    Call<RoomOrderResponse> submitPayment(@Body RoomOrder jsonRoomOrder);

    @GET("room/print-tagihan/{roomcode}")
    Call<BaseResponse> printBill(@Path("roomcode") String kodeTransaksi);

    @GET("order/pending-order/{room_code}")
    Call<BaseResponse> infoPendingOrder(@Path("room_code") String roomCode);

}
