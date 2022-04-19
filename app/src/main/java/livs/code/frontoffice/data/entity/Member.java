package livs.code.frontoffice.data.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Member implements Serializable {

    private static final long serialVersionUID = 3539599101027924081L;

    @ColumnInfo(name = "member_code")
    @SerializedName("member")
    private String memberCode;

    @ColumnInfo(name = "expire_date")
    @SerializedName("expire_date")
    private String expireDate;

    @ColumnInfo(name = "full_name")
    @SerializedName("nama_lengkap")
    private String fullName;

    @ColumnInfo(name = "alamat")
    @SerializedName("alamat")
    private String alamat;

    @ColumnInfo(name = "kota")
    @SerializedName("kota")
    private String kota;

    @ColumnInfo(name = "fax")
    @SerializedName("fax")
    private String fax;

    @ColumnInfo(name = "hp")
    @SerializedName("hp")
    private String hp;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    private String email;

    @ColumnInfo(name = "birthday")
    @SerializedName("birthday")
    private String birthday;

    @ColumnInfo(name = "diskon_room")
    @SerializedName("diskon_room")
    private String diskonRoom;

    @ColumnInfo(name = "diskon_food")
    @SerializedName("diskon_food")
    private String diskonFood;

    @ColumnInfo(name = "point_reward")
    @SerializedName("point_reward")
    private int pointReward;

    @ColumnInfo(name = "jenis_member")
    @SerializedName("jenis_member")
    private String jenisMember;

    @ColumnInfo(name = "foto_path_node")
    @SerializedName("photo_url_lokal")
    private String fotoPathNode;

    @ColumnInfo(name = "foto_path_php")
    @SerializedName("photo_url")
    private String fotoPathPhp;

    @ColumnInfo(name = "code_tipe_member")
    @SerializedName("code_tipe_member")
    private int codeTypeMember;

    @ColumnInfo(name = "sex")
    @SerializedName("sex")
    private String sex;



    @ColumnInfo(name = "member_reservasi_code")
    @SerializedName("member_reservasi_code")
    private String memberReservasiCode;

    @ColumnInfo(name = "pengali_poin")
    @SerializedName("pengali_poin")
    private int pengaliPoin;

    @ColumnInfo(name = "reservasi")
    @SerializedName("reservasi")
    private RoomType reservasiRoomType;

    @ColumnInfo(name = "payment_reservasi")
    @SerializedName("payment_reservasi")
    private Payment reservasiPayment;

    public Payment getReservasiPayment() {
        return reservasiPayment;
    }

    public void setReservasiPayment(Payment reservasiPayment) {
        this.reservasiPayment = reservasiPayment;
    }

    public RoomType getReservasiRoomType() {
        return reservasiRoomType;
    }

    public void setReservasiRoomType(RoomType reservasiRoomType) {
        this.reservasiRoomType = reservasiRoomType;
    }

    public String getFotoPathNode() {
        return fotoPathNode;
    }

    public void setFotoPathNode(String fotoPathNode) {
        this.fotoPathNode = fotoPathNode;
    }

    public String getFotoPathPhp() {
        return fotoPathPhp;
    }

    public void setFotoPathPhp(String fotoPathPhp) {
        this.fotoPathPhp = fotoPathPhp;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getMemberReservasiCode() {
        return memberReservasiCode;
    }

    public void setMemberReservasiCode(String memberReservasiCode) {
        this.memberReservasiCode = memberReservasiCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDiskonRoom() {
        return diskonRoom;
    }

    public void setDiskonRoom(String diskonRoom) {
        this.diskonRoom = diskonRoom;
    }

    public String getDiskonFood() {
        return diskonFood;
    }

    public void setDiskonFood(String diskonFood) {
        this.diskonFood = diskonFood;
    }

    public int getPointReward() {
        return pointReward;
    }

    public void setPointReward(int pointReward) {
        this.pointReward = pointReward;
    }

    public String getJenisMember() {
        return jenisMember;
    }

    public void setJenisMember(String jenisMember) {
        this.jenisMember = jenisMember;
    }

    public int getCodeTypeMember() {
        return codeTypeMember;
    }

    public void setCodeTypeMember(int codeTypeMember) {
        this.codeTypeMember = codeTypeMember;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPengaliPoin() {
        return pengaliPoin;
    }

    public void setPengaliPoin(int pengaliPoin) {
        this.pengaliPoin = pengaliPoin;
    }
}