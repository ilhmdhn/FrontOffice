package livs.code.frontoffice.view.fragment.operasional.invoicepayment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Inventory;
import livs.code.frontoffice.data.entity.Invoice;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.entity.Time;
import livs.code.frontoffice.data.repository.IhpRepository;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.helper.AppUtils;
import livs.code.frontoffice.viewmodel.RoomOrderViewModel;

public class InvoiceFragment extends Fragment {

    @BindView(R.id.payment_progressbar)
    MKLoader progressBar;

    @BindView(R.id.tv_value_jam_checkin)
    TextView valueCheckin;

    @BindView(R.id.tv_value_jam_checkout)
    TextView valueCheckout;

    @BindView(R.id.tv_value_jam_durasi)
    TextView valueDurasi;

    @BindView(R.id.value_total_ruangan)
    TextView valueRuangan;

    @BindView(R.id.value_voucher)
    TextView valueVoucher;

    @BindView(R.id.value_total_all)
    TextView valueJumlah;

    @BindView(R.id.value_total_service)
    TextView valueService;

    @BindView(R.id.value_total_tax)
    TextView valueTax;

    @BindView(R.id.value_jumlah_total)
    TextView valueJumlahTotal;

    @BindView(R.id.value_total_transfer_room)
    TextView valueTransferRuangan;

    @BindView(R.id.label_total_transfer_room)
    TextView labelTransferRuangan;

    @BindView(R.id.value_gift)
    TextView valueGift;

    @BindView(R.id.value_uang_muka)
    TextView valueDownPayment;

    @BindView(R.id.value_final_total)
    TextView valueFinalTotal;

    @BindView(R.id.info_value_total_invoice)
    TextView infoValueTotalInvoice;

    @BindView(R.id.label_voucher)
    TextView labelVoucher;

    @BindView(R.id.label_gift)
    TextView labelGift;

    @BindView(R.id.label_penjualan)
    TextView labelPenjualan;

    @BindView(R.id.btn_to_print)
    ImageButton btnToPrint;

    @BindView(R.id.btn_to_payment)
    AppCompatButton btnToPayment;

    private Invoice invoice;
    private Time timeRcp;
    private IhpRepository ihpRepository;
    private String user;

    private RoomOrderViewModel roomOrderViewModel;
    private RoomOrder roomOrder;
    private static String BASE_URL;
    private LinearLayout detailOrderLayout, detailTransferLayout;
    private static final String ARG_PARAM1 = "INVOICE";
    private static final String ARG_PARAM2 = "ROOM_ORDER";
    private static final String ARG_PARAM3 = "TIME_RCP";

    public InvoiceFragment() {
        // Required empty public constructor
    }

