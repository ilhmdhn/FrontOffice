package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.Response
import retrofit2.Call
import retrofit2.http.*

interface FirebaseClient {
    @FormUrlEncoded
    @POST("/firebase/token")
    fun insertToken(
        @Field("token") token: String,
        @Field("user") user: String,
        @Field("user_level") userLevel: String
    ): Call<Response>

    @PUT("/firebase/token/{token}")
    fun turnOffToken(
        @Path("token") token: String,
        ): Call<Response>
}