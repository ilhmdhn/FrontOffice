package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomType implements Serializable {

    private static final long serialVersionUID = 3417250766798987881L;

    @ColumnInfo(name = "room_type")
    @SerializedName("jenis_kamar")
    private String roomType;

    @ColumnInfo(name = "room_type_capacity")
    @SerializedName("kapasitas")
    private int roomTypeCapacity;

    @ColumnInfo(name = "room_type_not_lobby")
    @SerializedName("kamar_untuk_checkin")
    private boolean roomTypeNotLobby;

    @ColumnInfo(name = "available_room")
    @SerializedName("jumlah_available")
    private int availableRoom;

    @ColumnInfo(name = "reservasi_hour_duration")
    @SerializedName("reservasi_hour_duration")
    private int reservasiHourDuration;

    @ColumnInfo(name = "reservasi_checkin_time")
    @SerializedName("reservasi_checkin_time")
    private String reservasiCheckinTime;

    @ColumnInfo(name = "reservasi_checkout_time")
    @SerializedName("reservasi_checkout_time")
    private String reservasiCheckoutTime;


    @ColumnInfo(name = "reservasi_minute_duration")
    @SerializedName("reservasi_minute_duration")
    private int reservasiMinuteDuration;

    public String getReservasiCheckinTime() {
        return reservasiCheckinTime;
    }

    public String getReservasiCheckoutTime() {
        return reservasiCheckoutTime;
    }

    public void setReservasiCheckoutTime(String reservasiCheckoutTime) {
        this.reservasiCheckoutTime = reservasiCheckoutTime;
    }

    public int getReservasiHourDuration() {
        return reservasiHourDuration;
    }


    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getRoomTypeCapacity() {
        return roomTypeCapacity;
    }


    public int getAvailableRoom() {
        return availableRoom;
    }
}
