package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Invoice implements Serializable {

    private static final long serialVersionUID = 6395063872762457290L;

    @ColumnInfo(name = "normal_total_kamar_penjualan_sebelum_service")
    @SerializedName("normal_total_kamar_penjualan_sebelum_service")
    private double totalRoomAndInventory;

    @ColumnInfo(name = "normal_total_all")
    @SerializedName("normal_total_all")
    private double totalAll;

    @ColumnInfo(name = "normal_sewa_kamar")
    @SerializedName("normal_sewa_kamar")
    private double totalRoom;

    @ColumnInfo(name = "normal_extend_kamar")
    @SerializedName("normal_extend_kamar")
    private double totalExtendRoom;

    @ColumnInfo(name = "normal_penjualan")
    @SerializedName("normal_penjualan")
    private double totalInventory;

    @ColumnInfo(name = "normal_cancelation_penjualan")
    @SerializedName("normal_cancelation_penjualan")
    private double totalInventoryCancelation;

    @ColumnInfo(name = "normal_diskon_member_penjualan")
    @SerializedName("normal_diskon_member_penjualan")
    private double totalInventoryDiscount;

    @ColumnInfo(name = "normal_surcharge_kamar")
    @SerializedName("normal_surcharge_kamar")
    private double totalSurchargeRoom;

    @ColumnInfo(name = "normal_pajak")
    @SerializedName("normal_pajak")
    private double totalTax;

    @ColumnInfo(name = "normal_overpax_kamar")
    @SerializedName("normal_overpax_kamar")
    private double totalOverpack;

    @ColumnInfo(name = "normal_diskon_member_kamar")
    @SerializedName("normal_diskon_member_kamar")
    private double totalDiscountRoom;

    @ColumnInfo(name = "normal_service")
    @SerializedName("normal_service")
    private double totalService;

    @ColumnInfo(name = "normal_voucher")
    @SerializedName("normal_voucher")
    private double totalVoucher;

    @ColumnInfo(name = "normal_gift_voucher")
    @SerializedName("normal_gift_voucher")
    private boolean isGift;

    @ColumnInfo(name = "normal_uang_muka")
    @SerializedName("normal_uang_muka")
    private double totalUangMuka;

    @ColumnInfo(name = "normal_total_final")
    @SerializedName("normal_total_final")
    private double totalFinal;

    @ColumnInfo(name = "transfer_total_all")
    @SerializedName("transfer_total_all")
    private double totalTransfer;

    @ColumnInfo(name = "ringkasan_sewa_kamar")
    @SerializedName("ringkasan_sewa_kamar")
    private double ringkasanSewakamar;

    @ColumnInfo(name = "ringkasan_extend_kamar")
    @SerializedName("ringkasan_extend_kamar")
    private double ringkasanExtendKamar;

    @ColumnInfo(name = "ringkasan_overpax_kamar")
    @SerializedName("ringkasan_overpax_kamar")
    private double ringkasanOverpaxKamar;

    @ColumnInfo(name = "ringkasan_diskon_member_kamar")
    @SerializedName("ringkasan_diskon_member_kamar")
    private double ringkasanDiskonMemberKamar;

    @ColumnInfo(name = "ringkasan_surcharge_kamar")
    @SerializedName("ringkasan_surcharge_kamar")
    private double ringkasanSurchargeKamar;

    @ColumnInfo(name = "ringkasan_total_kamar")
    @SerializedName("ringkasan_total_kamar")
    private double ringkasanTotalKamar;

    @ColumnInfo(name = "ringkasan_penjualan")
    @SerializedName("ringkasan_penjualan")
    private double ringkasanPenjualan;

    @ColumnInfo(name = "ringkasan_cancelation_penjualan")
    @SerializedName("ringkasan_cancelation_penjualan")
    private double ringkasanCancelationPenjualan;

    @ColumnInfo(name = "ringkasan_diskon_member_penjualan")
    @SerializedName("ringkasan_diskon_member_penjualan")
    private double ringkasanDiskonMemberPenjualan;

    @ColumnInfo(name = "ringkasan_total_penjualan")
    @SerializedName("ringkasan_total_penjualan")
    private double ringkasanTotalPenjualan;

    @ColumnInfo(name = "ringkasan_service")
    @SerializedName("ringkasan_service")
    private double ringkasanService;

    @ColumnInfo(name = "ringkasan_pajak")
    @SerializedName("ringkasan_pajak")
    private double ringkasanPajak;

    @ColumnInfo(name = "ringkasan_total_all")
    @SerializedName("ringkasan_total_all")
    private double ringkasanTotalAll;

    @ColumnInfo(name = "ringkasan_sub_sub_total")
    @SerializedName("ringkasan_sub_sub_total")
    private double ringkasanSubSubTotal;

    @ColumnInfo(name = "ringkasan_sub_total")
    @SerializedName("ringkasan_sub_total")
    private double ringkasanSubTotal;


    public double getTotalTransfer() {
        return totalTransfer;
    }

    public double getTotalVoucher() {
        return totalVoucher;
    }

    public boolean isGift() {
        return isGift;
    }

    public double getTotalUangMuka() {
        return totalUangMuka;
    }

    public double getTotalRoomAndInventory() {
        return totalRoomAndInventory;
    }

    public double getTotalAll() {
        return totalAll;
    }

    public double getTotalRoom() {
        return totalRoom;
    }

    public double getTotalInventory() {
        return totalInventory;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public double getTotalService() {
        return totalService;
    }

    public double getTotalFinal() {
        return totalFinal;
    }

    public double getRingkasanSewakamar() {
        return ringkasanSewakamar;
    }

    public double getRingkasanExtendKamar() {
        return ringkasanExtendKamar;
    }

    public double getRingkasanOverpaxKamar() {
        return ringkasanOverpaxKamar;
    }

    public double getRingkasanDiskonMemberKamar() {
        return ringkasanDiskonMemberKamar;
    }

    public double getRingkasanSurchargeKamar() {
        return ringkasanSurchargeKamar;
    }

    public double getRingkasanPenjualan() {
        return ringkasanPenjualan;
    }

    public double getRingkasanCancelationPenjualan() {
        return ringkasanCancelationPenjualan;
    }

    public double getRingkasanDiskonMemberPenjualan() {
        return ringkasanDiskonMemberPenjualan;
    }

    public double getRingkasanService() {
        return ringkasanService;
    }

    public double getRingkasanPajak() {
        return ringkasanPajak;
    }

    public double getRingkasanTotalAll() {
        return ringkasanTotalAll;
    }

    public double getRingkasanSubSubTotal() {
        return ringkasanSubSubTotal;
    }

    public double getRingkasanSubTotal() {
        return ringkasanSubTotal;
    }
}