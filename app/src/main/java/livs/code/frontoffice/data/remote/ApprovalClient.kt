package livs.code.frontoffice.data.remote

import livs.code.frontoffice.data.remote.respons.Response
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApprovalClient {

    @FormUrlEncoded
    @POST("approval/submit-approval")
    fun submitApproval(
        @Field("user_id") userId: String,
        @Field("level_user") levelUser: String,
        @Field("room") room: String,
        @Field("keterangan") keterangan: String
    ): Call<Response>
}