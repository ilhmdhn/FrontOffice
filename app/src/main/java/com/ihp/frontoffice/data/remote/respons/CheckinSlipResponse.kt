package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class CheckinSlipResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("state")
	val state: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class OutletInfo(

	@field:SerializedName("kota")
	val kota: String? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("alamat_outlet")
	val alamatOutlet: String? = null,

	@field:SerializedName("nama_outlet")
	val namaOutlet: String? = null
)

data class CheckinDetail(

	@field:SerializedName("checkin_time")
	val checkinTime: String? = null,

	@field:SerializedName("room_name")
	val roomName: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("pax")
	val pax: Int? = null,

	@field:SerializedName("checkout_time")
	val checkoutTime: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("checkin_fee")
	val checkinFee: Int? = null,

	@field:SerializedName("checkin_duration")
	val checkinDuration: String? = null,

	@field:SerializedName("room_type")
	val roomType: String? = null
)

data class Data(

	@field:SerializedName("checkin_detail")
	val checkinDetail: CheckinDetail? = null,

	@field:SerializedName("outlet_info")
	val outletInfo: OutletInfo? = null
)
