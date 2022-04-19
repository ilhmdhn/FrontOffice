package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TypeEdc implements Serializable {

    private static final long serialVersionUID = 8406439611880497147L;

    @ColumnInfo(name = "edc_code")
    @SerializedName("nomor_edc")
    private String edcCode;

    @ColumnInfo(name = "edc_name")
    @SerializedName("nama_mesin")
    private String edcName;

    @ColumnInfo(name = "status")
    @SerializedName("status_aktif")
    private String status;


    public String getEdcCode() {
        return edcCode;
    }

    public void setEdcCode(String edcCode) {
        this.edcCode = edcCode;
    }

    public String getEdcName() {
        return edcName;
    }

    public void setEdcName(String edcName) {
        this.edcName = edcName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
