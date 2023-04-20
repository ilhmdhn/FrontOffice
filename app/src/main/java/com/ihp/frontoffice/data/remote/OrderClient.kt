package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.OrderResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderClient {

    @GET("room/{room_code}/order")
    fun getOrderRoom(
            @Path("room_code") roomCode: String
    ): Call<OrderResponse>
}