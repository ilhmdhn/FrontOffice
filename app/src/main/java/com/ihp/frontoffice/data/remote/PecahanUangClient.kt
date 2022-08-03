package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.PecahanUangResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PecahanUangClient {

    @GET("report/getCashDetail")
    fun getCashDetail(
        @Query("tanggal") tanggal: String,
        @Query("shift") shift: String
    ): Call<PecahanUangResponse>
}