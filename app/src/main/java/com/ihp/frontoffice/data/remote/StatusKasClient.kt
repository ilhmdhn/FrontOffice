package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.StatusKasResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StatusKasClient {

    @GET("/report/getStatusReportKas")
    fun getStatusKasReport(
        @Query("tanggal") tanggal: String,
        @Query("shift") shift: String,
        @Query("chusr") chusr: String
    ): Call<StatusKasResponse>
}