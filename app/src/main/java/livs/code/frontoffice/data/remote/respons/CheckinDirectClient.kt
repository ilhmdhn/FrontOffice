package livs.code.frontoffice.data.remote.respons

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Query

interface CheckinDirectClient {

    @DELETE("/checkin-direct/remove_promo")
    fun removePromo(
        @Query("rcp") kodeRcp: String
    ): Call<Response>
}