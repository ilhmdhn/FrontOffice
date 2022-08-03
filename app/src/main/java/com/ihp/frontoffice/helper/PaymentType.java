package com.ihp.frontoffice.helper;

public enum PaymentType {
    CASH("CASH"), //AVAILABLE TO CHECKIN
    DEBET("DEBET CARD"),
    CREDIT("CREDIT CARD"),
    TRANSFER("TRANSFER"),
    EMONEY("E-MONEY"),
    COMPLIMENTARY("COMPLIMENTARY"),
    PIUTANG("PIUTANG");

    private final String type;

    PaymentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}


