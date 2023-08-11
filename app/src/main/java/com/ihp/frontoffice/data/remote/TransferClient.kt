package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.TransferRoomResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TransferClient {
    @FormUrlEncoded
    @POST("/transfer/tolobby")
    fun transferLobbyToLobby(
        @Field("room_code") roomCode: String,
        @Field("room_destination") roomDestination: String,
        @Field("chusr") user: String
    ): Call<TransferRoomResponse>
}