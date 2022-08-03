package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.RoomCallResponse
import retrofit2.Call
import retrofit2.http.GET

interface RoomCallClient {

    @GET("/call/callroom")
    fun getCallRoom(): Call<RoomCallResponse>
}