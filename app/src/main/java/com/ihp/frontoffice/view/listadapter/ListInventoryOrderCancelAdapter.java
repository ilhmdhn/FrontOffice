package com.ihp.frontoffice.view.listadapter;

import android.content.Context;
import android.graphics.Color;
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
import com.ihp.frontoffice.data.entity.Inventory;
import com.ihp.frontoffice.helper.InventoryState;
import com.ihp.frontoffice.helper.AppUtils;

public class ListInventoryOrderCancelAdapter extends RecyclerView.Adapter<ListInventoryOrderCancelAdapter.InventoryOrderViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Inventory> inventoryList;

    public ListInventoryOrderCancelAdapter(Context context, ArrayList<Inventory> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.inventoryList = roomArrayList;
    }

    @NonNull
    @Override
    public InventoryOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_act_inventory_order_cancel, parent, false);
        return new InventoryOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryOrderViewHolder holder, int position) {
        Inventory ent = inventoryList.get(position);
        final InventoryOrderViewHolder viewHolder = (InventoryOrderViewHolder) holder;

        String desc = "UNIT PRICE : @" + AppUtils.formatNominal(ent.getUnitPrice());

        if (ent.getTotalDiscount() > 0) {
            desc = desc + " UNIT DISCOUNT : @" + AppUtils.formatNominal(ent.getUnitDiscount());
        }

        String total = "Total : " + AppUtils.formatNominal(ent.getTotalAfterDiscount());
        String code;
        if (null != ent.getSlipOrder()) {
            code = ent.getInventoryCode() + "|" + ent.getOrderPenjualan() ;
        } else {
            code = ent.getInventoryCode();
        }


        viewHolder.detailInventoryName.setText(ent.getInventoryName());
        viewHolder.detailInventoryTotal.setText(total);
        viewHolder.detailInventoryCode.setText(code);
        viewHolder.detailInventoryQty.setText("Qty " + ent.getQty());
        String state = "";
        int color = Color.GRAY;
        if (InventoryState.ORDER_SEND_TO_FO_OR_KITCHEN.getState() == ent.getInventoryState()) {
            state = "Order Dikirim ke Kasir / Dapur";
        } else if (InventoryState.ORDER_CANCEL_BY_FO_OR_KITCHEN.getState() == ent.getInventoryState()) {
            state = "Order Canceled";
            color = Color.RED;
        } else if (InventoryState.ORDER_ACCEPT_BY_FO_OR_KITCHEN.getState() == ent.getInventoryState()) {
            state = "Order Diterima Kasir / Dapur";
            color = Color.YELLOW;
        } else if (InventoryState.ORDER_COMPLETED.getState() == ent.getInventoryState()) {
            state = "Order Telah Diterima Tamu";
            color = Color.GREEN;
        }
        viewHolder.detailInventoryState.setText(state);
        viewHolder.detailInventoryState.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        if (inventoryList != null)
            return inventoryList.size();
        else return 0;
    }


    class InventoryOrderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.inventory_name)
        TextView detailInventoryName;

        @BindView(R.id.inventory_price)
        TextView detailInventoryTotal;

        @BindView(R.id.inventory_code)
        TextView detailInventoryCode;

        @BindView(R.id.inventory_qty)
        TextView detailInventoryQty;

        @BindView(R.id.inventory_state)
        TextView detailInventoryState;

        private InventoryOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }


    }


}
