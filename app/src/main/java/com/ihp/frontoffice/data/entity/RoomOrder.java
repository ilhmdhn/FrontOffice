package com.ihp.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RoomOrder implements Serializable {

    private static final long serialVersionUID = -8357365571490301709L;

    @ColumnInfo(name = "room")
    @SerializedName("room")
    private String roomCode;

    @ColumnInfo(name = "kode_rcp")
    @SerializedName("kode_rcp")
    private String kodeRcp;

    @ColumnInfo(name = "hp")
    @SerializedName("hp")
    private String hp;

    @ColumnInfo(name = "durasi_jam")
    @SerializedName("durasi_jam")
    private int durasiJam;

    @ColumnInfo(name = "durasi_menit")
    @SerializedName("durasi_menit")
    private int durasiMenit;

    @ColumnInfo(name = "chusr")
    @SerializedName("chusr")
    private String chuser;

    @ColumnInfo(name = "transfer_reason")
    @SerializedName("transfer_reason")
    private String transferReason;


    @ColumnInfo(name = "is_send_email_invoice")
    @SerializedName("is_send_email_invoice")
    private boolean isSendEmailInvoice;

   /* @ColumnInfo(name = "sign_path")
    @SerializedName("sign_path")
    private String signPath;
*/
    @ColumnInfo(name = "visitor")
    @SerializedName("visitor")
    private Member member;

    @ColumnInfo(name = "checkin_room_type")
    @SerializedName("checkin_room_type")
    private RoomType checkinRoomType;

    @ColumnInfo(name = "checkin_room")
    @SerializedName("checkin_room")
    private Room checkinRoom;

    @ColumnInfo(name = "old_room_before_transfer")
    @SerializedName("old_room_before_transfer")
    private Room oldRoomBeforeTransfer;

    @ColumnInfo(name = "invoice")
    @SerializedName("invoice")
    private Invoice invoice;

    @ColumnInfo(name = "time")
    @SerializedName("time")
    private Time time;

    @SerializedName("order_room")
    private List<Room> historyTransferOrderRoom;

    @SerializedName("order_room_extend")
    private List<Room> orderRoomExtends;

    @SerializedName("order_room_price")
    private List<RoomPrice> orderRoomPrices;

    @SerializedName("summary_order_inventory")
    private List<Inventory> summaryOrderInventories;

    @SerializedName("order_inventory")
    private List<Inventory> inventories;

    @SerializedName("order_inventory_cancelation")
    private List<Inventory> inventoryCancelation;

    @SerializedName("order_inventory_progress")
    private List<Inventory> inventoryOnOrderProgress;

    @SerializedName("order_promo_food")
    private List<InventoryPromo> inventoryPromos;

    @SerializedName("order_promo_room")
    private List<RoomPromo> roomPromos;

    @SerializedName("order_room_payment")
    private List<Payment> orderRoomPayments;

    public RoomType getCheckinRoomType() {
        return checkinRoomType;
    }

    public void setCheckinRoomType(RoomType checkinRoomType) {
        this.checkinRoomType = checkinRoomType;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Time getTime(){
        return time;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void setTime(Time timeRcp) {
        this.time = timeRcp;
    }

    public Room getCheckinRoom() {
        return checkinRoom;
    }

    public void setCheckinRoom(Room checkinRoom) {
        this.checkinRoom = checkinRoom;
    }

    public List<Room> getHistoryTransferOrderRoom() {
        return historyTransferOrderRoom;
    }

    public boolean isSendEmailInvoice() {
        return isSendEmailInvoice;
    }

    public void setSendEmailInvoice(boolean sendEmailInvoice) {
        isSendEmailInvoice = sendEmailInvoice;
    }

    public List<Room> getOrderRoomExtends() {
        return orderRoomExtends;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public List<Inventory> getInventoryCancelation() {
        return inventoryCancelation;
    }

    public List<Inventory> getInventoryOnOrderProgress() {
        return inventoryOnOrderProgress;
    }

    public List<InventoryPromo> getInventoryPromos() {
        return inventoryPromos;
    }

    public List<RoomPromo> getRoomPromos() {
        return roomPromos;
    }

    public void setChuser(String chuser) {
        this.chuser = chuser;
    }

    public List<Payment> getOrderRoomPayments() {
        return orderRoomPayments;
    }

    public void setOrderRoomPayments(List<Payment> orderRoomPayments) {
        this.orderRoomPayments = orderRoomPayments;
    }

    public String getHp() {
        return hp;
    }

    public void setDurasiJam(int durasiJam) {
        this.durasiJam = durasiJam;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getKodeRcp() {
        return kodeRcp;
    }

    public void setKodeRcp(String kodeRcp) {
        this.kodeRcp = kodeRcp;
    }

    public String getTransferReason() {
        return transferReason;
    }

    public void setTransferReason(String transferReason) {
        this.transferReason = transferReason;
    }

    public Room getOldRoomBeforeTransfer() {
        return oldRoomBeforeTransfer;
    }

    public void setOldRoomBeforeTransfer(Room oldRoomBeforeTransfer) {
        this.oldRoomBeforeTransfer = oldRoomBeforeTransfer;
    }

    public List<Inventory> getSummaryOrderInventories() {
        return summaryOrderInventories;
    }

}
