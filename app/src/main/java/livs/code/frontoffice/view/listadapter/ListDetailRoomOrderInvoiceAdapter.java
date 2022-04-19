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
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.helper.AppUtils;

public class ListDetailRoomOrderInvoiceAdapter extends RecyclerView.Adapter<ListDetailRoomOrderInvoiceAdapter.RoomOrderViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Room> roomList;

    public ListDetailRoomOrderInvoiceAdapter(Context context, ArrayList<Room> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public RoomOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_detail_room_invoice, parent, false);
        return new RoomOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomOrderViewHolder holder, int position) {
        Room room = roomList.get(position);
        final RoomOrderViewHolder roomOrderViewHolder = (RoomOrderViewHolder) holder;

        roomOrderViewHolder._detailCountVisitor.setText("Total Pengunjung " + room.getTotalVisitor());
        roomOrderViewHolder._detailCountVisitorOver.setText("Overpax " + String.valueOf(AppUtils.formatRupiah(Double.parseDouble(room.getOverpaxVisitor()))));
        roomOrderViewHolder._detailKeteranganTransfer.setText(room.getTransferInfo());

        roomOrderViewHolder._detailCodeAndTypeRoom.setText(room.getRoomType() + " " + room.getRoomCode());
        roomOrderViewHolder._detailKeteranganPromo.setText(room.getKeteranganStatusPromo());

        roomOrderViewHolder._detailQf1.setText(String.valueOf(room.getQf1()));
        roomOrderViewHolder._detailQf2.setText(String.valueOf(room.getQf2()));
        roomOrderViewHolder._detailQf3.setText(String.valueOf(room.getQf3()));
        roomOrderViewHolder._detailQf4.setText(String.valueOf(room.getQf4()));

        roomOrderViewHolder._detailQf1.setText(String.valueOf(room.getQm1()));
        roomOrderViewHolder._detailQm2.setText(String.valueOf(room.getQm2()));
        roomOrderViewHolder._detailQm3.setText(String.valueOf(room.getQm3()));
        roomOrderViewHolder._detailQm4.setText(String.valueOf(room.getQm4()));
    }

    @Override
    public int getItemCount() {
        if (roomList != null)
            return roomList.size();
        else return 0;
    }


    class RoomOrderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_count_visitor)
        TextView _detailCountVisitor;

        @BindView(R.id.detail_count_visitor_overpax)
        TextView _detailCountVisitorOver;

        @BindView(R.id.detail_keterangan_transfer)
        TextView _detailKeteranganTransfer;

        @BindView(R.id.detail_room)
        TextView _detailCodeAndTypeRoom;

        @BindView(R.id.detail_keterangan_promo)
        TextView _detailKeteranganPromo;

        @BindView(R.id.detail_qm1)
        TextView _detailQm1;

        @BindView(R.id.detail_qm2)
        TextView _detailQm2;

        @BindView(R.id.detail_qm3)
        TextView _detailQm3;

        @BindView(R.id.detail_qm4)
        TextView _detailQm4;

        @BindView(R.id.detail_qf1)
        TextView _detailQf1;

        @BindView(R.id.detail_qf2)
        TextView _detailQf2;

        @BindView(R.id.detail_qf3)
        TextView _detailQf3;

        @BindView(R.id.detail_qf4)
        TextView _detailQf4;

        private RoomOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
