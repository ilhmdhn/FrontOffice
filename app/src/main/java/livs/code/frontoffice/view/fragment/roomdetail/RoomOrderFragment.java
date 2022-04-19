package livs.code.frontoffice.view.fragment.roomdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.helper.RoomState;
import livs.code.frontoffice.helper.AppUtils;
import livs.code.frontoffice.view.listadapter.ListDetailRoomOrderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomOrderFragment extends Fragment {

    @BindView(R.id.sign_user)
    ImageView visitorSign;

    @BindView(R.id.detail_recycle_roomorder)
    RecyclerView detailRecycleRoomOrder;

    @BindView(R.id.warning_layout)
    View warnLayout;

    @BindView(R.id.btn_transfer)
    AppCompatButton btnTransfer;

    @BindView(R.id.btn_payment)
    AppCompatButton btnPayment;

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
    private Room room;


    private static final String ARG_PARAM1 = "ROOM_ORDER";
    private RoomOrder roomOrder;

    private ListDetailRoomOrderAdapter listDetailRoomOrderAdapter;
    private ArrayList<Room> listRoomOrder = new ArrayList<>();
    private String residualTime;

    public RoomOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomOrder Parameter 1.
     * @return A new instance of fragment RoomOrderFragment.
     */

    public static RoomOrderFragment newInstance(RoomOrder roomOrder) {
        RoomOrderFragment fragment = new RoomOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_room_order, container, false);
        ButterKnife.bind(this, view);
        imageDiscount.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        room = roomOrder.getCheckinRoom();
        if(room.getMemberName()!=null){
            visitorName.setText(room.getMemberName());
        }
        else{
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
            btnTransfer.setVisibility(View.GONE);
            btnPayment.setVisibility(View.GONE);
        }
        roomState.setText(state);
        String timeIn = AppUtils.getTanggal(room.getRoomCheckinHours());
        String timeOut = AppUtils.getTanggal(room.getRoomCheckoutHours());

        if(RoomState.HISTORY.getState() == room.getRoomState()){
            residualTime = (room.getRoomCheckinDuration()/60)+" Jam "+(room.getRoomCheckinDuration()%60)+" Menit";
        }else {
            residualTime = formatWaktuCheckin();
        }
        roomInTime.setText(timeIn);
        roomOutTime.setText(timeOut);
        roomResidualTime.setText(residualTime);


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
        btnTransfer.setOnClickListener(view -> {
           /* Navigation
                    .findNavController(view)
                    .navigate(RoomOrderDetailFragmentDirections
                            .actionNavDetailRoomFragmentToNavTransferRoomFragment(roomOrder.getCheckinRoom()));*/

        });

        btnPayment.setOnClickListener(view -> {
           /* Navigation
                    .findNavController(view)
                    .navigate(RoomOrderDetailFragmentDirections
                            .actionNavDetailRoomFragmentToNavPaymentFragment(roomOrder.getCheckinRoom()));*/
        });
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
            if (null == listDetailRoomOrderAdapter) {
                listDetailRoomOrderAdapter = new ListDetailRoomOrderAdapter(getContext(), listRoomOrder);
                detailRecycleRoomOrder.setAdapter(listDetailRoomOrderAdapter);
                detailRecycleRoomOrder.setLayoutManager(new LinearLayoutManager(getContext()));

            }
            listDetailRoomOrderAdapter.notifyDataSetChanged();
        } else {
            warnLayout.setVisibility(View.VISIBLE);
        }

    }


}