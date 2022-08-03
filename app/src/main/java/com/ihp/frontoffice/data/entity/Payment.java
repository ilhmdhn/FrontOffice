package com.ihp.frontoffice.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "payment")
public class  Payment implements Serializable {

    private static final long serialVersionUID = -2872364484393044557L;

    @NonNull
    @ColumnInfo(name = "payment_type")
    @SerializedName("payment_type")
    private String paymentType;

    @NonNull
    @ColumnInfo(name = "nominal")
    @SerializedName("nominal")
    private double nominal;

    @ColumnInfo(name = "nama_user_debet")
    @SerializedName("nama_user_debet")
    private String namaUserDebet;

    @ColumnInfo(name = "card_code_debet")
    @SerializedName("card_code_debet")
    private String cardCodeDebet;

    @ColumnInfo(name = "approval_code_debet")
    @SerializedName("approval_code_debet")
    private String approvalCodeDebet;

    @ColumnInfo(name = "expired_date_debet")
    @SerializedName("expired_date_debet")
    private String expiredDateDebet;

    @ColumnInfo(name = "edc_debet")
    @SerializedName("edc_debet")
    private String edcDebet;

    @ColumnInfo(name = "edc_code")
    @SerializedName("edc_code")
    private String edcCode;

    @ColumnInfo(name = "type_edc")
    @SerializedName("type_edc")
    private TypeEdc typeEdc;

    @ColumnInfo(name = "card_debet")
    @SerializedName("card_debet")
    private String cardDebet;


    @ColumnInfo(name = "nama_user_credit")
    @SerializedName("nama_user_credit")
    private String namaUserCredit;

    @ColumnInfo(name = "card_code_credit")
    @SerializedName("card_code_credit")
    private String cardCodeCredit;

    @ColumnInfo(name = "approval_code_credit")
    @SerializedName("approval_code_credit")
    private String approvalCodeCredit;

    @ColumnInfo(name = "expired_date_credit")
    @SerializedName("expired_date_credit")
    private String expiredDateCredit;

    @ColumnInfo(name = "edc_credit")
    @SerializedName("edc_credit")
    private String edcCredit;

    @ColumnInfo(name = "card_credit")
    @SerializedName("card_credit")
    private String cardCredit;

    @ColumnInfo(name = "bank_name_tf")
    @SerializedName("bank_name_tf")
    private String bankNameTf;

    @ColumnInfo(name = "bank_account_name_tf")
    @SerializedName("bank_account_name_tf")
    private String bankAccountNameTf;

    @ColumnInfo(name = "type_emoney")
    @SerializedName("type_emoney")
    private String typeEmoney;

    @ColumnInfo(name = "nama_user_emoney")
    @SerializedName("nama_user_emoney")
    private String namaUserEmoney;

    @ColumnInfo(name = "account_emoney")
    @SerializedName("account_emoney")
    private String accountEmoney;

    @ColumnInfo(name = "ref_code_emoney")
    @SerializedName("ref_code_emoney")
    private String refCodeEmoney;

    @ColumnInfo(name = "nama_user_compliment")
    @SerializedName("nama_user_compliment")
    private String namaUserCompliment;

    @ColumnInfo(name = "instruksi_compliment")
    @SerializedName("instruksi_compliment")
    private String instruksiCompliment;

    @ColumnInfo(name = "instansi_compliment")
    @SerializedName("instansi_compliment")
    private String instansiCompliment;

    @ColumnInfo(name = "nama_user_piutang")
    @SerializedName("nama_user_piutang")
    private String namaUserPiutang;

    @ColumnInfo(name = "type_piutang")
    @SerializedName("type_piutang")
    private String typePiutang;

    @ColumnInfo(name = "id_member_piutang")
    @SerializedName("id_member_piutang")
    private String idMemberPiutang;

    @ColumnInfo(name = "id_payment")
    @SerializedName("id_payment")
    private String idPayment;

    @ColumnInfo(name = "edc_machine")
    @SerializedName("edc_machine")
    private String edcMachine;

    @ColumnInfo(name = "bank_type")
    @SerializedName("bank_type")
    private String bankType;

    @ColumnInfo(name = "bank_akun_name")
    @SerializedName("bank_akun_name")
    private String bankAkunName;

    @ColumnInfo(name = "bank_akun_number")
    @SerializedName("bank_akun_number")
    private String bankAkunNumber;

    @ColumnInfo(name = "bank_akun_approval")
    @SerializedName("bank_akun_approval")
    private String bankAkunApproval;

    @NonNull
    @ColumnInfo(name = "bayar")
    @SerializedName("bayar")
    private double bayar;

    @NonNull
    @ColumnInfo(name = "kembali")
    @SerializedName("kembali")
    private double kembali;

    @NonNull
    @ColumnInfo(name = "chusr")
    @SerializedName("Chusr")
    private String chusr;

