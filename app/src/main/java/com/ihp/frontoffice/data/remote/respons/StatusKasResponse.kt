package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class StatusKasResponse(

	@field:SerializedName("data")
	val dataStatusKas: DataStatusKas? = null,

	@field:SerializedName("state")
	val state: Boolean? = null,

	@field:SerializedName("message")
	val message: String = ""
)

data class DataStatusKas(

	@field:SerializedName("jumlah_tamu_piutang")
	val jumlahTamuPiutang: Long? = 0,

	@field:SerializedName("jumlah_reservasi_belum_checkin")
	val jumlahReservasiBelumCheckin: Long? = 0,

	@field:SerializedName("jumlah_jam_piutang")
	val jumlahJamPiutang: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_emoney")
	val jumlahPembayaranEmoney: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_piutang")
	val jumlahPembayaranPiutang: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_transfer")
	val jumlahPembayaranTransfer: Long? = 0,

	@field:SerializedName("jumlah_reservasi_sudah_checkin_belum_bayar")
	val jumlahReservasiSudahCheckinBelumBayar: Long? = 0,

	@field:SerializedName("hutang_smart_card")
	val hutangSmartCard: Long? = 0,

	@field:SerializedName("total_pembayaran")
	val totalPembayaran: Long? = 0,

	@field:SerializedName("jumlah_tamu_sudah_bayar")
	val jumlahTamuSudahBayar: Long? = 0,

	@field:SerializedName("jumlah_checkin_piutang")
	val jumlahCheckinPiutang: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_poin_membership")
	val jumlahPembayaranPoinMembership: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_voucher")
	val jumlahPembayaranVoucher: Long? = 0,

	@field:SerializedName("jumlah_nilai_kamar")
	val jumlahNilaiKamar: Long? = 0,

	@field:SerializedName("total_penjualan")
	val totalPenjualan: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_uang_muka")
	val jumlahPembayaranUangMuka: Long? = 0,

	@field:SerializedName("jumlah_pendapatan_lain")
	val jumlahPendapatanLain: Long? = 0,

	@field:SerializedName("makanan_minuman")
	val makananMinuman: Long? = 0,

	@field:SerializedName("total_hutang_reservasi")
	val totalHutangReservasi: Long? = 0,

	@field:SerializedName("jumlah_checkin_sudah_bayar")
	val jumlahCheckinSudahBayar: Long? = 0,

	@field:SerializedName("jumlah_uang_muka_checkin_sudah_belum_bayar")
	val jumlahUangMukaCheckinSudahBelumBayar: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_complimentary")
	val jumlahPembayaranComplimentary: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_credit_card")
	val jumlahPembayaranCreditCard: Long? = 0,

	@field:SerializedName("jumlah_reservasi_sudah_checkin")
	val jumlahReservasiSudahCheckin: Long? = 0,

	@field:SerializedName("jumlah_pembayaran_cash")
	val jumlahPembayaranCash: Long = 0,

	@field:SerializedName("jumlah_jam_sudah_bayar")
	val jumlahJamSudahBayar: Long = 0,

	@field:SerializedName("jumlah_pembayaran_smart_card")
	val jumlahPembayaranSmartCard: Long = 0,

	@field:SerializedName("tanggal")
	val tanggal: String = "",

	@field:SerializedName("jumlah_pembayaran_debet_card")
	val jumlahPembayaranDebetCard: Long = 0,

	@field:SerializedName("piutang_room")
	val piutangRoom: Long = 0,

	@field:SerializedName("piutang_fnb")
	val piutangFnb: Long = 0,

	@field:SerializedName("uang_muka")
	val uangMuka: Long = 0
)
