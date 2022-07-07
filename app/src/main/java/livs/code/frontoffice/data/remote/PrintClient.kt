package livs.code.frontoffice.data.remote

import livs.code.frontoffice.data.remote.respons.Response
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PrintClient {

    @FormUrlEncoded
    @POST("printer/print-tagihan")
    fun printTagihan(
        @Field("rcp") rcp: String,
        @Field("chusr") user: String
    ): Call<Response>
}