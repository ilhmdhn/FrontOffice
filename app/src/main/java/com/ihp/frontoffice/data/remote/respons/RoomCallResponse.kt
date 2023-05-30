package com.ihp.frontoffice.data.remote.respons

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoomCallResponse(

    @field:SerializedName("state")
    val state: Boolean = false,

    @field:SerializedName("data")
    val data: List<DataRoomCall> = mutableListOf(),

    @field:SerializedName("message")
    val message: String = ""
) : Parcelable

@Parcelize
data class DataRoomCall(

    @field:SerializedName("is_now")
    val isNow: Int,

    @field:SerializedName("notif_type")
    val type: Int,

    @field:SerializedName("room")
    val room: String,

    @field:SerializedName("description")
    val keterangan: String,

    @field:SerializedName("user")
    val user: String,

    @field:SerializedName("call_time")
    val callTime: String,

    @field:SerializedName("call_response")
    val responseTime: String
) : Parcelable
