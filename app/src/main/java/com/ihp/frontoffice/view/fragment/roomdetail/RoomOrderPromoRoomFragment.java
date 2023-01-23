package com.ihp.frontoffice.view.fragment.roomdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.entity.RoomPromo;
import com.ihp.frontoffice.helper.RoomState;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.view.listadapter.ListPromoRoomAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomOrderPromoRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomOrderPromoRoomFragment extends Fragment {

    @BindView(R.id.promo_room_listview)
    ListView detailListViewPromoRoom;
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
    @BindView(R.id.image_discount_order_promo_room)
    ImageView imageDiscount;

    private Room room;
    private String residualTime;
    private ListPromoRoomAdapter listPromoRoomAdapter;
    private ArrayList<RoomPromo> listPromoRoom = new ArrayList<>();
    private static final String ARG_PARAM1 = "ROOM_ORDER";
    private RoomOrder roomOrder;

    public RoomOrderPromoRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomOrder Parameter 1.
     * @return A new instance of fragment RoomOrderPromoRoomFragment.
     */

    public static RoomOrderPromoRoomFragment newInstance(RoomOrder roomOrder) {
        RoomOrderPromoRoomFragment fragment = new RoomOrderPromoRoomFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, roomOrder);
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

        View view = inflater.inflate(R.layout.fragment_room_order_promo_room, container, false);
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
        }
        else if (RoomState.HISTORY.getState() == room.getRoomState()) {
            state = "Room history | ";
        }
        roomState.setText(state);

        String timeIn = AppUtils.getTanggal(room.getRoomCheckinHours());
        String timeOut = AppUtils.getTanggal(room.getRoomCheckoutHours());

        if(RoomState.HISTORY.getState() == room.getRoomState()){
            residualTime = " "+(room.getRoomCheckinDuration()/60)+" Jam "+(room.getRoomCheckinDuration()%60)+" Menit";
        }else {
            residualTime = formatWaktuCheckin();
        }

        roomInTime.setText(timeIn);
        roomOutTime.setText(timeOut);
        roomResidualTime.setText(residualTime);
        setListViewPromoRoom();
    }

    private String formatWaktuCheckin() {
        String kata = "";
        if (room.getRoomResidualCheckinHoursTime() != 0 &&
                room.getRoomResidualCheckinHoursMinutesTime() != 0) {
            return kata;
        } else {
            if (room.getRoomResidualCheckinHoursTime() != 0) {
                kata = "Sisa " + room.getRoomResidualCheckinHoursTime() + " jam ";
            }

            kata = kata + room.getRoomResidualCheckinHoursTime() + " menit ";
        }

        return kata;
    }

    private void setListViewPromoRoom() {
        listPromoRoom = (ArrayList<RoomPromo>) roomOrder.getRoomPromos();
        if (listPromoRoom.size() > 0) {
            if (null == listPromoRoomAdapter) {
                listPromoRoomAdapter = new ListPromoRoomAdapter(requireActivity(), listPromoRoom);
                detailListViewPromoRoom.setAdapter(listPromoRoomAdapter);
            }

            listPromoRoomAdapter.notifyDataSetChanged();
        }else{
            warnLayout.setVisibility(View.VISIBLE);
        }

    }
}