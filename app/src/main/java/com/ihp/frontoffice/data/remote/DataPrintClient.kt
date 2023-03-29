package com.ihp.frontoffice.data.remote


import com.ihp.frontoffice.data.remote.respons.Response
import com.ihp.frontoffice.data.remote.respons.xBillResponse
import com.ihp.frontoffice.data.remote.respons.xInvoiceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DataPrintClient {

    @GET("/mobile-print/bill")
    fun getPrintBill(
        @Query("room") room: String
    ): Call<xBillResponse>

    @GET("/mobile-print/view-bill")
    fun getViewBill(
            @Query("room") room: String
    ): Call<xBillResponse>

    @GET("/mobile-print/invoice")
    fun getPrintInvoice(
        @Query("rcp") rcp: String
    ):Call<xInvoiceResponse>

    @GET("mobile-print/update-status")
    fun updateStatusPrint(
        @Query("rcp") rcp: String,
        @Query("status_print") printStatus: String
    ): Call<Response>
}