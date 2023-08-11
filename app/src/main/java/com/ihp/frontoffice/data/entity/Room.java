package com.ihp.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Room implements Serializable {

//    private static final long serialVersionUID = -9176475615850942210L;

    @ColumnInfo(name = "room_number")
    @SerializedName("kamar")
    private String roomCode;

    @ColumnInfo(name = "room_type")
    @SerializedName("jenis_kamar")
    private String roomType;

    @ColumnInfo(name = "room_capacity")
    @SerializedName("kapasitas")
    private int roomCapacity;

    @ColumnInfo(name = "invoice_transfer")
    @SerializedName("invoice_transfer")
    private String ivcTransfer = "";


    @ColumnInfo(name = "invoice")
    @SerializedName("invoice")
    private String invoice;

    @ColumnInfo(name = "room_alias")
    @SerializedName("room_alias")
    private String roomAlias;


    @SerializedName("status_print")
    private String statusPrinter;

    @ColumnInfo(name = "room_state")
    @SerializedName("status_kamar")
    private int roomState;

    @ColumnInfo(name = "room_call_service")
    @SerializedName("call_service_kamar")
    private int roomCallService;

    @ColumnInfo(name = "room_call")
    @SerializedName("room_call")
    private boolean roomCall;

    @ColumnInfo(name = "room_not_lobby")
    @SerializedName("kamar_untuk_checkin")
    private boolean roomNotLobby;

    @ColumnInfo(name = "room_rcp")
    @SerializedName("reception")
    private String roomRcp;

    @ColumnInfo(name = "room_ivc")
    @SerializedName("room_ivc")
    private String roomIvc;

    @ColumnInfo(name = "room_guess_name")
    @SerializedName("nama_member")
    private String roomGuessName;

    @ColumnInfo(name = "event_owner")
    @SerializedName("event_owner")
    private String eventOwner;

    @ColumnInfo(name = "member_ref")
    @SerializedName("member_ref")
    private String codeMemberRef;

    @ColumnInfo(name = "img_guess_path")
    @SerializedName("photo_url_lokal")
    private String imgGuessPath;

    @ColumnInfo(name = "room_guess_hp")
    @SerializedName("hp")
    private String roomGuessHp;

    @ColumnInfo(name = "event_desc")
    @SerializedName("keterangan_tamu")
    private String eventDesc;

    @ColumnInfo(name = "desc")
    @SerializedName("keterangan")
    private String desc;

    @ColumnInfo(name = "room_ip_address")
    @SerializedName("ip_address")
    private String roomIpAddress;

    @ColumnInfo(name = "room_chusr")
    @SerializedName("chusr")
    private String roomChusr;

    @ColumnInfo(name = "member_code")
    @SerializedName("kode_member")
    private String memberCode;

    @ColumnInfo(name = "member_name")
    @SerializedName("nama")
    private String memberName;

    @ColumnInfo(name = "room_checkin_duration")
    @SerializedName("durasi_checkin")
    private int roomCheckinDuration;

    @ColumnInfo(name = "room_residual_checkin_time")
    @SerializedName("sisa_checkin")
    private int roomResidualCheckinTime;

    @ColumnInfo(name = "room_residual_checkin_hours_time")
    @SerializedName("sisa_jam_checkin")
    private int roomResidualCheckinHoursTime;

    @ColumnInfo(name = "total_all")
    @SerializedName("total_all")
    private double totalAllInvoice;

    @ColumnInfo(name = "room_residual_checkin_minutes_time")
    @SerializedName("sisa_menit_checkin")
    private int roomResidualCheckinHoursMinutesTime;

    @ColumnInfo(name = "room_checkin_hours")
    @SerializedName("jam_checkin")
    private Date roomCheckinHours;

    @ColumnInfo(name = "room_checkout_hours")
    @SerializedName("jam_checkout")
    private Date roomCheckoutHours;

    @ColumnInfo(name = "qm1")
    @SerializedName("qm1")
    private int qm1;
    @ColumnInfo(name = "qm2")
    @SerializedName("qm2")
    private int qm2;
    @ColumnInfo(name = "qm3")
    @SerializedName("qm3")
    private int qm3;
    @ColumnInfo(name = "qm4")
    @SerializedName("qm4")
    private int qm4;

    @ColumnInfo(name = "qf1")
    @SerializedName("qf1")
    private int qf1;
    @ColumnInfo(name = "qf2")
    @SerializedName("qf2")
    private int qf2;
    @ColumnInfo(name = "qf3")
    @SerializedName("qf3")
    private int qf3;
    @ColumnInfo(name = "qf4")
    @SerializedName("qf4")
    private int qf4;

    @ColumnInfo(name = "total_visitor")
    @SerializedName("pax")
    private String totalVisitor;

    @ColumnInfo(name = "overpax_visitor")
    @SerializedName("overpax")
    private String overpaxVisitor;

    @ColumnInfo(name = "tanggal_extend")
    @SerializedName("tanggal_extend")
    private Date timeExtends;

    @ColumnInfo(name = "jam_extend")
    @SerializedName("jam_extend")
    private int extendHours;

    @ColumnInfo(name = "menit_extend")
    @SerializedName("menit_extend")
    private int extendMinutes;

    @ColumnInfo(name = "transfer_info")
    @SerializedName("keterangan_transfer")
    private String transferInfo;

    @ColumnInfo(name = "keterangan_status_promo")
    @SerializedName("keterangan_status_promo")
    private String keteranganStatusPromo;

    @ColumnInfo(name = "dp_payment_type")
    @SerializedName("keterangan_payment_uang_muka")
    private String dpPaymentType;

    @ColumnInfo(name = "dp_payment_nominal")
    @SerializedName("uang_muka")
    private int dpPaymentNominal;

    @ColumnInfo(name = "dp_card_type")
    @SerializedName("input1_jenis_kartu")
    private String dpCardType;

    @ColumnInfo(name = "dp_edc")
    @SerializedName("edc_machine")
    private String dpEdc;

    @ColumnInfo(name = "dp_edc_code")
    @SerializedName("edc_machine_")
    private String codeDpEdc;

    @ColumnInfo(name = "dp_card_name")
    @SerializedName("input2_nama")
    private String dpCardName;

    @ColumnInfo(name = "dp_card_number")
    @SerializedName("input3_nomor_kartu")
    private String dpCardNumber;

    @ColumnInfo(name = "dp_card_approval_number")
    @SerializedName("input4_nomor_approval")
    private String dpCardApprovalNumber;

    @ColumnInfo(name = "voucher_type")
    @SerializedName("voucher")
    private String voucherCode;

    @ColumnInfo(name = "voucher_nominal")
    @SerializedName("pay_value_voucher")
    private String voucherNominal;

    @ColumnInfo(name = "charge_penjualan")
    @SerializedName("charge_penjualan")
    private String chargePenjualan;
    @SerializedName("summary_order_inventory")
    private List<Inventory> summaryOrderInventories;

    @SerializedName("order_inventory")
    private List<Inventory> inventoryOrder;

    @SerializedName("order_inventory_cancelation")
    private List<Inventory> inventoryCancelation;

    @SerializedName("order_inventory_progress")
    private List<Inventory> inventoryOnOrderProgress;


    public String getChargePenjualan(){return chargePenjualan;}
    public List<Inventory> getSummaryOrderInventories() {
        return summaryOrderInventories;
    }

    public List<Inventory> getInventoryCancelation() {
        return inventoryCancelation;
    }

    public List<Inventory> getInventoryOnOrderProgress() {
        return inventoryOnOrderProgress;
    }

    public boolean isRoomCall() {
        return roomCall;
    }

    public String getDpPaymentType() {
        return dpPaymentType;
    }

    public int getDpPaymentNominal() {
        return dpPaymentNominal;
    }

    public String getDpCardType() {
        return dpCardType;
    }

    public String getDpEdc() {
        return dpEdc;
    }

    public String getDpCardName() {
        return dpCardName;
    }

    public String getDpCardNumber() {
        return dpCardNumber;
    }

    public String getDpCardApprovalNumber() {
        return dpCardApprovalNumber;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKeteranganStatusPromo() {
        return keteranganStatusPromo;
    }

    public String getTransferInfo() {
        return transferInfo;
    }

    public String getOverpaxVisitor() {
        return overpaxVisitor;
    }

    public Date getTimeExtends() {
        return timeExtends;
    }

    public int getExtendHours() {
        return extendHours;
    }

    public int getExtendMinutes() {
        return extendMinutes;
    }

    public String getRoomGuessHp() {
        return roomGuessHp;
    }

    public int getQm1() {
        return qm1;
    }

    public String getStatusPrinter(){
        return statusPrinter;
    }

    public int getQm2() {
        return qm2;
    }

    public int getQm3() {
        return qm3;
    }

    public int getQm4() {
        return qm4;
    }

    public int getQf1() {
        return qf1;
    }

    public int getQf2() {
        return qf2;
    }

    public int getQf3() {
        return qf3;
    }

    public int getQf4() {
        return qf4;
    }

    public String getAlias(){
        return roomAlias;
    }

    public String getTotalVisitor() {
        return totalVisitor;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public boolean isRoomNotLobby() {
        return roomNotLobby;
    }

    public String getRoomRcp() {
        return roomRcp;
    }

    public String getRoomGuessName() {
        return roomGuessName;
    }

    public void setRoomGuessName(String roomGuessName) {
        this.roomGuessName = roomGuessName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getRoomCheckinDuration() {
        return roomCheckinDuration;
    }

    public Date getRoomCheckinHours() {
        return roomCheckinHours;
    }

    public Date getRoomCheckoutHours() {
        return roomCheckoutHours;
    }

    public int getRoomResidualCheckinHoursTime() {
        return roomResidualCheckinHoursTime;
    }

    public int getRoomResidualCheckinHoursMinutesTime() {
        return roomResidualCheckinHoursMinutesTime;
    }

    public String getIvcTransfer() {
        return ivcTransfer;
    }

    public void setRoomResidualCheckinHoursMinutesTime(int roomResidualCheckinHoursMinutesTime) {
        this.roomResidualCheckinHoursMinutesTime = roomResidualCheckinHoursMinutesTime;
    }

    public double getTotalAllInvoice() {
        return totalAllInvoice;
    }

    public String getCodeMemberRef() {
        return codeMemberRef;
    }

    public String getInvoice() {
        return invoice;
    }
}