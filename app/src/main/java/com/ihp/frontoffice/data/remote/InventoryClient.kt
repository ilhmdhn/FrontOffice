package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.InventoryPagingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InventoryClient {

    @GET("inventory/list-paging")
    suspend fun getInventoryPaging(
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Query("category") category: String,
            @Query("search") search: String
    ): InventoryPagingResponse
}