    public static InvoiceFragment newInstance(Invoice invoice, RoomOrder roomOrder, Time timeRcp) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, invoice);
        args.putSerializable(ARG_PARAM2, roomOrder);
        args.putSerializable(ARG_PARAM3, timeRcp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            invoice = (Invoice) getArguments().getSerializable(ARG_PARAM1);
            roomOrder = (RoomOrder) getArguments().getSerializable(ARG_PARAM2);
            timeRcp = (Time) getArguments().getSerializable(ARG_PARAM3);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        ButterKnife.bind(this, view);

        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        user = ((MyApp) getActivity().getApplicationContext()).getUserFo().getUserId();
        ihpRepository = new IhpRepository();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        roomOrderViewModel = new ViewModelProvider(getActivity())
                .get(RoomOrderViewModel.class);
        roomOrderViewModel.init(BASE_URL);
        initInvoice();
        btnToPayment.setOnClickListener(view -> {
            GlobalBus
                    .getBus()
                    .post(new EventsWrapper.NavigationInvoicePayment(true));
        });

        btnToPrint.setOnClickListener(view -> {
            ihpRepository.printBill(BASE_URL, roomOrder.getCheckinRoom().getRoomRcp(), user, requireActivity());
//            GlobalBus.getBus()
//                    .post(new EventsWrapper
//                            .PrintBillInvoice(roomOrder.getCheckinRoom().getRoomCode()));
        });
    }

    private void addDetailInventoryOrder() {
        detailOrderLayout = getView().findViewById(R.id.detail_order_layout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Inventory data : roomOrder.getSummaryOrderInventories()) {
            View rowView = inflater.inflate(R.layout.invoice_payment_detail_fnb, null);
            TextView fnbName = rowView.findViewById(R.id.fnb_name);
            TextView labelDisc = rowView.findViewById(R.id.fnb_disc_label);
            TextView fnbQty = rowView.findViewById(R.id.fnb_qty);
            TextView fnbPrice = rowView.findViewById(R.id.fnb_price);
            TextView fnbDiskon = rowView.findViewById(R.id.fnb_diskon);
            TextView fnbTotal = rowView.findViewById(R.id.fnb_total);

            fnbName.setText(data.getInventoryName());
            fnbQty.setText("(" + data.getQty());
            fnbPrice.setText(AppUtils.formatNominal(data.getUnitPrice()) + ")");
            if (data.getTotalDiscount() > 0) {
                fnbDiskon.setText("(" + AppUtils.formatNominal(data.getTotalDiscount())+")");
                //labelDisc.setVisibility(View.VISIBLE);
            } else {
                fnbDiskon.setVisibility(View.GONE);
                labelDisc.setVisibility(View.GONE);
            }
            fnbTotal.setText(AppUtils.formatNominal(data.getTotalAfterDiscount()+data.getTotalDiscount()));

            detailOrderLayout.addView(rowView);
        }
    }

    private void addDetailTransferRoom() {
        detailTransferLayout = getView().findViewById(R.id.detail_room_transfer_layout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Room roomCheckin = roomOrder.getCheckinRoom();
        for (Room room : roomOrder.getHistoryTransferOrderRoom()) {
            if (!room.getRoomCode().equals(roomCheckin.getRoomCode())) {
                View rowView = inflater.inflate(R.layout.invoice_payment_detail_room_transfer, null);
                TextView roomCode = rowView.findViewById(R.id.room_code);
                TextView total = rowView.findViewById(R.id.total);

                roomCode.setText(room.getRoomType() +" "+ room.getRoomCode());
                total.setText(AppUtils.formatNominal(room.getTotalAllInvoice()));

                detailTransferLayout.addView(rowView);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void initInvoice() {
        labelPenjualan.setVisibility(View.GONE);
        if (roomOrder.getSummaryOrderInventories().size() > 0) {
            labelPenjualan.setVisibility(View.VISIBLE);
            addDetailInventoryOrder();
        }

        if (roomOrder.getHistoryTransferOrderRoom().size() > 1) {
            labelTransferRuangan.setVisibility(View.VISIBLE);
            addDetailTransferRoom();
        }else{
            labelTransferRuangan.setVisibility(View.GONE);
        }

        valueRuangan.setText(AppUtils.formatNominal(invoice.getTotalRoom()));

        //valueFnb.setText(AppUtils.formatNominal(invoice.getTotalInventory()));
        valueCheckin.setText(timeRcp.getCheckinTime());
        valueCheckout.setText(timeRcp.getCheckoutTime());
        valueDurasi.setText(timeRcp.getTimeClock()+" jam");

        valueJumlah.setText(AppUtils.formatNominal(invoice.getTotalRoomAndInventory()));
        valueService.setText(AppUtils.formatNominal(invoice.getTotalService()));
        valueTax.setText(AppUtils.formatNominal(invoice.getTotalTax()));
        valueJumlahTotal.setText(AppUtils.formatNominal(invoice.getTotalAll()));

        valueTransferRuangan.setText(AppUtils.formatNominal(invoice.getTotalTransfer()));
        valueTransferRuangan.setVisibility(View.INVISIBLE);
        valueDownPayment.setText(AppUtils.formatNominal(invoice.getTotalUangMuka()));
        if (invoice.isGift()) {
            valueVoucher.setText("Rp0");
            valueVoucher.setVisibility(View.GONE);
            valueGift.setText(AppUtils.formatNominal(invoice.getTotalVoucher()));
            if (invoice.getTotalVoucher() < 1) {
                valueGift.setVisibility(View.GONE);
                labelVoucher.setVisibility(View.GONE);
                labelGift.setVisibility(View.GONE);
            }
        } else {
            valueVoucher.setText(AppUtils.formatNominal(invoice.getTotalVoucher()));
            valueGift.setText("Rp0");
            valueGift.setVisibility(View.GONE);
            if (invoice.getTotalVoucher() < 1) {
                valueVoucher.setVisibility(View.GONE);
                labelVoucher.setVisibility(View.GONE);
                labelGift.setVisibility(View.GONE);
            }
        }


        valueFinalTotal.setText(AppUtils.formatNominal(invoice.getTotalFinal()));
        infoValueTotalInvoice.setText(AppUtils.formatNominal(invoice.getTotalFinal()));
    }
}