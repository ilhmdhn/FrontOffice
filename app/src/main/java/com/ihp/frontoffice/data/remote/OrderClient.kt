package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.BaseResponse
import com.ihp.frontoffice.data.remote.respons.OrderBeforeTransferResponse
import com.ihp.frontoffice.data.remote.respons.OrderResponse
import com.ihp.frontoffice.data.remote.respons.Response
import retrofit2.Call
import retrofit2.http.*

interface OrderClient {

    @GET("room/{room_code}/order")
    fun getOrderRoom(
            @Path("room_code") roomCode: String
    ): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/cancelOrder")
    fun cancelOrder(
            @Field("order_slip_order") so: String,
            @Field("order_inventory") inventoryCode: String,
            @Field("order_qty") qty: String,
            @Field("order_room_rcp") rcp: String,
            @Field("order_room_user") user: String,
            @Field("order_model_android") android: String,
    ): Call<Response>

    @FormUrlEncoded
    @POST("order/revisi-order")
    fun revisiOrder(
            @Field("order_slip_order") so: String,
            @Field("order_inventory") inventoryCode: String,
            @Field("order_note") note: String,
            @Field("order_qty") qty: String,
            @Field("order_qty_temp") qtyTemp: String,
            @Field("order_room_rcp") rcp: String,
            @Field("order_room_user") user: String,
            @Field("order_model_android") android: String,
    ): Call<Response>

    @GET("order/old-room")
    fun getOldOrder(
            @Query("ivc")invoice: String
    ):Call<OrderBeforeTransferResponse>
}