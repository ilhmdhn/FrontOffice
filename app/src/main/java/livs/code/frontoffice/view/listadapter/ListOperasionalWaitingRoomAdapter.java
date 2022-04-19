package livs.code.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.helper.AppUtils;

public class ListOperasionalWaitingRoomAdapter extends RecyclerView.Adapter<ListOperasionalWaitingRoomAdapter.RoomViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Room> roomList;

    public ListOperasionalWaitingRoomAdapter(Context context, ArrayList<Room> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_operasional_waiting_room, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        final RoomViewHolder roomViewHolder = (RoomViewHolder) holder;
        roomViewHolder.setRoom(room);
        roomViewHolder._roomInfo.setText(room.getRoomType()+" "+ room.getRoomCode());
        roomViewHolder._memberInfo.setText(room.getRoomGuessName());
        roomViewHolder.setInfotime();

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
        @BindView(R.id.room_info)
        TextView _roomInfo;

        @BindView(R.id.member_info)
        TextView _memberInfo;

        @BindView(R.id.time_info)
        TextView _timeInfo;

        private Room room;
        private RoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setInfotime(){
            _timeInfo.setText(AppUtils.formatSisaWaktuCheckin(this.room));
        }



        public void setRoom(Room room) {
            this.room = room;
        }
    }
}
