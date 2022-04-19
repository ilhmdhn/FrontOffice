package livs.code.frontoffice.helper;

public enum InventoryLocationProduction {
    CASHIER_FO(1),
    KITCHEN(2),
    BAR(3);

    private final int state;

    InventoryLocationProduction(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}


