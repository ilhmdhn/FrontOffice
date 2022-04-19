package livs.code.frontoffice.helper;

public enum RoomState {
    READY(0), //AVAILABLE TO CHECKIN
    CHECKSOUND(1), // AVAILABLE TO CHECKIN
    CHECKIN(2), // CHECKIN
    CLAIMED(3), //DITAGIHKAN ON CHECKIN
    PAID(4), // DIBAYAR ON CHECKIN
    CHECKOUT_REPAIRED(5),//CHECKOUT
    HISTORY(6);

    private final int state;

    RoomState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}