    @NonNull
    public String getPaymentType() {
        return paymentType;
    }

    @NonNull
    public String getChusr() {
        return chusr;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }

    public void setPaymentType(@NonNull String paymentType) {
        this.paymentType = paymentType;
    }

    public String getBankNameTf() {
        return bankNameTf;
    }

    public String getBankAccountNameTf() {
        return bankAccountNameTf;
    }

    public String getEdcDebet() {
        return edcDebet;
    }

    public void setEdcDebet(String edcDebet) {
        this.edcDebet = edcDebet;
    }

    public String getCardDebet() {
        return cardDebet;
    }

    public void setCardDebet(String cardDebet) {
        this.cardDebet = cardDebet;
    }

    public String getEdcCredit() {
        return edcCredit;
    }

    public void setEdcCredit(String edcCredit) {
        this.edcCredit = edcCredit;
    }

    public String getCardCredit() {
        return cardCredit;
    }

    public void setCardCredit(String cardCredit) {
        this.cardCredit = cardCredit;
    }

    public String getNamaUserDebet() {
        return namaUserDebet;
    }

    public void setNamaUserDebet(String namaUserDebet) {
        this.namaUserDebet = namaUserDebet;
    }

    public String getCardCodeDebet() {
        return cardCodeDebet;
    }

    public void setCardCodeDebet(String cardCodeDebet) {
        this.cardCodeDebet = cardCodeDebet;
    }

    public String getApprovalCodeDebet() {
        return approvalCodeDebet;
    }

    public void setApprovalCodeDebet(String approvalCodeDebet) {
        this.approvalCodeDebet = approvalCodeDebet;
    }


    public String getNamaUserCredit() {
        return namaUserCredit;
    }

    public void setNamaUserCredit(String namaUserCredit) {
        this.namaUserCredit = namaUserCredit;
    }

    public String getCardCodeCredit() {
        return cardCodeCredit;
    }

    public void setCardCodeCredit(String cardCodeCredit) {
        this.cardCodeCredit = cardCodeCredit;
    }

    public String getApprovalCodeCredit() {
        return approvalCodeCredit;
    }

    public void setApprovalCodeCredit(String approvalCodeCredit) {
        this.approvalCodeCredit = approvalCodeCredit;
    }

    public String getTypeEmoney() {
        return typeEmoney;
    }

    public void setTypeEmoney(String typeEmoney) {
        this.typeEmoney = typeEmoney;
    }

    public String getNamaUserEmoney() {
        return namaUserEmoney;
    }

    public void setNamaUserEmoney(String namaUserEmoney) {
        this.namaUserEmoney = namaUserEmoney;
    }

    public String getAccountEmoney() {
        return accountEmoney;
    }

    public void setAccountEmoney(String accountEmoney) {
        this.accountEmoney = accountEmoney;
    }

    public String getRefCodeEmoney() {
        return refCodeEmoney;
    }

    public void setRefCodeEmoney(String refCodeEmoney) {
        this.refCodeEmoney = refCodeEmoney;
    }

    public String getNamaUserCompliment() {
        return namaUserCompliment;
    }

    public void setNamaUserCompliment(String namaUserCompliment) {
        this.namaUserCompliment = namaUserCompliment;
    }

    public String getInstruksiCompliment() {
        return instruksiCompliment;
    }

    public void setInstruksiCompliment(String instruksiCompliment) {
        this.instruksiCompliment = instruksiCompliment;
    }

    public String getInstansiCompliment() {
        return instansiCompliment;
    }

    public void setInstansiCompliment(String instansiCompliment) {
        this.instansiCompliment = instansiCompliment;
    }

    public String getNamaUserPiutang() {
        return namaUserPiutang;
    }

    public void setNamaUserPiutang(String namaUserPiutang) {
        this.namaUserPiutang = namaUserPiutang;
    }

    public String getTypePiutang() {
        return typePiutang;
    }

    public void setTypePiutang(String typePiutang) {
        this.typePiutang = typePiutang;
    }

    public String getIdMemberPiutang() {
        return idMemberPiutang;
    }

    public void setIdMemberPiutang(String idMemberPiutang) {
        this.idMemberPiutang = idMemberPiutang;
    }

    public String getBankType() {
        return bankType;
    }

    public String getBankAkunName() {
        return bankAkunName;
    }

    public String getBankAkunNumber() {
        return bankAkunNumber;
    }

    public String getBankAkunApproval() {
        return bankAkunApproval;
    }

    public double getBayar() {
        return bayar;
    }

    public double getKembali() {
        return kembali;
    }

    public void setKembali(double kembali) {
        this.kembali = kembali;
    }

    public TypeEdc getTypeEdc() {
        return typeEdc;
    }

    public void setTypeEdc(TypeEdc typeEdc) {
        this.typeEdc = typeEdc;
    }
}
