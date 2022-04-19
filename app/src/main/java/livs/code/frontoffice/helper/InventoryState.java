package livs.code.frontoffice.helper;

public enum InventoryState {
    ORDER_SEND_TO_FO_OR_KITCHEN(1),
    ORDER_CANCEL_BY_FO_OR_KITCHEN(2),
    ORDER_ACCEPT_BY_FO_OR_KITCHEN(3),
    ORDER_COMPLETED(5);

    //1=terkirim ke dapur /kasir , 2=cancel by dapur/kasir, 3=diterima kasir/dapur, 5=terkirim ter DO ke Room

    private final int state;

    InventoryState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}


