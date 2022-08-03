package com.ihp.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Payment;
import com.ihp.frontoffice.helper.AppUtils;

public class ListDetailRoomOrderPaymentAdapter extends RecyclerView.Adapter<ListDetailRoomOrderPaymentAdapter.RoomOrderViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Payment> paymentList;

    public ListDetailRoomOrderPaymentAdapter(Context context, ArrayList<Payment> paymentArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.paymentList = paymentArrayList;
    }

    @NonNull
    @Override
    public RoomOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_detail_room_payment, parent, false);
        return new RoomOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomOrderViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        final RoomOrderViewHolder roomOrderViewHolder = (RoomOrderViewHolder) holder;

        /*roomOrderViewHolder._detailCountVisitor.setText("Total Pengunjung " + room.getTotalVisitor());
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
        roomOrderViewHolder._detailQm4.setText(String.valueOf(room.getQm4()));*/

        roomOrderViewHolder.layout_first.setVisibility(View.GONE);
        roomOrderViewHolder.layout_second.setVisibility(View.GONE);

        roomOrderViewHolder._paymentTotal.setText(String.valueOf(AppUtils.formatRupiah(payment.getNominal())));
        roomOrderViewHolder._payment.setText(
                payment.getPaymentType()+" "+
                        payment.getBankType()
        );
        roomOrderViewHolder._paymentNama.setText(payment.getBankAkunName());
        roomOrderViewHolder._paymentNoKartu.setText(payment.getBankAkunNumber());
        roomOrderViewHolder._paymentNoApproval.setText(payment.getBankAkunApproval());
        roomOrderViewHolder._paymentKeterangan.setText(payment.getBankType());

        roomOrderViewHolder._paymentNoKartu.setVisibility(View.GONE);
        roomOrderViewHolder._paymentNoApproval.setVisibility(View.GONE);
        roomOrderViewHolder._paymentNama.setVisibility(View.GONE);
        roomOrderViewHolder._paymentKeterangan.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (paymentList != null)
            return paymentList.size();
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

        @BindView(R.id.layout_first)
        LinearLayout layout_first;

        @BindView(R.id.layout_second)
        LinearLayout layout_second;

        @BindView(R.id.payment)
        TextView _payment;

        @BindView(R.id.payment_no_approval)
        TextView _paymentNoApproval;

        @BindView(R.id.payment_total)
        TextView _paymentTotal;

        @BindView(R.id.payment_keterangan)
        TextView _paymentKeterangan;

        @BindView(R.id.payment_nama)
        TextView _paymentNama;

        @BindView(R.id.payment_no_kartu)
        TextView _paymentNoKartu;

        private RoomOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
