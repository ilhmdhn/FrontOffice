package com.ihp.frontoffice.data.remote

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.ihp.frontoffice.data.remote.respons.Response
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Query

interface CheckinDirectClient {

    @DELETE("/checkin-direct/remove_promo")
    fun removePromo(
        @Query("rcp") kodeRcp: String
    ): Call<Response>

    @DELETE("/checkin-direct/remove-promo-fnb/{rcp}")
    fun removePromoFood(
        @Path("rcp") reception: String
    ): Call<Response>
}