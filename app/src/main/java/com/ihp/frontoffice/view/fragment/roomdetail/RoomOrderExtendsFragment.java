package com.ihp.frontoffice.view.fragment.roomdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.helper.RoomState;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.view.listadapter.ListDetailRoomOrderExtendAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomOrderExtendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomOrderExtendsFragment extends Fragment {

    @BindView(R.id.extends_history_checkin)
    ListView detailListViewRoomOrderExtend;

    @BindView(R.id.warning_layout)
    View warnLayout;

    @BindView(R.id.btn_extend)
    AppCompatButton btnExtend;

    private static final String ARG_PARAM1 = "ROOM_ORDER";
    private RoomOrder roomOrder;
    private ListDetailRoomOrderExtendAdapter listDetailRoomOrderExtendAdapter;
    private ArrayList<Room> listRoomOrderExtend;

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
    @BindView(R.id.image_discount_room_extend)
    ImageView imageDiscount;

    private Room room;
    private String residualTime;
    public RoomOrderExtendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomOrder Parameter 1.
     * @return A new instance of fragment RoomOrderExtendsFragment.
     */

    public static RoomOrderExtendsFragment newInstance(RoomOrder roomOrder) {
        RoomOrderExtendsFragment fragment = new RoomOrderExtendsFragment();
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
        View view = inflater.inflate(R.layout.fragment_room_order_extends, container, false);
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
            btnExtend.setVisibility(View.GONE);
        }
        roomState.setText(state);

        String timeIn = AppUtils.getTanggal(room.getRoomCheckinHours());
        String timeOut = AppUtils.getTanggal(room.getRoomCheckoutHours());

        if(RoomState.HISTORY.getState() == room.getRoomState()){
            residualTime =(room.getRoomCheckinDuration()/60)+" Jam "+(room.getRoomCheckinDuration()%60)+" Menit";
        }else {
            residualTime = formatWaktuCheckin();
        }

        roomInTime.setText(timeIn);
        roomOutTime.setText(timeOut);
        roomResidualTime.setText(residualTime);

        setRecycleRoomOrderExtend();
        btnExtend.setOnClickListener(view -> {
           /* Navigation
                    .findNavController(view)
                    .navigate(RoomOrderDetailFragmentDirections
                            .actionNavDetailRoomFragmentToNavExtendsRoomFragment(roomOrder.getCheckinRoom()));*/
        });
    }

    private String formatWaktuCheckin() {
        String kata = "";
        if (room.getRoomResidualCheckinHoursTime() < 1 &&
                room.getRoomResidualCheckinHoursMinutesTime() <1) {
            return kata;
        } else {
            kata = "Sisa ";
            if (room.getRoomResidualCheckinHoursTime() > 0) {
                kata =  + room.getRoomResidualCheckinHoursTime() + " jam ";
            }

            kata = kata + room.getRoomResidualCheckinHoursMinutesTime() + " menit ";
        }

        return kata;
    }

    private void setRecycleRoomOrderExtend() {
        listRoomOrderExtend = (ArrayList<Room>) roomOrder.getOrderRoomExtends();
        if (listRoomOrderExtend.size() > 0) {
            if (null == listDetailRoomOrderExtendAdapter) {
                listDetailRoomOrderExtendAdapter = new ListDetailRoomOrderExtendAdapter(requireActivity(), listRoomOrderExtend);
                detailListViewRoomOrderExtend.setAdapter(listDetailRoomOrderExtendAdapter);

            }
            listDetailRoomOrderExtendAdapter.notifyDataSetChanged();
        }
        else{
            warnLayout.setVisibility(View.VISIBLE);
        }
    }
}