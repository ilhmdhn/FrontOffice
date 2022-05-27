package livs.code.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class StatusKasResponse(

	@field:SerializedName("data")
	val dataStatusKas: DataStatusKas,

	@field:SerializedName("state")
	val state: Boolean? = null,

	@field:SerializedName("message")
	val message: String = ""
)

data class DataStatusKas(

	@field:SerializedName("jumlah_tamu_piutang")
	val jumlahTamuPiutang: Int? = 0,

	@field:SerializedName("jumlah_reservasi_belum_checkin")
	val jumlahReservasiBelumCheckin: Int? = 0,

	@field:SerializedName("jumlah_jam_piutang")
	val jumlahJamPiutang: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_emoney")
	val jumlahPembayaranEmoney: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_piutang")
	val jumlahPembayaranPiutang: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_transfer")
	val jumlahPembayaranTransfer: Int? = 0,

	@field:SerializedName("jumlah_reservasi_sudah_checkin_belum_bayar")
	val jumlahReservasiSudahCheckinBelumBayar: Int? = 0,

	@field:SerializedName("hutang_smart_card")
	val hutangSmartCard: Int? = 0,

	@field:SerializedName("total_pembayaran")
	val totalPembayaran: Int? = 0,

	@field:SerializedName("jumlah_tamu_sudah_bayar")
	val jumlahTamuSudahBayar: Int? = 0,

	@field:SerializedName("jumlah_checkin_piutang")
	val jumlahCheckinPiutang: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_poin_membership")
	val jumlahPembayaranPoinMembership: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_voucher")
	val jumlahPembayaranVoucher: Int? = 0,

	@field:SerializedName("jumlah_nilai_kamar")
	val jumlahNilaiKamar: Int? = 0,

	@field:SerializedName("total_penjualan")
	val totalPenjualan: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_uang_muka")
	val jumlahPembayaranUangMuka: Int? = 0,

	@field:SerializedName("jumlah_pendapatan_lain")
	val jumlahPendapatanLain: Int? = 0,

	@field:SerializedName("makanan_minuman")
	val makananMinuman: Int? = 0,

	@field:SerializedName("total_hutang_reservasi")
	val totalHutangReservasi: Int? = 0,

	@field:SerializedName("jumlah_checkin_sudah_bayar")
	val jumlahCheckinSudahBayar: Int? = 0,

	@field:SerializedName("jumlah_uang_muka_checkin_sudah_belum_bayar")
	val jumlahUangMukaCheckinSudahBelumBayar: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_complimentary")
	val jumlahPembayaranComplimentary: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_credit_card")
	val jumlahPembayaranCreditCard: Int? = 0,

	@field:SerializedName("jumlah_reservasi_sudah_checkin")
	val jumlahReservasiSudahCheckin: Int? = 0,

	@field:SerializedName("jumlah_pembayaran_cash")
	val jumlahPembayaranCash: Int = 0,

	@field:SerializedName("jumlah_jam_sudah_bayar")
	val jumlahJamSudahBayar: Int = 0,

	@field:SerializedName("jumlah_pembayaran_smart_card")
	val jumlahPembayaranSmartCard: Int = 0,

	@field:SerializedName("tanggal")
	val tanggal: String = "",

	@field:SerializedName("jumlah_pembayaran_debet_card")
	val jumlahPembayaranDebetCard: Int = 0,

	@field:SerializedName("piutang_room")
	val piutangRoom: Int = 0,

	@field:SerializedName("piutang_fnb")
	val piutangFnb: Int = 0,

	@field:SerializedName("uang_muka")
	val uangMuka: Int = 0
)
