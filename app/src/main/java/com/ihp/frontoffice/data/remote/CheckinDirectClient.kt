package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.Response
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Query

interface CheckinDirectClient {

    @DELETE("/checkin-direct/remove_promo")
    fun removePromo(
        @Query("rcp") kodeRcp: String
    ): Call<Response>
}