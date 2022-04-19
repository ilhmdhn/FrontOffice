package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomPrice implements Serializable {

    private static final long serialVersionUID = 7185581733532932535L;
    @ColumnInfo(name = "room_rcp")
    @SerializedName("reception")
    public String roomRcp;

    @ColumnInfo(name = "jenis_kamar")
    @SerializedName("jenis_kamar")
    public String jenisKamar;

    @ColumnInfo(name = "hari")
    @SerializedName("hari")
    public int hari;

    @ColumnInfo(name = "keterangan_hari")
    @SerializedName("keterangan_hari")
    public String keteranganHari;

    @ColumnInfo(name = "overpax")
    @SerializedName("overpax")
    public int overpax;

    @ColumnInfo(name = "tarif")
    @SerializedName("tarif")
    public int tarif;

    @ColumnInfo(name = "overpax_permenit")
    @SerializedName("overpax_permenit")
    public double overpaxPermenit;

    @ColumnInfo(name = "tarif_per_menit")
    @SerializedName("tarif_per_menit")
    public double tarifPerMenit;

    @ColumnInfo(name = "time_start")
    @SerializedName("time_start")
    public String time_start;

    @ColumnInfo(name = "time_finish")
    @SerializedName("time_finish")
    public String time_finish;

    @ColumnInfo(name = "room_number")
    @SerializedName("kamar")
    private String roomNmbr;

    @ColumnInfo(name = "invoice")
    @SerializedName("invoice")
    public String invoice;

    @ColumnInfo(name = "invoice_transfer")
    @SerializedName("invoice_transfer")
    public String invoiceTransfer;

    @ColumnInfo(name = "not_room_transfer")
    @SerializedName("not_room_transfer")
    public String notRoomTransfer;

    public String getRoomRcp() {
        return roomRcp;
    }

    public void setRoomRcp(String roomRcp) {
        this.roomRcp = roomRcp;
    }

    public String getJenisKamar() {
        return jenisKamar;
    }

    public void setJenisKamar(String jenisKamar) {
        this.jenisKamar = jenisKamar;
    }

    public int getHari() {
        return hari;
    }

    public void setHari(int hari) {
        this.hari = hari;
    }

    public String getKeteranganHari() {
        return keteranganHari;
    }

    public void setKeteranganHari(String keteranganHari) {
        this.keteranganHari = keteranganHari;
    }

    public int getOverpax() {
        return overpax;
    }

    public void setOverpax(int overpax) {
        this.overpax = overpax;
    }

    public int getTarif() {
        return tarif;
    }

    public void setTarif(int tarif) {
        this.tarif = tarif;
    }

    public double getOverpaxPermenit() {
        return overpaxPermenit;
    }

    public void setOverpaxPermenit(double overpaxPermenit) {
        this.overpaxPermenit = overpaxPermenit;
    }

    public double getTarifPerMenit() {
        return tarifPerMenit;
    }

    public void setTarifPerMenit(double tarifPerMenit) {
        this.tarifPerMenit = tarifPerMenit;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_finish() {
        return time_finish;
    }

    public void setTime_finish(String time_finish) {
        this.time_finish = time_finish;
    }

    public String getRoomNmbr() {
        return roomNmbr;
    }

    public void setRoomNmbr(String roomNmbr) {
        this.roomNmbr = roomNmbr;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoiceTransfer() {
        return invoiceTransfer;
    }

    public void setInvoiceTransfer(String invoiceTransfer) {
        this.invoiceTransfer = invoiceTransfer;
    }

    public String getNotRoomTransfer() {
        return notRoomTransfer;
    }

    public void setNotRoomTransfer(String notRoomTransfer) {
        this.notRoomTransfer = notRoomTransfer;
    }
}
