package livs.code.frontoffice.data.remote.respons

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RomCallResponse(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("length")
    val length: Int? = null,

    @field:SerializedName("state")
    val state: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class DataItem(

    @field:SerializedName("Nama_Tamu_Alias")
    val namaTamuAlias: String? = null,

    @field:SerializedName("Kamar")
    val kamar: String? = null,

    @field:SerializedName("Nama_Tamu")
    val namaTamu: String? = null,

    @field:SerializedName("Service_Kamar")
    val serviceKamar: Int? = null,

    @field:SerializedName("Kamar_Alias")
    val kamarAlias: String? = null
) : Parcelable
