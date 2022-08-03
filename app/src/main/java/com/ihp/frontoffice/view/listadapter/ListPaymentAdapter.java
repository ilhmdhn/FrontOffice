package com.ihp.frontoffice.view.listadapter;

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
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Payment;
import com.ihp.frontoffice.helper.AppUtils;

public class ListPaymentAdapter extends RecyclerView.Adapter<ListPaymentAdapter.PaymentViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Payment> roomList;
    private RemovePayment removePaymentListener;
    private EditPayment editPaymentListener;

    public ListPaymentAdapter(Context context, ArrayList<Payment> roomArrayList,
                              RemovePayment removePaymentListener,EditPayment editPaymentListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
        this.removePaymentListener = removePaymentListener;
        this.editPaymentListener = editPaymentListener;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_payment, parent, false);
        return new PaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = roomList.get(position);
        final PaymentViewHolder paymentViewHolder = (PaymentViewHolder) holder;

        paymentViewHolder.setPayment(payment);
        paymentViewHolder._nominal.setText(AppUtils.formatNominal(payment.getNominal()));
        paymentViewHolder._type.setText(payment.getPaymentType());

        paymentViewHolder._deletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePaymentListener.onRemovePayment(payment, position);
            }
        });

        paymentViewHolder._editPayment.setOnClickListener(view -> {
            editPaymentListener.onEditPayment(payment,position);
        });

    }

    @Override
    public int getItemCount() {
        if (roomList != null)
            return roomList.size();
        else return 0;
    }


    class PaymentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.holderPaymentNominal)
        TextView _nominal;

        @BindView(R.id.holderPaymentType)
        TextView _type;

        @BindView(R.id.button_payment_edit)
        ImageView _editPayment;

        @BindView(R.id.button_payment_delete)
        ImageView _deletePayment;


        private Payment payment;

        private PaymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public Payment getPayment() {
            return payment;
        }

        public void setPayment(Payment payment) {
            this.payment = payment;
        }
    }

    public interface RemovePayment {
        void onRemovePayment(Payment payment, int index);
    }

    public interface EditPayment {
        void onEditPayment(Payment payment, int index);
    }

}
