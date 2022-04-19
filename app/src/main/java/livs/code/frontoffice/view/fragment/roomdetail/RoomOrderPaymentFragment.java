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
import livs.code.frontoffice.data.entity.Payment;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.helper.AppUtils;
import livs.code.frontoffice.helper.RoomState;
import livs.code.frontoffice.view.listadapter.ListDetailRoomOrderPaymentAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomOrderPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomOrderPaymentFragment extends Fragment {

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

    @BindView(R.id.payment_bayar)
    TextView _paymentBayar;
    @BindView(R.id.payment_total0_)
    TextView _paymentTotal0_;

    @BindView(R.id.payment_kembali)
    TextView _paymentKembali;

    private Room room;
    private Payment payment;


    private static final String ARG_PARAM1 = "ROOM_ORDER";
    private RoomOrder roomOrder;

    private ListDetailRoomOrderPaymentAdapter listDetailRoomOrderPaymentAdapter;
    private ArrayList<Room> listRoomOrder = new ArrayList<>();
    private ArrayList<Payment> listPayment = new ArrayList<>();
    private String residualTime;
    private Double totalRincianPembayaran = Double.valueOf(0);

    public RoomOrderPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomOrder Parameter 1.
     * @return A new instance of fragment RoomOrderFragment.
     */

    public static RoomOrderPaymentFragment newInstance(RoomOrder roomOrder) {
        RoomOrderPaymentFragment fragment = new RoomOrderPaymentFragment();
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
        View view = inflater.inflate(R.layout.fragment_room_order_payment, container, false);
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
        totalRincianPembayaran = Double.valueOf(0);
        for(int a=0; a<roomOrder.getOrderRoomPayments().size();a++){
            totalRincianPembayaran=totalRincianPembayaran+roomOrder.getOrderRoomPayments().get(a).getNominal();
        }
        _paymentBayar.setText(AppUtils.formatRupiah(roomOrder.getOrderRoomPayments().get(0).getBayar()));
        _paymentTotal0_.setText(AppUtils.formatRupiah(totalRincianPembayaran));
        _paymentKembali.setText(AppUtils.formatRupiah(roomOrder.getOrderRoomPayments().get(0).getKembali()));

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
        listPayment = (ArrayList<Payment>)roomOrder.getOrderRoomPayments();
        if (listPayment.size() > 0) {
            if (null == listDetailRoomOrderPaymentAdapter) {
                listDetailRoomOrderPaymentAdapter = new ListDetailRoomOrderPaymentAdapter(getContext(), listPayment);
                detailRecycleRoomOrder.setAdapter(listDetailRoomOrderPaymentAdapter);
                detailRecycleRoomOrder.setLayoutManager(new LinearLayoutManager(getContext()));

            }
            listDetailRoomOrderPaymentAdapter.notifyDataSetChanged();
        } else {
            warnLayout.setVisibility(View.VISIBLE);
        }

    }


}