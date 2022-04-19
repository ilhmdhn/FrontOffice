package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Room implements Serializable {

    private static final long serialVersionUID = -9176475615850942210L;

    @ColumnInfo(name = "room_number")
    @SerializedName("kamar")
    private String roomCode;

    @ColumnInfo(name = "room_type")
    @SerializedName("jenis_kamar")
    private String roomType;

    @ColumnInfo(name = "room_capacity")
    @SerializedName("kapasitas")
    private int roomCapacity;

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

    @SerializedName("summary_order_inventory")
    private List<Inventory> summaryOrderInventories;

    @SerializedName("order_inventory")
    private List<Inventory> inventoryOrder;

    @SerializedName("order_inventory_cancelation")
    private List<Inventory> inventoryCancelation;

    @SerializedName("order_inventory_progress")
    private List<Inventory> inventoryOnOrderProgress;

    public List<Inventory> getSummaryOrderInventories() {
        return summaryOrderInventories;
    }

    public void setSummaryOrderInventories(List<Inventory> summaryOrderInventories) {
        this.summaryOrderInventories = summaryOrderInventories;
    }

    public List<Inventory> getInventoryOrder() {
        return inventoryOrder;
    }

    public void setInventoryOrder(List<Inventory> inventoryOrder) {
        this.inventoryOrder = inventoryOrder;
    }

    public List<Inventory> getInventoryCancelation() {
        return inventoryCancelation;
    }

    public void setInventoryCancelation(List<Inventory> inventoryCancelation) {
        this.inventoryCancelation = inventoryCancelation;
    }

    public List<Inventory> getInventoryOnOrderProgress() {
        return inventoryOnOrderProgress;
    }

    public void setInventoryOnOrderProgress(List<Inventory> inventoryOnOrderProgress) {
        this.inventoryOnOrderProgress = inventoryOnOrderProgress;
    }

    public boolean isRoomCall() {
        return roomCall;
    }

    public void setRoomCall(boolean roomCall) {
        this.roomCall = roomCall;
    }

    public String getDpPaymentType() {
        return dpPaymentType;
    }

    public void setDpPaymentType(String dpPaymentType) {
        this.dpPaymentType = dpPaymentType;
    }

    public int getDpPaymentNominal() {
        return dpPaymentNominal;
    }

    public void setDpPaymentNominal(int dpPaymentNominal) {
        this.dpPaymentNominal = dpPaymentNominal;
    }

    public String getRoomIvc() {
        return roomIvc;
    }

    public void setRoomIvc(String roomIvc) {
        this.roomIvc = roomIvc;
    }

    public String getDpCardType() {
        return dpCardType;
    }

    public void setDpCardType(String dpCardType) {
        this.dpCardType = dpCardType;
    }

    public String getDpEdc() {
        return dpEdc;
    }

    public String getCodeDpEdc() {
        return codeDpEdc;
    }

    public void setCodeDpEdc(String codeDpEdc) {
        this.codeDpEdc = codeDpEdc;
    }

    public void setDpEdc(String dpEdc) {
        this.dpEdc = dpEdc;
    }

    public String getDpCardName() {
        return dpCardName;
    }

    public void setDpCardName(String dpCardName) {
        this.dpCardName = dpCardName;
    }

    public String getDpCardNumber() {
        return dpCardNumber;
    }

    public void setDpCardNumber(String dpCardNumber) {
        this.dpCardNumber = dpCardNumber;
    }

    public String getDpCardApprovalNumber() {
        return dpCardApprovalNumber;
    }

    public void setDpCardApprovalNumber(String dpCardApprovalNumber) {
        this.dpCardApprovalNumber = dpCardApprovalNumber;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherNominal() {
        return voucherNominal;
    }

    public void setVoucherNominal(String voucherNominal) {
        this.voucherNominal = voucherNominal;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgGuessPath() {
        return imgGuessPath;
    }

    public void setImgGuessPath(String imgGuessPath) {
        this.imgGuessPath = imgGuessPath;
    }

    public String getKeteranganStatusPromo() {
        return keteranganStatusPromo;
    }

    public void setKeteranganStatusPromo(String keteranganStatusPromo) {
        this.keteranganStatusPromo = keteranganStatusPromo;
    }

    public String getTransferInfo() {
        return transferInfo;
    }

    public void setTransferInfo(String transferInfo) {
        this.transferInfo = transferInfo;
    }

    public String getOverpaxVisitor() {
        return overpaxVisitor;
    }

    public void setOverpaxVisitor(String overpaxVisitor) {
        this.overpaxVisitor = overpaxVisitor;
    }

    public Date getTimeExtends() {
        return timeExtends;
    }

    public void setTimeExtends(Date timeExtends) {
        this.timeExtends = timeExtends;
    }

    public int getExtendHours() {
        return extendHours;
    }

    public void setExtendHours(int extendHours) {
        this.extendHours = extendHours;
    }

    public int getExtendMinutes() {
        return extendMinutes;
    }

    public void setExtendMinutes(int extendMinutes) {
        this.extendMinutes = extendMinutes;
    }

    public String getRoomGuessHp() {
        return roomGuessHp;
    }

    public void setRoomGuessHp(String roomGuessHp) {
        this.roomGuessHp = roomGuessHp;
    }

    public int getQm1() {
        return qm1;
    }

    public void setQm1(int qm1) {
        this.qm1 = qm1;
    }

    public int getQm2() {
        return qm2;
    }

    public void setQm2(int qm2) {
        this.qm2 = qm2;
    }

    public int getQm3() {
        return qm3;
    }

    public void setQm3(int qm3) {
        this.qm3 = qm3;
    }

    public int getQm4() {
        return qm4;
    }

    public void setQm4(int qm4) {
        this.qm4 = qm4;
    }

    public int getQf1() {
        return qf1;
    }

    public void setQf1(int qf1) {
        this.qf1 = qf1;
    }

    public int getQf2() {
        return qf2;
    }

    public void setQf2(int qf2) {
        this.qf2 = qf2;
    }

    public int getQf3() {
        return qf3;
    }

    public void setQf3(int qf3) {
        this.qf3 = qf3;
    }

    public int getQf4() {
        return qf4;
    }

    public void setQf4(int qf4) {
        this.qf4 = qf4;
    }

    public String getTotalVisitor() {
        return totalVisitor;
    }

    public void setTotalVisitor(String totalVisitor) {
        this.totalVisitor = totalVisitor;
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

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public int getRoomCallService() {
        return roomCallService;
    }

    public void setRoomCallService(int roomCallService) {
        this.roomCallService = roomCallService;
    }

    public boolean isRoomNotLobby() {
        return roomNotLobby;
    }

    public void setRoomNotLobby(boolean roomNotLobby) {
        this.roomNotLobby = roomNotLobby;
    }

    public String getRoomRcp() {
        return roomRcp;
    }

    public void setRoomRcp(String roomRcp) {
        this.roomRcp = roomRcp;
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

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getRoomIpAddress() {
        return roomIpAddress;
    }

    public void setRoomIpAddress(String roomIpAddress) {
        this.roomIpAddress = roomIpAddress;
    }

    public String getRoomChusr() {
        return roomChusr;
    }

    public void setRoomChusr(String roomChusr) {
        this.roomChusr = roomChusr;
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

    public void setRoomCheckinDuration(int roomCheckinDuration) {
        this.roomCheckinDuration = roomCheckinDuration;
    }

    public int getRoomResidualCheckinTime() {
        return roomResidualCheckinTime;
    }

    public void setRoomResidualCheckinTime(int roomResidualCheckinTime) {
        this.roomResidualCheckinTime = roomResidualCheckinTime;
    }

    public Date getRoomCheckinHours() {
        return roomCheckinHours;
    }

    public void setRoomCheckinHours(Date roomCheckinHours) {
        this.roomCheckinHours = roomCheckinHours;
    }

    public Date getRoomCheckoutHours() {
        return roomCheckoutHours;
    }

    public void setRoomCheckoutHours(Date roomCheckoutHours) {
        this.roomCheckoutHours = roomCheckoutHours;
    }

    public int getRoomResidualCheckinHoursTime() {
        return roomResidualCheckinHoursTime;
    }

    public void setRoomResidualCheckinHoursTime(int roomResidualCheckinHoursTime) {
        this.roomResidualCheckinHoursTime = roomResidualCheckinHoursTime;
    }

    public int getRoomResidualCheckinHoursMinutesTime() {
        return roomResidualCheckinHoursMinutesTime;
    }

    public void setRoomResidualCheckinHoursMinutesTime(int roomResidualCheckinHoursMinutesTime) {
        this.roomResidualCheckinHoursMinutesTime = roomResidualCheckinHoursMinutesTime;
    }

    public double getTotalAllInvoice() {
        return totalAllInvoice;
    }

    public void setTotalAllInvoice(double totalAllInvoice) {
        this.totalAllInvoice = totalAllInvoice;
    }

    public String getCodeMemberRef() {
        return codeMemberRef;
    }

    public void setCodeMemberRef(String codeMemberRef) {
        this.codeMemberRef = codeMemberRef;
    }

    /* @ToMany(referencedJoinProperty = "receptionRoom")
    private List<Order> orders;*/
}