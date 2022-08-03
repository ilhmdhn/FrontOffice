package com.ihp.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.helper.AppUtils;

public class ListDetailRoomOrderExtendAdapter extends ArrayAdapter<Room> {

    public ListDetailRoomOrderExtendAdapter(@NonNull Context context, List<Room> roomPromoList) {
        super(context, 0, roomPromoList);
        /*this.context = context;
        this.roomPromoList = roomPromoList;*/
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        Room roomPromo = getItem(position);
        View view = convertView;

        // Jika tidak ada view atau view sama dengan null maka buat lagi view yang baru
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.holder_view_extend_history, parent, false);
        }

        TextView roomInfo = (TextView) view.findViewById(R.id.extendCodeAndTypeRoom);
        TextView extendDurasi = (TextView) view.findViewById(R.id.extendDuration);
        TextView extendTime = (TextView) view.findViewById(R.id.extendTime);
        String durasi = "";
        roomInfo.setText(roomPromo.getRoomType()+" "+roomPromo.getRoomCode());
        if (roomPromo.getExtendHours() > 0 && roomPromo.getExtendMinutes() > 0) {
            durasi = roomPromo.getExtendHours() + " Jam " + roomPromo.getExtendMinutes() + " Menit";
        } else if (roomPromo.getExtendHours() > 0) {
            durasi = roomPromo.getExtendHours() + " Jam ";
        } else if (roomPromo.getExtendMinutes() > 0) {
            durasi = roomPromo.getExtendMinutes() + " Menit";
        }

        extendDurasi.setText(durasi);
        extendTime.setText(AppUtils.getTanggal(roomPromo.getTimeExtends()));


        return view;
    }


}
