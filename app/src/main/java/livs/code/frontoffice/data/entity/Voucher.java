package livs.code.frontoffice.data.entity;


import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Voucher{
    @ColumnInfo(name = "outlet")
    @SerializedName("outlet")
    private String outlet;
    @ColumnInfo(name = "voucher")
    @SerializedName("voucher")
    private String voucher;
    @ColumnInfo(name = "date_")
    @SerializedName("date_")
    private Date date_;
    @ColumnInfo(name = "date")
    @SerializedName("date")
    private String date;
    @ColumnInfo(name = "active_date")
    @SerializedName("active_date")
    private String active_date;
    @ColumnInfo(name = "expired_")
    @SerializedName("expired_")
    private Date expired_;
    @ColumnInfo(name = "expired")
    @SerializedName("expired")
    private String expired;
    @ColumnInfo(name = "expired_date")
    @SerializedName("expired_date")
    private String expired_date;
    @ColumnInfo(name = "jenis_kamar")
    @SerializedName("jenis_kamar")
    private String jenis_kamar;
    @ColumnInfo(name = "jam_free")
    @SerializedName("jam_free")
    private int jam_free;
    @ColumnInfo(name = "menit_free")
    @SerializedName("menit_free")
    private int menit_free;

    @ColumnInfo(name = "date_start")
    @SerializedName("date_start")
    private int date_start;
    @ColumnInfo(name = "time_start")
    @SerializedName("time_start")
    private String time_start;

    @ColumnInfo(name = "date_finish")
    @SerializedName("date_finish")
    private int date_finish;

    @ColumnInfo(name = "time_finish")
    @SerializedName("time_finish")
    private String time_finish;

    @ColumnInfo(name = "nilai")
    @SerializedName("nilai")
    private int nilai;

    @ColumnInfo(name = "jenis_voucher")
    @SerializedName("jenis_voucher")
    private int jenis_voucher;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    private int status;

    @ColumnInfo(name = "reception")
    @SerializedName("reception")
    private String reception;

    @ColumnInfo(name = "member")
    @SerializedName("member")
    public String member;

    @ColumnInfo(name = "member_type")
    @SerializedName("member_type")
    private int member_type;

    @ColumnInfo(name = "name_")
    @SerializedName("name_")
    private String name_;

    @ColumnInfo(name = "category")
    @SerializedName("category")
    private int category;

    @ColumnInfo(name = "group_")
    @SerializedName("group_")
    private int group_;

    @ColumnInfo(name = "condition_day")
    @SerializedName("condition_day")
    private int condition_day;

    @ColumnInfo(name = "condition_tax_service")
    @SerializedName("condition_tax_service")
    private int condition_tax_service;

    @ColumnInfo(name = "condition_room_type_over")
    @SerializedName("condition_room_type_over")
    private int condition_room_type_over;

    @ColumnInfo(name = "condition_hour")
    @SerializedName("condition_hour")
    private int condition_hour;

    @ColumnInfo(name = "room_price")
    @SerializedName("room_price")
    private int room_price;

    @ColumnInfo(name = "condition_room_price")
    @SerializedName("condition_room_price")
    private int condition_room_price;

    @ColumnInfo(name = "room_discount")
    @SerializedName("room_discount")
    private int room_discount;

    @ColumnInfo(name = "condition_room_discount")
    @SerializedName("condition_room_discount")
    private int condition_room_discount;

    @ColumnInfo(name = "item")
    @SerializedName("item")
    private String item;

    @ColumnInfo(name = "qty")
    @SerializedName("qty")
    private int qty;

    @ColumnInfo(name = "condition_item")
    @SerializedName("condition_item")
    private String condition_item;

    @ColumnInfo(name = "condition_item_qty")
    @SerializedName("condition_item_qty")
    private int condition_item_qty;

    @ColumnInfo(name = "condition_item_price")
    @SerializedName("condition_item_price")
    private int condition_item_price;

    @ColumnInfo(name = "fnb_rice")
    @SerializedName("fnb_rice")
    private int fnb_rice;

    @ColumnInfo(name = "condition_fnb_price")
    @SerializedName("condition_fnb_price")
    private int condition_fnb_price;

    @ColumnInfo(name = "fnb_discount")
    @SerializedName("fnb_discount")
    private int fnb_discount;

    @ColumnInfo(name = "condition_fnb_discount")
    @SerializedName("condition_fnb_discount")
    private int condition_fnb_discount;

    @ColumnInfo(name = "condition_price")
    @SerializedName("condition_price")
    private int condition_price;

    @ColumnInfo(name = "discount")
    @SerializedName("discount")
    private int discount;

    @ColumnInfo(name = "condition_discount")
    @SerializedName("condition_discount")
    private int condition_discount;

    @ColumnInfo(name = "reedem_poin")
    @SerializedName("reedem_poin")
    private int reedem_poin;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "description_")
    @SerializedName("description_")
    private String description_;

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJenis_kamar() {
        return jenis_kamar;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
