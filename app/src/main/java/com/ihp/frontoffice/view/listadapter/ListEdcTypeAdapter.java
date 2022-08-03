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
import com.ihp.frontoffice.data.entity.TypeEdc;

public class ListEdcTypeAdapter extends ArrayAdapter<TypeEdc> {

    public ListEdcTypeAdapter(@NonNull Context context, List<TypeEdc> roomPromoList) {
        super(context, 0, roomPromoList);
        /*this.context = context;
        this.roomPromoList = roomPromoList;*/
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        TypeEdc roomPromo = getItem(position);
        View view = convertView;

        // Jika tidak ada view atau view sama dengan null maka buat lagi view yang baru
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.holder_view_edc, parent, false);
        }

        TextView namaPromo = (TextView) view.findViewById(R.id.edc_name);

        namaPromo.setText(roomPromo.getEdcName());

        return view;
    }


}
