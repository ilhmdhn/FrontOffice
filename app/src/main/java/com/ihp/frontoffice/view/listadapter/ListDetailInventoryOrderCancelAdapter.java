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
import com.ihp.frontoffice.data.entity.Inventory;
import com.ihp.frontoffice.helper.AppUtils;

public class ListDetailInventoryOrderCancelAdapter extends RecyclerView.Adapter<ListDetailInventoryOrderCancelAdapter.InventoryOrderCancelViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Inventory> inventoryList;

    public ListDetailInventoryOrderCancelAdapter(Context context, ArrayList<Inventory> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.inventoryList = roomArrayList;
    }

    @NonNull
    @Override
    public InventoryOrderCancelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_detail_inventory_order_cancelation, parent, false);
        return new InventoryOrderCancelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryOrderCancelViewHolder holder, int position) {
        Inventory ent = inventoryList.get(position);
        final InventoryOrderCancelViewHolder viewHolder = holder;

        String desc = "UNIT PRICE : @" + AppUtils.formatNominal(ent.getUnitPrice());

        if (ent.getTotalDiscount() > 0) {
            desc = desc + " UNIT DISCOUNT : @" + AppUtils.formatNominal(ent.getUnitDiscount());
        }

        String total = "Qty " + ent.getQty() + " | Total : " + AppUtils.formatNominal(ent.getTotalAfterDiscount());
        String code = ent.getInventoryCode() + " | " + ent.getSlipOrder() + " | " + ent.getOrderPenjualan() + " | "+ent.getOrderCancelation();

        viewHolder.detailInventoryName.setText(ent.getInventoryName());
        viewHolder.detailInventoryTotal.setText(total);
        viewHolder.detailInventoryCode.setText(code);
        viewHolder.detailKeteranganInventory.setText(desc);
    }

    @Override
    public int getItemCount() {
        if (inventoryList != null)
            return inventoryList.size();
        else return 0;
    }


    class InventoryOrderCancelViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_inventory_name_cancel)
        TextView detailInventoryName;

        @BindView(R.id.detail_inventory_total_cancel)
        TextView detailInventoryTotal;

        @BindView(R.id.detail_inventory_keterangan_cancel)
        TextView detailKeteranganInventory;

        @BindView(R.id.detail_inventory_code_cancel)
        TextView detailInventoryCode;


        private InventoryOrderCancelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }


}
