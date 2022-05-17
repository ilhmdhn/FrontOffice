package livs.code.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class PecahanUangResponse(

	@field:SerializedName("data")
	val pecahanUangData: PecahanUangData? = null,

	@field:SerializedName("state")
	val state: Boolean? = false,

	@field:SerializedName("message")
	val message: String? = ""
)

data class PecahanUangData(

	@field:SerializedName("seratus_ribu")
	val seratusRibu: Int? = 0,

	@field:SerializedName("dua_puluh_ribu")
	val duaPuluhRibu: Int? = 0,

	@field:SerializedName("dua_ratus")
	val duaRatus: Int? = 0,

	@field:SerializedName("shift")
	val shift: String? = "",

	@field:SerializedName("seratus")
	val seratus: Int? = 0,

	@field:SerializedName("lima_puluh")
	val limaPuluh: Int? = 0,

	@field:SerializedName("lima_ribu")
	val limaRibu: Int? = 0,

	@field:SerializedName("lima_ratus")
	val limaRatus: Int? = 0,

	@field:SerializedName("sepuluh_ribu")
	val sepuluhRibu: Int? = 0,

	@field:SerializedName("lima_puluh_ribu")
	val limaPuluhRibu: Int? = 0,

	@field:SerializedName("tanggal")
	val tanggal: String? = "",

	@field:SerializedName("dua_ribu")
	val duaRibu: Int? = 0,

	@field:SerializedName("seribu")
	val seribu: Int? = 0,

	@field:SerializedName("dua_puluh_lima")
	val duaPuluhLima: Int? = 0,

	@field:SerializedName("status")
	val status: Int? = 0
)
