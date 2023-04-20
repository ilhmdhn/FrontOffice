package com.ihp.frontoffice.data.remote

import com.ihp.frontoffice.data.remote.respons.InventoryPagingResponse
import com.ihp.frontoffice.data.remote.respons.Response
import retrofit2.Call
import retrofit2.http.*

interface InventoryClient {
    @GET("inventory/list-paging")
    suspend fun getInventoryPaging(
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Query("category") category: String,
            @Query("search") search: String
    ): InventoryPagingResponse

    @FormUrlEncoded
    @POST("order/single/room/sol/sod")
    fun sendOrder(
            @Field("order_user_name") chusr: String,
            @Field("order_room_code") roomCode: String,
            @Field("order_room_rcp") roomRcp: String,
            @Field("order_room_type") roomType: String,
            @Field("order_room_durasi_checkin") checkinDuration: String,
            @Field("order_pos_ip") posIp: String,
            @Field("order_model_android") foType: String,
            @Field("arr_order_inv") orderInv: List<String>,
            @Field("arr_order_qty") orderQty: ArrayList<String>,
            @Field("arr_order_notes") orderNotes: ArrayList<String>,
            @Field("arr_order_price") orderPrice: ArrayList<String>,
            @Field("arr_order_nama_item") orderItemName: ArrayList<String>,
            @Field("arr_order_location_item") orderLocation: ArrayList<String>
    ):Call<Response>
}