package com.ihp.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.view.fragment.fnb.ListInventoryFragmentDirections;

public class ListCheckinRoomAdapter extends RecyclerView.Adapter<ListCheckinRoomAdapter.RoomViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Room> roomList;

    public ListCheckinRoomAdapter(Context context, ArrayList<Room> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_checkin_room, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        final RoomViewHolder roomViewHolder = (RoomViewHolder) holder;
        roomViewHolder.setRoom(room);
        roomViewHolder._roomKode.setText(room.getRoomCode());
        roomViewHolder._roomTipe.setText(room.getRoomType());
        roomViewHolder._checkinTime.setText(AppUtils.getTanggal(room.getRoomCheckinHours()));
        roomViewHolder._checkoutTime.setText(AppUtils.getTanggal(room.getRoomCheckoutHours()));
        roomViewHolder._roomMember.setText(room.getRoomGuessName());

        if(Objects.equals(room.getStatusPrinter(), "0")){
            roomViewHolder._bttnDetail.setVisibility(View.VISIBLE);
        }else{
            roomViewHolder._bttnDetail.setVisibility(View.INVISIBLE);
        }
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

        @BindView(R.id.bttn_detail)
        Button _bttnDetail;

        private Room room;

        private RoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            _bttnDetail.setOnClickListener(view -> {
                Navigation
                        .findNavController(view)
                        .navigate(ListInventoryFragmentDirections.actionNavListInventoryFragmentToNavOperasionalFnbFragment(room));
            });


        }

        public void setRoom(Room room) {
            this.room = room;
        }

        public Room getRoom() {
            return room;
        }
    }
}
