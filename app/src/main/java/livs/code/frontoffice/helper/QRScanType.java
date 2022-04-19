package livs.code.frontoffice.helper;

public enum QRScanType {
    CHECKIN("CHECKIN"),
    RESERVASI("RESERVASI"),
    VOUCHER("VOUCHER"),
    MEMBER_INFO("MEMBER");

    private final String type;

    QRScanType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}


