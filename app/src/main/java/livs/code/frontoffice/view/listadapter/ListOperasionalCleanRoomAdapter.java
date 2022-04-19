package livs.code.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import livs.code.frontoffice.helper.AppUtils;

public class ListOperasionalCleanRoomAdapter extends RecyclerView.Adapter<ListOperasionalCleanRoomAdapter.RoomViewHolder> {
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<Room> roomList;

    public ListOperasionalCleanRoomAdapter(Context context, ArrayList<Room> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_operasional_clean_room, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        final RoomViewHolder holder1 = (RoomViewHolder) holder;
        holder1.setRoom(room);
        holder1._nameMember.setText("Capacity "+ room.getRoomCapacity());
        holder1._roomInfo.setText(room.getRoomCode());

        holder1.setMemberImage();
    }


    @Override
    public int getItemCount() {
        if (roomList != null)
            return roomList.size();
        else return 0;
    }

    public void clearItem() {
        roomList.clear();
        notifyDataSetChanged();
    }


    class RoomViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        @BindView(R.id.image_member)
        ImageView memberFoto;

        @BindView(R.id.name_member)
        TextView _nameMember;

        @BindView(R.id.room_info)
        TextView _roomInfo;



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




        public void setMemberImage() {
          /*  Glide.with(context)
                    .load(room.getImgGuessPath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.user)
                    .skipMemoryCache(true)
                    .into(memberFoto);*/
            //memberFoto.setVisibility(View.GONE);
        }
    }
}
