package com.ihp.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Time  implements Serializable {
    @ColumnInfo(name = "checkin")
    @SerializedName("checkin")
    private String checkin;

    @ColumnInfo(name = "checkout")
    @SerializedName("checkout")
    private String checkout;

    @ColumnInfo(name = "durasi")
    @SerializedName("durasi")
    private String durasi;

    public String getCheckinTime() {
        return checkin;
    }

    public String getCheckoutTime() {
        return checkout;
    }

    public String getTimeClock() {
        return durasi;
    }
}
