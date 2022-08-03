package com.ihp.frontoffice.view.listadapter;

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
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.RoomType;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;

public class ListOperasionalReadyRoomTypeAdapter extends RecyclerView.Adapter<ListOperasionalReadyRoomTypeAdapter.TypeRoomViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<RoomType> roomList;

    public ListOperasionalReadyRoomTypeAdapter(Context context, ArrayList<RoomType> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public TypeRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_operasional_ready_room_type, parent, false);
        return new TypeRoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeRoomViewHolder holder, int position) {
        RoomType roomType = roomList.get(position);
        final TypeRoomViewHolder holder1 = (TypeRoomViewHolder) holder;
        holder1.setRoomType(roomType);

        holder1._availableInfo.setText("" + roomType.getAvailableRoom());
        holder1._typeInfo.setText(roomType.getRoomType());

    }

    public void setTypeRooms(List<RoomType> rooms) {
        roomList = rooms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (roomList != null)
            return roomList.size();
        else return 0;
    }


    class TypeRoomViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {


        @BindView(R.id.room_type_available)
        TextView _availableInfo;

        @BindView(R.id.room_type)
        TextView _typeInfo;


        private RoomType roomType;

        private TypeRoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setRoomType(RoomType roomType) {
            this.roomType = roomType;
        }

        @Override
        public void onClick(View view) {
            GlobalBus
                    .getBus()
                    .post(new EventsWrapper.OperasionalBusRoomType(this.roomType));
        }
    }
}
