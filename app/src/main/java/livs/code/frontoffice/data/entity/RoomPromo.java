package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomPromo implements Serializable {


    private static final long serialVersionUID = 754231604118750106L;

    @ColumnInfo(name = "room_promo_name")
    @SerializedName("promo_room")
    private String roomPromoName;

    @ColumnInfo(name = "discount_percent")
    @SerializedName("diskon_persen")
    private int discountPercent;

    @ColumnInfo(name = "discount_rupiah")
    @SerializedName("diskon_rp")
    private String discountRupiah;

    @ColumnInfo(name = "time_start")
    @SerializedName("time_start")
    private String timeStart;

    @ColumnInfo(name = "time_end")
    @SerializedName("time_finish")
    private String timeEnd;

    @ColumnInfo(name = "notes")
    @SerializedName("keterangan_khusus")
    private String notes;


    public String getRoomPromoName() {
        return roomPromoName;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public String getDiscountRupiah() {
        return discountRupiah;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getNotes() {
        return notes;
    }

}
