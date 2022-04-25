package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomPrice implements Serializable {

    private static final long serialVersionUID = 7185581733532932535L;

    @ColumnInfo(name = "invoice")
    @SerializedName("invoice")
    public String invoice;

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
