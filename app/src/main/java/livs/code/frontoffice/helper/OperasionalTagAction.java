package livs.code.frontoffice.helper;

public enum OperasionalTagAction {

    EXTEND("Extend"),
    TRANSFER("Transfer"),
    PAYMENT("Payment"),
    CHECKOUT("Checkout"),
    CLEAN("Clean"),
    CONFIRM_FNB("Konfirmasi Order");


    private final String tag;

    OperasionalTagAction(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
