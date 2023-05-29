package com.ihp.frontoffice.helper;

public enum QRScanType {
    CHECKIN("CHECKIN"),
    RESERVASI("RESERVASI"),
    VOUCHER("VOUCHER"),

    ENTER_ROOM("ENTER_ROOM"),
    MEMBER_INFO("MEMBER");

    private final String type;

    QRScanType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}


