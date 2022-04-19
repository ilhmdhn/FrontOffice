package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "config")
public class Config implements Serializable {

    private static final long serialVersionUID = -7500248145897785647L;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    int id;

    @ColumnInfo(name = "server_port")
    @SerializedName("server_port")
    private String serverPort;

    @ColumnInfo(name = "server_ip")
    @SerializedName("server_ip")
    private String serverIp;

    @ColumnInfo(name = "base_url")
    @SerializedName("base_url")
    private String baseURL;

    public Config(String serverIp, String serverPort, String baseURL) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.baseURL = baseURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
}
