package livs.code.frontoffice.data.remote

import livs.code.frontoffice.data.remote.respons.UserListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportClient {

    @GET("report/user")
    fun getListUser(
        @Query("level_user") levelUser: String,
    ): Call<UserListResponse>
}