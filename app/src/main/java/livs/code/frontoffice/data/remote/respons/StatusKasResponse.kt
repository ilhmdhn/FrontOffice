package livs.code.frontoffice.data.remote.respons

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class StatusKasResponse(

	@field:SerializedName("data")
    var dataStatusKas: DataStatusKas,

	@field:SerializedName("state")
	var state: Boolean? = null,

	@field:SerializedName("message")
	var message: String? = "berhasil"
)

@Parcelize
data class DataStatusKas(

	@field:SerializedName("jumlah_checkin_sudah_bayar")
	val jumlahCheckinSudahBayar: Int = 0,

	@field:SerializedName("jumlah_tamu_piutang")
	val jumlahTamuPiutang: Int = 0,

	@field:SerializedName("jumlah_jam_piutang")
	val jumlahJamPiutang: Int = 0,

	@field:SerializedName("jumlah_pembayaran_piutang")
	val jumlahPembayaranPiutang: Int = 0,

	@field:SerializedName("jumlah_pembayaran_emoney")
	val jumlahPembayaranEmoney: Int = 0,

	@field:SerializedName("jumlah_pembayaran_transfer")
	val jumlahPembayaranTransfer: Int = 0,

	@field:SerializedName("jumlah_tamu_sudah_bayar")
	val jumlahTamuSudahBayar: Int = 0,

	@field:SerializedName("jumlah_checkin_piutang")
	val jumlahCheckinPiutang: Int = 0,

	@field:SerializedName("jumlah_pembayaran_voucher")
	val jumlahPembayaranVoucher: Int = 0,

	@field:SerializedName("jumlah_pembayaran_complimentary")
	val jumlahPembayaranComplimentary: Int = 0,

	@field:SerializedName("jumlah_pembayaran_credit_card")
	val jumlahPembayaranCreditCard: Int = 0,

	@field:SerializedName("jumlah_pembayaran_uang_muka")
	val jumlahPembayaranUangMuka: Int = 0,

	@field:SerializedName("jumlah_pembayaran_cash")
	val jumlahPembayaranCash: Int = 0,

	@field:SerializedName("jumlah_jam_sudah_bayar")
	val jumlahJamSudahBayar: Int = 0,

	@field:SerializedName("jumlah_pembayaran_smart_card")
	val jumlahPembayaranSmartCard: Int = 0,

	@field:SerializedName("tanggal")
	val tanggal: String? = "00-00-0000",

	@field:SerializedName("jumlah_pendapatan_lain")
	val jumlahPendapatanLain: Int = 0,

	@field:SerializedName("jumlah_pembayaran_debet_card")
	val jumlahPembayaranDebetCard: Int = 0,

	@field:SerializedName("jumlah_uang_muka_checkin_belum_bayar")
	val jumlahUangMukaCheckinBelumBayar: Int = 0
): Parcelable
