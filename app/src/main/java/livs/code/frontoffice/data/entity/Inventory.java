package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by program on 24/10/2017.
 */

@Entity(tableName = "inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = -3694974175227292497L;


    @ColumnInfo(name = "slip_order")
    @SerializedName("slip_order")
    public String slipOrder;

    @ColumnInfo(name = "code_inventory")
    @SerializedName("inventory")
    public String inventoryCode;

    @ColumnInfo(name = "name_inventory")
    @SerializedName("nama")
    public String inventoryName;

    @ColumnInfo(name = "qty")
    @SerializedName("qty")
    public int qty;

    @ColumnInfo(name = "unfinished_accept_order_qty")
    @SerializedName("order_qty_belum_terkirim")
    public int unfinishedAcceptOrderQty;

    @ColumnInfo(name = "inventory_state")
    @SerializedName("order_status")
    public int inventoryState;

    @ColumnInfo(name = "location_code")
    @SerializedName("location")
    public int locationCode;


    @ColumnInfo(name = "printed")
    @SerializedName("printed")
    public boolean printed;

    @ColumnInfo(name = "note")
    @SerializedName("note")
    public String note;


    @ColumnInfo(name = "order_cancelation")
    @SerializedName("order_cancelation")
    public String orderCancelation;

    @ColumnInfo(name = "qty_belum_ocd")
    @SerializedName("qty_belum_ocd")
    public int qtyBeforeOcd;

    @ColumnInfo(name = "order_penjualan")
    @SerializedName("order_penjualan")
    public String orderPenjualan;

    @ColumnInfo(name = "qty_okd")
    @SerializedName("qty_okd")
    public int qtyOkd;

    @ColumnInfo(name = "unit_price_inventory")
    @SerializedName("price")
    public double unitPrice;

    @ColumnInfo(name = "unit_discount_inventory")
    @SerializedName("diskon")
    public double unitDiscount;

    @ColumnInfo(name = "total_after_discount")
    @SerializedName("total_setelah_diskon")
    public double totalAfterDiscount;

    @ColumnInfo(name = "total_discount")
    @SerializedName("total_diskon")
    public double totalDiscount;

    @ColumnInfo(name = "kamar")
    @SerializedName("kamar")
    public String kamar;

    @ColumnInfo(name = "invoice")
    @SerializedName("invoice")
    public String invoice;

    public int getUnfinishedAcceptOrderQty() {
        return unfinishedAcceptOrderQty;
    }

    public int getInventoryState() {
        return inventoryState;
    }

    public String getOrderPenjualan() {
        return orderPenjualan;
    }

    public String getSlipOrder() {
        return slipOrder;
    }

    public String getOrderCancelation() {
        return orderCancelation;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getLocationCode() {
        return locationCode;
    }

    public boolean isPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getUnitDiscount() {
        return unitDiscount;
    }

    public double getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public String getKamar() {
        return kamar;
    }

    public void setKamar(String kamar) {
        this.kamar = kamar;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public int getQtyBeforeOcd() {
        return qtyBeforeOcd;
    }

    public int getQtyOkd() {
        return qtyOkd;
    }
}
