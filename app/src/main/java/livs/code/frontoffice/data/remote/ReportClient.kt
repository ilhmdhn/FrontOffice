package livs.code.frontoffice.data.remote

import livs.code.frontoffice.data.remote.respons.MySalesResponse
import livs.code.frontoffice.data.remote.respons.Response
import livs.code.frontoffice.data.remote.respons.SalesItemListResponse
import livs.code.frontoffice.data.remote.respons.UserListResponse
import retrofit2.Call
import retrofit2.http.*

interface ReportClient {

    @GET("report/user")
    fun getListUser(
        @Query("level_user") levelUser: String,
    ): Call<UserListResponse>

    @FormUrlEncoded
    @POST("report/cashdetail")
    fun postCashDetail(
        @Field("tanggal") tanggal: String,
        @Field("shift") shift: String,
        @Field("seratus_ribu") seratusRibu: Int,
        @Field("lima_puluh_ribu") limaPuluhRIbu: Int,
        @Field("dua_puluh_ribu") duaPuluhRibu: Int,
        @Field("sepuluh_ribu") sepuluhRibu: Int,
        @Field("lima_ribu") limaRibu: Int,
        @Field("dua_ribu") duaRibu:  Int,
        @Field("seribu") seribu: Int,
        @Field("lima_ratus") limaRatus: Int,
        @Field("dua_ratus") duaRatus: Int,
        @Field("seratus") seratus: Int,
        @Field("lima_puluh") limaPuluh: Int,
        @Field("dua_lima") duaLima: Int
    ): Call<Response>

    @PUT("report/cashdetail/{tanggal}/{shift}")
    fun updateCashDetail(
        @Path("tanggal") tanggal: String,
        @Path("shift") shift: String,
        @Query("seratus_ribu") seratusRibu: Int,
        @Query("lima_puluh_ribu") limaPuluhRIbu: Int,
        @Query("dua_puluh_ribu") duaPuluhRibu: Int,
        @Query("sepuluh_ribu") sepuluhRibu: Int,
        @Query("lima_ribu") limaRibu: Int,
        @Query("dua_ribu") duaRibu:  Int,
        @Query("seribu") seribu: Int,
        @Query("lima_ratus") limaRatus: Int,
        @Query("dua_ratus") duaRatus: Int,
        @Query("seratus") seratus: Int,
        @Query("lima_puluh") limaPuluh: Int,
        @Query("dua_lima") duaLima: Int
    ): Call<Response>

    @GET("report/mysales/today")
    fun getSalesToday(
    ): Call<MySalesResponse>

    @GET("report/mysales/weekly")
    fun getSalesWeekly(
    ): Call<MySalesResponse>

    @GET("report/mysales/monthly")
    fun getSalesMonthly(
    ): Call<MySalesResponse>

    @GET("report/salesitem")
    fun getSaleItems(
        @Query("durasi") time: String,
        @Query("chusr") chusr: String
    ):Call<SalesItemListResponse>
}