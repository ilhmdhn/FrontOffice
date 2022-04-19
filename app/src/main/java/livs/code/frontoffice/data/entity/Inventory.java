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

    @ColumnInfo(name = "inv_id_global")
    @SerializedName("InventoryID_Global")
    private String invIdGlobal;

    @ColumnInfo(name = "name_inventory")
    @SerializedName("nama")
    public String inventoryName;

    @ColumnInfo(name = "qty")
    @SerializedName("qty")
    public int qty;

    @ColumnInfo(name = "accepted_qty")
    @SerializedName("qty_terima")
    public int acceptedQty;

    @ColumnInfo(name = "unfinished_accept_order_qty")
    @SerializedName("order_qty_belum_terkirim")
    public int unfinishedAcceptOrderQty;

    @ColumnInfo(name = "accepted_order_date")
    @SerializedName("tanggal_terima")
    public Date acceptedOrderDate;

    @ColumnInfo(name = "completed_order_date")
    @SerializedName("tanggal_kirim")
    public Date completedOrderDate;

    @ColumnInfo(name = "inventory_state")
    @SerializedName("order_status")
    public int inventoryState;

    @ColumnInfo(name = "inventory_state_desc")
    @SerializedName("keterangan_status_slip_order")
    public String inventoryStateDesc;

    @ColumnInfo(name = "location_code")
    @SerializedName("location")
    public int locationCode;

    @ColumnInfo(name = "inv_group")
    @SerializedName("group")
    private int invGroup;

    @ColumnInfo(name = "inv_service")
    @SerializedName("service")
    private int invService;

    @ColumnInfo(name = "inv_pajak")
    @SerializedName("pajak")
    private int invPajak;

    @ColumnInfo(name = "inv_point")
    @SerializedName("point")
    private int invPoint;

    @ColumnInfo(name = "inv_reward")
    @SerializedName("reward")
    private int invReward;

    @ColumnInfo(name = "inv_shrct")
    @SerializedName("str_name")
    private String invShrct;

    @ColumnInfo(name = "inv_service_persen_food")
    @SerializedName("service_Persen_Food")
    private int invServicePersenFood;

    @ColumnInfo(name = "inv_service_rp_food")
    @SerializedName("service_Rp_Food")
    private int invServiceRpFood;

    @ColumnInfo(name = "inv_tax_persen_food")
    @SerializedName("tax_persen_food")
    private int invTaxPersenFood;

    @ColumnInfo(name = "inv_tax_rp_food")
    @SerializedName("tax_rp_food")
    private int invTaxRpFood;

    @ColumnInfo(name = "inv_nilai_service")
    @SerializedName("nilai_service")
    private Double invNilaiService;

    @ColumnInfo(name = "inv_nilai_pajak")
    @SerializedName("nilai_pajak")
    private Double invNilaiPajak;

    @ColumnInfo(name = "inv_price_include_service_pajak")
    @SerializedName("price_include_service_pajak")
    private Double invPriceIncludeServicePajak;

    @ColumnInfo(name = "location_desc")
    @SerializedName("keterangan_location")
    public String descLocation;

    @ColumnInfo(name = "printed")
    @SerializedName("printed")
    public boolean printed;

    @ColumnInfo(name = "note")
    @SerializedName("note")
    public String note;

    @ColumnInfo(name = "id_cooker")
    @SerializedName("id_cooker")
    public String idCooker;

    @ColumnInfo(name = "order_cancelation")
    @SerializedName("order_cancelation")
    public String orderCancelation;

    @ColumnInfo(name = "qty_ocd")
    @SerializedName("qty_ocd")
    public int qtyOcd;

    @ColumnInfo(name = "qty_belum_ocd")
    @SerializedName("qty_belum_ocd")
    public int qtyBeforeOcd;

    @ColumnInfo(name = "order_penjualan")
    @SerializedName("order_penjualan")
    public String orderPenjualan;

    @ColumnInfo(name = "qty_okd")
    @SerializedName("qty_okd")
    public int qtyOkd;

    @ColumnInfo(name = "qty_belum_okd")
    @SerializedName("qty_belum_okd")
    public int qtyBeforeOkd;

    @ColumnInfo(name = "promo_food")
    @SerializedName("promo_food")
    public String promoFood;

    @ColumnInfo(name = "unit_price_inventory")
    @SerializedName("price")
    public double unitPrice;

    @ColumnInfo(name = "unit_discount_inventory")
    @SerializedName("diskon")
    public double unitDiscount;

    @ColumnInfo(name = "price_after_discount")
    @SerializedName("harga_per_item_setelah_diskon")
    public double priceAfterDiscount;

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

    @ColumnInfo(name = "invoice_transfer")
    @SerializedName("invoice_transfer")
    public String invoiceTransfer;

    @ColumnInfo(name = "not_room_transfer")
    @SerializedName("not_room_transfer")
    public String notRoomTransfer;

    public int getAcceptedQty() {
        return acceptedQty;
    }

    public void setAcceptedQty(int acceptedQty) {
        this.acceptedQty = acceptedQty;
    }

    public Date getAcceptedOrderDate() {
        return acceptedOrderDate;
    }

    public void setAcceptedOrderDate(Date acceptedOrderDate) {
        this.acceptedOrderDate = acceptedOrderDate;
    }

    public int getUnfinishedAcceptOrderQty() {
        return unfinishedAcceptOrderQty;
    }

    public void setUnfinishedAcceptOrderQty(int unfinishedAcceptOrderQty) {
        this.unfinishedAcceptOrderQty = unfinishedAcceptOrderQty;
    }

    public Date getCompletedOrderDate() {
        return completedOrderDate;
    }

    public void setCompletedOrderDate(Date completedOrderDate) {
        this.completedOrderDate = completedOrderDate;
    }

    public int getInventoryState() {
        return inventoryState;
    }

    public void setInventoryState(int inventoryState) {
        this.inventoryState = inventoryState;
    }

    public String getInventoryStateDesc() {
        return inventoryStateDesc;
    }

    public void setInventoryStateDesc(String inventoryStateDesc) {
        this.inventoryStateDesc = inventoryStateDesc;
    }

    public String getOrderPenjualan() {
        return orderPenjualan;
    }

    public void setOrderPenjualan(String orderPenjualan) {
        this.orderPenjualan = orderPenjualan;
    }

    public String getSlipOrder() {
        return slipOrder;
    }

    public void setSlipOrder(String slipOrder) {
        this.slipOrder = slipOrder;
    }

    public String getOrderCancelation() {
        return orderCancelation;
    }

    public void setOrderCancelation(String orderCancelation) {
        this.orderCancelation = orderCancelation;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
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

    public void setLocationCode(int locationCode) {
        this.locationCode = locationCode;
    }

    public String getDescLocation() {
        return descLocation;
    }

    public void setDescLocation(String descLocation) {
        this.descLocation = descLocation;
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

    public String getIdCooker() {
        return idCooker;
    }

    public void setIdCooker(String idCooker) {
        this.idCooker = idCooker;
    }

    public String getPromoFood() {
        return promoFood;
    }

    public void setPromoFood(String promoFood) {
        this.promoFood = promoFood;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnitDiscount() {
        return unitDiscount;
    }

    public void setUnitDiscount(double unitDiscount) {
        this.unitDiscount = unitDiscount;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public double getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public void setTotalAfterDiscount(double totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
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

    public int getQtyOcd() {
        return qtyOcd;
    }

    public void setQtyOcd(int qtyOcd) {
        this.qtyOcd = qtyOcd;
    }

    public int getQtyBeforeOcd() {
        return qtyBeforeOcd;
    }

    public void setQtyBeforeOcd(int qtyBeforeOcd) {
        this.qtyBeforeOcd = qtyBeforeOcd;
    }

    public int getQtyOkd() {
        return qtyOkd;
    }

    public void setQtyOkd(int qtyOkd) {
        this.qtyOkd = qtyOkd;
    }

    public int getQtyBeforeOkd() {
        return qtyBeforeOkd;
    }

    public void setQtyBeforeOkd(int qtyBeforeOkd) {
        this.qtyBeforeOkd = qtyBeforeOkd;
    }
}
