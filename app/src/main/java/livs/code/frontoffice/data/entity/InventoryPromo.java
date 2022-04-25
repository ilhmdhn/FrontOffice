package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InventoryPromo implements Serializable {


    private static final long serialVersionUID = 3785099944394100391L;

    @ColumnInfo(name = "food_promo_name")
    @SerializedName("promo_food")
    private String foodPromoName;

    @ColumnInfo(name = "discount_percent")
    @SerializedName("diskon_persen")
    private int discountPercent;

    @ColumnInfo(name = "discount_rupiah")
    @SerializedName("diskon_rp")
    private String discountRupiah;

    @ColumnInfo(name = "notes")
    @SerializedName("Khusus1")
    private String notes;

    @ColumnInfo(name = "time_start")
    @SerializedName("time_start")
    private String timeStart;

    @ColumnInfo(name = "time_end")
    @SerializedName("time_finish")
    private String timeEnd;

    public String getFoodPromoName() {
        return foodPromoName;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public String getDiscountRupiah() {
        return discountRupiah;
    }

    public String getNotes() {
        return notes;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }
}
