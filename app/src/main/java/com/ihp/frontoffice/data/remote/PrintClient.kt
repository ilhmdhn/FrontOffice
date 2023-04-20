package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.PrintStatusResponse
import com.ihp.frontoffice.data.remote.respons.Response
import retrofit2.Call
import retrofit2.http.*

interface PrintClient {
    @FormUrlEncoded
    @POST("printer/print-tagihan")
    fun printTagihan(
        @Field("rcp") rcp: String,
        @Field("chusr") user: String
    ): Call<Response>

    @FormUrlEncoded
    @POST("printer/print-invoice")
    fun printInvoice(
        @Field("rcp") rcp: String
    ): Call<Response>

    @GET("printer/print-status")
    fun printStatus(
        @Query("rcp")  rcp: String
    ): Call<PrintStatusResponse>


}