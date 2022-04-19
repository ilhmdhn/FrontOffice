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

    public void setTotalTransfer(double totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    public double getTotalVoucher() {
        return totalVoucher;
    }

    public void setTotalVoucher(double totalVoucher) {
        this.totalVoucher = totalVoucher;
    }

    public boolean isGift() {
        return isGift;
    }

    public void setGift(boolean gift) {
        isGift = gift;
    }

    public double getTotalUangMuka() {
        return totalUangMuka;
    }

    public void setTotalUangMuka(double totalUangMuka) {
        this.totalUangMuka = totalUangMuka;
    }

    public double getTotalRoomAndInventory() {
        return totalRoomAndInventory;
    }

    public void setTotalRoomAndInventory(double totalRoomAndInventory) {
        this.totalRoomAndInventory = totalRoomAndInventory;
    }

    public double getTotalAll() {
        return totalAll;
    }

    public void setTotalAll(double totalAll) {
        this.totalAll = totalAll;
    }

    public double getTotalRoom() {
        return totalRoom;
    }

    public void setTotalRoom(double totalRoom) {
        this.totalRoom = totalRoom;
    }

    public double getTotalExtendRoom() {
        return totalExtendRoom;
    }

    public void setTotalExtendRoom(double totalExtendRoom) {
        this.totalExtendRoom = totalExtendRoom;
    }

    public double getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(double totalInventory) {
        this.totalInventory = totalInventory;
    }

    public double getTotalInventoryCancelation() {
        return totalInventoryCancelation;
    }

    public void setTotalInventoryCancelation(double totalInventoryCancelation) {
        this.totalInventoryCancelation = totalInventoryCancelation;
    }

    public double getTotalInventoryDiscount() {
        return totalInventoryDiscount;
    }

    public void setTotalInventoryDiscount(double totalInventoryDiscount) {
        this.totalInventoryDiscount = totalInventoryDiscount;
    }

    public double getTotalSurchargeRoom() {
        return totalSurchargeRoom;
    }

    public void setTotalSurchargeRoom(double totalSurchargeRoom) {
        this.totalSurchargeRoom = totalSurchargeRoom;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public double getTotalOverpack() {
        return totalOverpack;
    }

    public void setTotalOverpack(double totalOverpack) {
        this.totalOverpack = totalOverpack;
    }

    public double getTotalDiscountRoom() {
        return totalDiscountRoom;
    }

    public void setTotalDiscountRoom(double totalDiscountRoom) {
        this.totalDiscountRoom = totalDiscountRoom;
    }

    public double getTotalService() {
        return totalService;
    }

    public void setTotalService(double totalService) {
        this.totalService = totalService;
    }

    public double getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(double totalFinal) {
        this.totalFinal = totalFinal;
    }

    public double getRingkasanSewakamar() {
        return ringkasanSewakamar;
    }

    public void setRingkasanSewakamar(double ringkasanSewakamar) {
        this.ringkasanSewakamar = ringkasanSewakamar;
    }

    public double getRingkasanExtendKamar() {
        return ringkasanExtendKamar;
    }

    public void setRingkasanExtendKamar(double ringkasanExtendKamar) {
        this.ringkasanExtendKamar = ringkasanExtendKamar;
    }

    public double getRingkasanOverpaxKamar() {
        return ringkasanOverpaxKamar;
    }

    public void setRingkasanOverpaxKamar(double ringkasanOverpaxKamar) {
        this.ringkasanOverpaxKamar = ringkasanOverpaxKamar;
    }

    public double getRingkasanDiskonMemberKamar() {
        return ringkasanDiskonMemberKamar;
    }

    public void setRingkasanDiskonMemberKamar(double ringkasanDiskonMemberKamar) {
        this.ringkasanDiskonMemberKamar = ringkasanDiskonMemberKamar;
    }

    public double getRingkasanSurchargeKamar() {
        return ringkasanSurchargeKamar;
    }

    public void setRingkasanSurchargeKamar(double ringkasanSurchargeKamar) {
        this.ringkasanSurchargeKamar = ringkasanSurchargeKamar;
    }

    public double getRingkasanTotalKamar() {
        return ringkasanTotalKamar;
    }

    public void setRingkasanTotalKamar(double ringkasanTotalKamar) {
        this.ringkasanTotalKamar = ringkasanTotalKamar;
    }

    public double getRingkasanPenjualan() {
        return ringkasanPenjualan;
    }

    public void setRingkasanPenjualan(double ringkasanPenjualan) {
        this.ringkasanPenjualan = ringkasanPenjualan;
    }

    public double getRingkasanCancelationPenjualan() {
        return ringkasanCancelationPenjualan;
    }

    public void setRingkasanCancelationPenjualan(double ringkasanCancelationPenjualan) {
        this.ringkasanCancelationPenjualan = ringkasanCancelationPenjualan;
    }

    public double getRingkasanDiskonMemberPenjualan() {
        return ringkasanDiskonMemberPenjualan;
    }

    public void setRingkasanDiskonMemberPenjualan(double ringkasanDiskonMemberPenjualan) {
        this.ringkasanDiskonMemberPenjualan = ringkasanDiskonMemberPenjualan;
    }

    public double getRingkasanTotalPenjualan() {
        return ringkasanTotalPenjualan;
    }

    public void setRingkasanTotalPenjualan(double ringkasanTotalPenjualan) {
        this.ringkasanTotalPenjualan = ringkasanTotalPenjualan;
    }

    public double getRingkasanService() {
        return ringkasanService;
    }

    public void setRingkasanService(double ringkasanService) {
        this.ringkasanService = ringkasanService;
    }

    public double getRingkasanPajak() {
        return ringkasanPajak;
    }

    public void setRingkasanPajak(double ringkasanPajak) {
        this.ringkasanPajak = ringkasanPajak;
    }

    public double getRingkasanTotalAll() {
        return ringkasanTotalAll;
    }

    public void setRingkasanTotalAll(double ringkasanTotalAll) {
        this.ringkasanTotalAll = ringkasanTotalAll;
    }

    public double getRingkasanSubSubTotal() {
        return ringkasanSubSubTotal;
    }

    public void setRingkasanSubSubTotal(double ringkasanSubSubTotal) {
        this.ringkasanSubSubTotal = ringkasanSubSubTotal;
    }

    public double getRingkasanSubTotal() {
        return ringkasanSubTotal;
    }

    public void setRingkasanSubTotal(double ringkasanSubTotal) {
        this.ringkasanSubTotal = ringkasanSubTotal;
    }
}
