package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import livs.code.frontoffice.helper.TimestampDbConverter;

@Entity(tableName = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = -6484897190481149103L;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "slip_order")
    @SerializedName("slip_order")
    private String slipOrder;

    @ColumnInfo(name = "rcp_code")
    @SerializedName("rcp_code")
    private String rcpCode;

    @ColumnInfo(name = "notif_type")
    @SerializedName("notif_type")
    private String notifType;

    @ColumnInfo(name = "room_type")
    @SerializedName("room_type")
    private String roomType;

    @ColumnInfo(name = "room_code")
    @SerializedName("room_code")
    private String roomCode;

    @ColumnInfo(name = "create_chusr")
    @SerializedName("create_chusr")
    private String createUser;

    @ColumnInfo(name = "accepted_chusr")
    @SerializedName("accepted_chusr")
    private String acceptedUser;

    @ColumnInfo(name = "is_read")
    @SerializedName("is_read")
    private boolean isRead;

    public int getId() {
        return id;
    }

    public String getRcpCode() {
        return rcpCode;
    }

    public void setRcpCode(String rcpCode) {
        this.rcpCode = rcpCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getSlipOrder() {
        return slipOrder;
    }

    public void setSlipOrder(String slipOrder) {
        this.slipOrder = slipOrder;
    }

    public String getNotifType() {
        return notifType;
    }

    public void setNotifType(String notifType) {
        this.notifType = notifType;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }


    public String getAcceptedUser() {
        return acceptedUser;
    }

    public void setAcceptedUser(String acceptedUser) {
        this.acceptedUser = acceptedUser;
    }
}
