package livs.code.frontoffice.events;


import livs.code.frontoffice.data.entity.Inventory;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomType;

public class EventsWrapper {

    public static class PrintBillInvoice {
        private String code;

        public PrintBillInvoice(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
    public static class RefreshDataFragment {
        private boolean state;
        public RefreshDataFragment(boolean b) {
            this.state = b;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }
    }

    public static class ScanResult {
        private String data;
        private boolean isSuccess;

        public ScanResult(boolean isSuccess,String data) {
            this.isSuccess = isSuccess;
            this.data = data;

        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }


    public static class DbProgressDone {
        private boolean isProgressDone;
        public DbProgressDone(Boolean success) {
            this.isProgressDone =success;
        }

        public boolean isProgressDone() {
            return isProgressDone;
        }

        public void setProgressDone(boolean progressDone) {
            isProgressDone = progressDone;
        }
    }

    public static class CheckoutRoom {

        private Room room;
        public CheckoutRoom(Room room) {
            this.room = room;
        }

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }
    }

    public static class CleanRoom{
        private Room room;

        public CleanRoom(Room room) {
            this.room = room;
        }

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }
    }

    public static class RefreshCurrentFragment {

        private  boolean refresh;

        public RefreshCurrentFragment(boolean b) {
            this.refresh = b;
        }

        public boolean isRefresh() {
            return refresh;
        }

        public void setRefresh(boolean refresh) {
            this.refresh = refresh;
        }
    }

    public static class TitleFragment {


        private  String title;

        public TitleFragment(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class DeliveryOrderInventory {
        private Inventory inventory;
        public DeliveryOrderInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    }

    public static class CancelOrderInventory {
        private Inventory inventory;
        public CancelOrderInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    }



    public static class ResetNotifikasi {
        public ResetNotifikasi(boolean b) {
        }
    }

    public static class OperasionalBusRoomType {
        private RoomType roomType;
        public OperasionalBusRoomType(RoomType roomType) {
        this.roomType = roomType;
        }

        public RoomType getRoomType() {
            return roomType;
        }
    }

    public static class OperasionalBusCheckinRoom {
        private Room room;
        public OperasionalBusCheckinRoom(Room room) {
            this.room = room;
        }

        public Room getRoom() {
            return room;
        }
    }

    public static class NavigationInvoicePayment {
        private boolean toPayment;

        public NavigationInvoicePayment(boolean toPayment) {
            this.toPayment = toPayment;
        }

        public boolean isToPayment() {
            return toPayment;
        }

        public void setToPayment(boolean toPayment) {
            this.toPayment = toPayment;
        }
    }

    public static class NavigationPaymentInvoice {
        private boolean toInvoice;

        public NavigationPaymentInvoice(boolean toInvoice) {
            this.toInvoice = toInvoice;
        }

        public boolean isToInvoice() {
            return toInvoice;
        }

        public void setToInvoice(boolean toInvoice) {
            this.toInvoice = toInvoice;
        }
    }

    public static class XZscan {
        private String scanTypeAction;

        public XZscan(String scanTypeAction) {
            this.scanTypeAction = scanTypeAction;
        }

        public String getScanTypeAction() {
            return scanTypeAction;
        }
    }
}
