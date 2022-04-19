package livs.code.frontoffice.view.fragment.roomdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.helper.AppUtils;
import livs.code.frontoffice.helper.RoomState;
import livs.code.frontoffice.view.listadapter.ListDetailRoomOrderInvoiceAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomOrderInvoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomOrderInvoiceFragment extends Fragment {

    @BindView(R.id.sign_user)
    ImageView visitorSign;

    @BindView(R.id.detail_recycle_roomorder)
    RecyclerView detailRecycleRoomOrder;

    @BindView(R.id.warning_layout)
    View warnLayout;

    @BindView(R.id.detail_visitor_name)
    TextView visitorName;

    @BindView(R.id.detail_status)
    TextView roomState;

    @BindView(R.id.checkin_time)
    TextView roomInTime;

    @BindView(R.id.checkout_time)
    TextView roomOutTime;

    @BindView(R.id.residual_time)
    TextView roomResidualTime;
    @BindView(R.id.image_discount_room_order)
    ImageView imageDiscount;

    @BindView(R.id.invoice_ruangan)
    TextView _invoiceRuangan;
    @BindView(R.id.invoice_extend_ruangan)
    TextView _invoiceExtendRuangan;
    @BindView(R.id.invoice_voucher_ruangan)
    TextView _invoiceVoucherRuangan;
    @BindView(R.id.invoice_surcharge_kamar)
    TextView _invoiceSurchargeKamar;
    @BindView(R.id.invoice_overpax)
    TextView _invoiceOverpax;
    @BindView(R.id.invoice_penjualan)
    TextView _invoicePenjualan;
    @BindView(R.id.invoice_retur_penjualan)
    TextView _invoiceReturPenjualan;
    @BindView(R.id.invoice_sub_jumlah)
    TextView _invoiceSubJumlah;
    @BindView(R.id.invoice_diskon)
    TextView _invoiceDiskon;
    @BindView(R.id.invoice_sub_sub_jumlah)
    TextView _invoiceSubSubJumlah;
    @BindView(R.id.invoice_service)
    TextView _invoiceService;
    @BindView(R.id.invoice_pajak)
    TextView _invoicePajak;
    @BindView(R.id.invoice_uang_muka)
    TextView _invoiceUangMuka;
    @BindView(R.id.invoice_gift_voucher)
    TextView _invoiceGiftVoucher;
    @BindView(R.id.invoice_grand_total)
    TextView _invoiceGrandTotal;

    private Room room;


    private static final String ARG_PARAM1 = "ROOM_ORDER";
    private RoomOrder roomOrder;

    private ListDetailRoomOrderInvoiceAdapter listDetailRoomOrderInvoiceAdapter;
    private ArrayList<Room> listRoomOrder = new ArrayList<>();
    private String residualTime;

    public RoomOrderInvoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomOrder Parameter 1.
     * @return A new instance of fragment RoomOrderFragment.
     */

    public static RoomOrderInvoiceFragment newInstance(RoomOrder roomOrder) {
        RoomOrderInvoiceFragment fragment = new RoomOrderInvoiceFragment();
        Bundle args = new Bundle();
        args.putSerializable("ROOM_ORDER", roomOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomOrder = (RoomOrder) getArguments().getSerializable(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_order_invoice, container, false);
        ButterKnife.bind(this, view);
        imageDiscount.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        room = roomOrder.getCheckinRoom();
        if (room.getMemberName() != null) {
            visitorName.setText(room.getMemberName());
        } else {
            visitorName.setText(room.getRoomGuessName());
        }
        String state = "";
        if (RoomState.READY.getState() == room.getRoomState()) {

        } else if (RoomState.CHECKIN.getState() == room.getRoomState()) {
            state = "Checkin";
        } else if (RoomState.CLAIMED.getState() == room.getRoomState()) {
            state = "Invoice Sudah Ditagihkan | ";

        } else if (RoomState.PAID.getState() == room.getRoomState()) {
            state = "Room Sudah Dibayar | ";
        } else if ((RoomState.CHECKOUT_REPAIRED.getState() == room.getRoomState()) ||
                (RoomState.CHECKSOUND.getState() == room.getRoomState())) {
        } else if (RoomState.HISTORY.getState() == room.getRoomState()) {
            state = "Room history | ";
        }
        roomState.setText(state);
        String timeIn = AppUtils.getTanggal(room.getRoomCheckinHours());
        String timeOut = AppUtils.getTanggal(room.getRoomCheckoutHours());

        if (RoomState.HISTORY.getState() == room.getRoomState()) {
            residualTime = (room.getRoomCheckinDuration() / 60) + " Jam " + (room.getRoomCheckinDuration() % 60) + " Menit";
        } else {
            residualTime = formatWaktuCheckin();
        }
        roomInTime.setText(timeIn);
        roomOutTime.setText(timeOut);
        roomResidualTime.setText(residualTime);

        _invoiceRuangan.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanSewakamar())));
        _invoiceExtendRuangan.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanExtendKamar())));
        _invoiceSurchargeKamar.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanSurchargeKamar())));
        _invoiceOverpax.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanOverpaxKamar())));
        _invoicePenjualan.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanPenjualan())));
        _invoiceReturPenjualan.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanCancelationPenjualan())));
        _invoiceSubJumlah.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanSubTotal())));
        _invoiceDiskon.setText(String.valueOf(AppUtils.formatRupiah(
                roomOrder.getInvoice().getRingkasanDiskonMemberKamar() + roomOrder.getInvoice().getRingkasanDiskonMemberPenjualan())));
        _invoiceSubSubJumlah.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanSubSubTotal())));
        _invoiceService.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanService())));
        _invoicePajak.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanPajak())));
        _invoiceUangMuka.setText("("+String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getTotalUangMuka()))+")");

        _invoiceGrandTotal.setText(String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getRingkasanTotalAll())));
        if (roomOrder.getInvoice().isGift() == true) {
            _invoiceVoucherRuangan.setText("("+String.valueOf(AppUtils.formatRupiah((double) 0))+")");
            _invoiceGiftVoucher.setText("("+String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getTotalVoucher()))+")");
        } else {
            _invoiceGiftVoucher.setText("("+String.valueOf(AppUtils.formatRupiah((double) 0))+")");
            _invoiceVoucherRuangan.setText("("+String.valueOf(AppUtils.formatRupiah(roomOrder.getInvoice().getTotalVoucher()))+")");
        }


        /*if (roomOrder.getSignPath() != "") {
            String imgUrl = MyApp.BASE_URL + "/" + roomOrder.getSignPath();
            Glide.with(this)
                    .load(imgUrl)
                    .into(visitorSign);
          *//*  Glide.with(this)
                    .load(MyApp.BASE_URL + roomOrder.getSignPath())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(true)
                    .into(visitorSign);*//*
            visitorSign.setVisibility(View.VISIBLE);
        }*/


        setRecycleRoomOrder();
    }

    private String formatWaktuCheckin() {
        String kata = "";
        if (room.getRoomResidualCheckinHoursTime() < 1 &&
                room.getRoomResidualCheckinHoursMinutesTime() < 1) {
            return kata;
        } else {
            kata = "Sisa ";
            if (room.getRoomResidualCheckinHoursTime() > 0) {
                kata = +room.getRoomResidualCheckinHoursTime() + " jam ";
            }

            kata = kata + room.getRoomResidualCheckinHoursMinutesTime() + " menit ";
        }

        return kata;
    }

    private void setRecycleRoomOrder() {
        listRoomOrder = (ArrayList<Room>) roomOrder.getHistoryTransferOrderRoom();
        if (listRoomOrder.size() > 0) {
            if (null == listDetailRoomOrderInvoiceAdapter) {
                listDetailRoomOrderInvoiceAdapter = new ListDetailRoomOrderInvoiceAdapter(getContext(), listRoomOrder);
                detailRecycleRoomOrder.setAdapter(listDetailRoomOrderInvoiceAdapter);
                detailRecycleRoomOrder.setLayoutManager(new LinearLayoutManager(getContext()));

            }
            listDetailRoomOrderInvoiceAdapter.notifyDataSetChanged();
        } else {
            warnLayout.setVisibility(View.VISIBLE);
        }

    }


}