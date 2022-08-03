package com.ihp.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.RoomState;
import com.ihp.frontoffice.helper.AppUtils;

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.RoomViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Room> roomList;

    public ListRoomAdapter(Context context, ArrayList<Room> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_room, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        final RoomViewHolder roomViewHolder = (RoomViewHolder) holder;
        roomViewHolder.setGone();
        roomViewHolder.setRoom(room);
        roomViewHolder._roomKode.setText(room.getRoomCode());
        roomViewHolder._roomTipe.setText(room.getRoomType());
        roomViewHolder._checkinTime.setText(AppUtils.getTanggal(room.getRoomCheckinHours()));
        roomViewHolder._checkoutTime.setText(AppUtils.getTanggal(room.getRoomCheckoutHours()));
        if(room.getMemberName()!=null){
            roomViewHolder._roomMember.setText(room.getMemberName());
        }
        else{
            roomViewHolder._roomMember.setText(room.getRoomGuessName());
        }

        if (roomViewHolder.getRoom().isRoomNotLobby()) {

            if (RoomState.READY.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._bttnCheckin.setVisibility(View.VISIBLE);
                roomViewHolder._bttnCheckin.setText("Check In");

            } else if (RoomState.CHECKIN.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._layoutTime.setVisibility(View.VISIBLE);
                roomViewHolder._bttnDetail.setVisibility(View.VISIBLE);
            } else if (RoomState.CLAIMED.getState() == roomViewHolder.getRoom().getRoomState()) {

            } else if (RoomState.PAID.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._layoutTime.setVisibility(View.VISIBLE);
                roomViewHolder._bttnCheckout.setVisibility(View.VISIBLE);
            } else if ((RoomState.CHECKOUT_REPAIRED.getState() == roomViewHolder.getRoom().getRoomState()) ||
                    (RoomState.CHECKSOUND.getState() == roomViewHolder.getRoom().getRoomState())) {
                roomViewHolder._bttnClean.setVisibility(View.VISIBLE);
            } else if (RoomState.HISTORY.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._layoutTime.setVisibility(View.VISIBLE);
                roomViewHolder._bttnHistory.setVisibility(View.VISIBLE);
            }

        } else {
            roomViewHolder._layoutTime.setVisibility(View.GONE);
            roomViewHolder._bttnOrder.setVisibility(View.VISIBLE);

            if (RoomState.READY.getState() == roomViewHolder.getRoom().getRoomState()) {
            } else if (RoomState.CHECKSOUND.getState() == roomViewHolder.getRoom().getRoomState()) {
            } else if (RoomState.CHECKIN.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._bttnPayment.setVisibility(View.VISIBLE);
            } else if (RoomState.CLAIMED.getState() == roomViewHolder.getRoom().getRoomState()) {
            } else if (RoomState.PAID.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._bttnCheckout.setVisibility(View.VISIBLE);
            } else if (RoomState.CHECKOUT_REPAIRED.getState() == roomViewHolder.getRoom().getRoomState()) {
            } else if (RoomState.HISTORY.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._bttnHistory.setVisibility(View.VISIBLE);
            }
        }

        // roomViewHolder._checkinDurasi.setText(room.getRoomDurasiCheckin());
    }

    public void setRooms(List<Room> rooms) {
        roomList = rooms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (roomList != null)
            return roomList.size();
        else return 0;
    }


    class RoomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_room_code)
        TextView _roomKode;

        @BindView(R.id.row_room_type)
        TextView _roomTipe;

        @BindView(R.id.row_jam_checkin)
        TextView _checkinTime;

        @BindView(R.id.row_jam_checkout)
        TextView _checkoutTime;

        @BindView(R.id.row_room_member)
        TextView _roomMember;

        @BindView(R.id.checkin_time_layout)
        LinearLayout _layoutTime;

        @BindView(R.id.bttn_checkin)
        Button _bttnCheckin;

        @BindView(R.id.bttn_order)
        Button _bttnOrder;


        @BindView(R.id.bttn_extends)
        Button _bttnExtend;

        @BindView(R.id.bttn_transfer)
        Button _bttnTransfer;

        @BindView(R.id.bttn_payment)
        Button _bttnPayment;

        @BindView(R.id.bttn_checkout)
        Button _bttnCheckout;

        @BindView(R.id.bttn_clean)
        Button _bttnClean;

        @BindView(R.id.bttn_detail)
        Button _bttnDetail;

        @BindView(R.id.bttn_history)
        Button _bttnHistory;

        private Context context;
        private Room room;

        private RoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();


            _bttnCheckin.setVisibility(View.GONE);
            _bttnOrder.setVisibility(View.GONE);
            _bttnCheckout.setVisibility(View.GONE);
            _bttnClean.setVisibility(View.GONE);
            _bttnExtend.setVisibility(View.GONE);
            _bttnPayment.setVisibility(View.GONE);
            _bttnTransfer.setVisibility(View.GONE);
            _bttnDetail.setVisibility(View.GONE);
            _bttnHistory.setVisibility(View.GONE);
            _layoutTime.setVisibility(View.GONE);

            _bttnCheckin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (room.isRoomNotLobby()) {
                       /* Navigation
                                .findNavController(view)
                                .navigate(ListRoomFragmentDirections
                                .actionNavRoomToCheckinRoomFragment(room));
                                */
                    } else {

                    }
                }
            });

            _bttnTransfer.setOnClickListener(view -> {
                /*Navigation
                        .findNavController(view)
                        .navigate(ListRoomFragmentDirections.actionNavListRoomToTransferRoomFragment(room));*/
            });

            _bttnExtend.setOnClickListener(view -> {
                /*Navigation
                        .findNavController(view)
                        .navigate(ListRoomFragmentDirections.actionNavRoomToExtendsRoomFragment(room));*/

            });

            _bttnPayment.setOnClickListener(view -> {
                /*Navigation
                        .findNavController(view)
                        .navigate(ListRoomFragmentDirections
                                .actionNavRoomToPaymentInvoiceFragment(room));*/
            });

            _bttnDetail.setOnClickListener(view -> {
               /* Navigation
                        .findNavController(view)
                        .navigate(ListHistoryRoomFragmentDirections
                                .actionNavListHistoryRoomFragmentToNavDetailHistoryRoomFragment(room));*/

            });

            _bttnHistory.setOnClickListener(view -> {
              /*  Navigation
                        .findNavController(view)
                        .navigate(ListHistoryRoomFragmentDirections
                                .actionNavListHistoryRoomFragmentToNavDetailHistoryRoomFragment(room));*/
            });

            _bttnCheckout.setOnClickListener(view -> {
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper.CheckoutRoom(this.room));
            });
            _bttnClean.setOnClickListener(view -> {
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper.CleanRoom(this.room));
            });
        }

        public void setGone() {
            _bttnCheckin.setVisibility(View.GONE);
            _bttnOrder.setVisibility(View.GONE);
            _bttnCheckout.setVisibility(View.GONE);
            _bttnExtend.setVisibility(View.GONE);
            _bttnPayment.setVisibility(View.GONE);
            _bttnTransfer.setVisibility(View.GONE);
            _bttnDetail.setVisibility(View.GONE);
            _bttnHistory.setVisibility(View.GONE);
            _layoutTime.setVisibility(View.GONE);
        }

        public void setRoom(Room room) {
            this.room = room;
        }

        public Room getRoom() {
            return room;
        }
    }
}
