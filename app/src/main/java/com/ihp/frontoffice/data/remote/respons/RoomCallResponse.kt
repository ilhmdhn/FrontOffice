package com.ihp.frontoffice.data.remote.respons

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoomCallResponse(

    @field:SerializedName("state")
    val state: Boolean? = null,

    @field:SerializedName("data")
    val data: List<DataRoomCall?>? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class DataRoomCall(

    @field:SerializedName("waktu")
    val isNow: Int,

    @field:SerializedName("notif_type")
    val type: Int,

    @field:SerializedName("room")
    val room: String,

    @field:SerializedName("keterangan")
    val keterangan: String,

    @field:SerializedName("chusr")
    val user: String,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("call_date")
    val dateCall: String
) : Parcelable
