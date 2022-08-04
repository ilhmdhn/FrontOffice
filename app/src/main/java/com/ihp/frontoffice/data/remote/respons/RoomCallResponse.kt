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

    @field:SerializedName("Kamar")
    val kamar: String,

    @field:SerializedName("Nama_Tamu")
    val namaTamu: String? = null,

    @field:SerializedName("Service_Kamar")
    val serviceKamar: Int? = null,

    @field:SerializedName("Kamar_Alias")
    val kamarAlias: String? = null
) : Parcelable
