package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.Response
import com.ihp.frontoffice.data.remote.respons.RoomCallResponse
import retrofit2.Call
import retrofit2.http.*

interface RoomCallClient {

    @GET("/call/callroom")
    fun getCallRoom(): Call<RoomCallResponse>

    @FormUrlEncoded
    @PUT("call/callroom/{room}")
    fun responseCallRoom(
        @Path("room")room: String,
        @Field("state")state: Byte,
        @Field("chusr")user: String
    ): Call<Response>
}