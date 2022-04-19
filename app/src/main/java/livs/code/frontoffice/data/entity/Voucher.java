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

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Date getDate_() {
        return date_;
    }

    public void setDate_(Date date_) {
        this.date_ = date_;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActive_date() {
        return active_date;
    }

    public void setActive_date(String active_date) {
        this.active_date = active_date;
    }

    public Date getExpired_() {
        return expired_;
    }

    public void setExpired_(Date expired_) {
        this.expired_ = expired_;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }

    public String getJenis_kamar() {
        return jenis_kamar;
    }

    public void setJenis_kamar(String jenis_kamar) {
        this.jenis_kamar = jenis_kamar;
    }

    public int getJam_free() {
        return jam_free;
    }

    public void setJam_free(int jam_free) {
        this.jam_free = jam_free;
    }

    public int getMenit_free() {
        return menit_free;
    }

    public void setMenit_free(int menit_free) {
        this.menit_free = menit_free;
    }

    public int getDate_start() {
        return date_start;
    }

    public void setDate_start(int date_start) {
        this.date_start = date_start;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public int getDate_finish() {
        return date_finish;
    }

    public void setDate_finish(int date_finish) {
        this.date_finish = date_finish;
    }

    public String getTime_finish() {
        return time_finish;
    }

    public void setTime_finish(String time_finish) {
        this.time_finish = time_finish;
    }

    public int getNilai() {
        return nilai;
    }

    public void setNilai(int nilai) {
        this.nilai = nilai;
    }

    public int getJenis_voucher() {
        return jenis_voucher;
    }

    public void setJenis_voucher(int jenis_voucher) {
        this.jenis_voucher = jenis_voucher;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReception() {
        return reception;
    }

    public void setReception(String reception) {
        this.reception = reception;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getMember_type() {
        return member_type;
    }

    public void setMember_type(int member_type) {
        this.member_type = member_type;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getGroup_() {
        return group_;
    }

    public void setGroup_(int group_) {
        this.group_ = group_;
    }

    public int getCondition_day() {
        return condition_day;
    }

    public void setCondition_day(int condition_day) {
        this.condition_day = condition_day;
    }

    public int getCondition_tax_service() {
        return condition_tax_service;
    }

    public void setCondition_tax_service(int condition_tax_service) {
        this.condition_tax_service = condition_tax_service;
    }

    public int getCondition_room_type_over() {
        return condition_room_type_over;
    }

    public void setCondition_room_type_over(int condition_room_type_over) {
        this.condition_room_type_over = condition_room_type_over;
    }

    public int getCondition_hour() {
        return condition_hour;
    }

    public void setCondition_hour(int condition_hour) {
        this.condition_hour = condition_hour;
    }

    public int getRoom_price() {
        return room_price;
    }

    public void setRoom_price(int room_price) {
        this.room_price = room_price;
    }

    public int getCondition_room_price() {
        return condition_room_price;
    }

    public void setCondition_room_price(int condition_room_price) {
        this.condition_room_price = condition_room_price;
    }

    public int getRoom_discount() {
        return room_discount;
    }

    public void setRoom_discount(int room_discount) {
        this.room_discount = room_discount;
    }

    public int getCondition_room_discount() {
        return condition_room_discount;
    }

    public void setCondition_room_discount(int condition_room_discount) {
        this.condition_room_discount = condition_room_discount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getCondition_item() {
        return condition_item;
    }

    public void setCondition_item(String condition_item) {
        this.condition_item = condition_item;
    }

    public int getCondition_item_qty() {
        return condition_item_qty;
    }

    public void setCondition_item_qty(int condition_item_qty) {
        this.condition_item_qty = condition_item_qty;
    }

    public int getCondition_item_price() {
        return condition_item_price;
    }

    public void setCondition_item_price(int condition_item_price) {
        this.condition_item_price = condition_item_price;
    }

    public int getFnb_rice() {
        return fnb_rice;
    }

    public void setFnb_rice(int fnb_rice) {
        this.fnb_rice = fnb_rice;
    }

    public int getCondition_fnb_price() {
        return condition_fnb_price;
    }

    public void setCondition_fnb_price(int condition_fnb_price) {
        this.condition_fnb_price = condition_fnb_price;
    }

    public int getFnb_discount() {
        return fnb_discount;
    }

    public void setFnb_discount(int fnb_discount) {
        this.fnb_discount = fnb_discount;
    }

    public int getCondition_fnb_discount() {
        return condition_fnb_discount;
    }

    public void setCondition_fnb_discount(int condition_fnb_discount) {
        this.condition_fnb_discount = condition_fnb_discount;
    }

    public int getCondition_price() {
        return condition_price;
    }

    public void setCondition_price(int condition_price) {
        this.condition_price = condition_price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getCondition_discount() {
        return condition_discount;
    }

    public void setCondition_discount(int condition_discount) {
        this.condition_discount = condition_discount;
    }

    public int getReedem_poin() {
        return reedem_poin;
    }

    public void setReedem_poin(int reedem_poin) {
        this.reedem_poin = reedem_poin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_() {
        return description_;
    }

    public void setDescription_(String description_) {
        this.description_ = description_;
    }
}
