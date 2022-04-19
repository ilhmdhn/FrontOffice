package livs.code.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.RoomPromo;

public class ListPromoRoomAdapter extends ArrayAdapter<RoomPromo> {
    /*List<RoomPromo> roomPromoList;
    Context context;*/

    public ListPromoRoomAdapter(@NonNull Context context, List<RoomPromo> roomPromoList) {
        super(context, 0, roomPromoList);
        /*this.context = context;
        this.roomPromoList = roomPromoList;*/
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        RoomPromo roomPromo = getItem(position);
        View view = convertView;

        // Jika tidak ada view atau view sama dengan null maka buat lagi view yang baru
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.holder_view_promo, parent, false);
        }

        TextView namaPromo = (TextView) view.findViewById(R.id.promo_name);
        TextView promoValue = view.findViewById(R.id.promo_value);
        TextView timeStart = view.findViewById(R.id.time_start);
        TextView timeEnd = view.findViewById(R.id.time_end);
        TextView desc = view.findViewById(R.id.desc);

        namaPromo.setText(roomPromo.getRoomPromoName());
        StringBuilder promoValueFrmt = new StringBuilder();

        promoValueFrmt.append(roomPromo.getDiscountPercent());
        promoValueFrmt.append("%");
        promoValueFrmt.append(" - ");
        promoValueFrmt.append(roomPromo.getDiscountRupiah());
        promoValue.setText(promoValueFrmt.toString());

        timeStart.setText(roomPromo.getTimeStart());
        timeEnd.setText(roomPromo.getTimeEnd());
        desc.setText(roomPromo.getNotes());
        return view;
    }


}
