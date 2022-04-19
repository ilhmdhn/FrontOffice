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
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;

public class ListOperasionalReadyRoomAdapter extends RecyclerView.Adapter<ListOperasionalReadyRoomAdapter.RoomViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Room> roomList;

    public ListOperasionalReadyRoomAdapter(Context context, ArrayList<Room> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_operasional_ready_room, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        final RoomViewHolder roomViewHolder = (RoomViewHolder) holder;
        roomViewHolder.setRoom(room);
        roomViewHolder._roomNumber.setText(room.getRoomCode());
        roomViewHolder._roomCapacity.setText(String.valueOf(room.getRoomCapacity()));
        if(room.getRoomCapacity()<2){
            roomViewHolder._roomCapacity.setVisibility(View.GONE);
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


    class RoomViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {


        @BindView(R.id.room_number)
        TextView _roomNumber;

        @BindView(R.id.room_capacity)
        TextView _roomCapacity;
        private Room room;

        private RoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setRoom(Room room) {
            this.room = room;
        }

        public Room getRoom() {
            return room;
        }

        @Override
        public void onClick(View view) {
            GlobalBus
                    .getBus()
                    .post(new EventsWrapper.OperasionalBusCheckinRoom(this.room));
        }
    }
}
