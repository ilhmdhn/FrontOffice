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
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;

public class ListInventoryOrderAdapter extends RecyclerView.Adapter<ListInventoryOrderAdapter.InventoryOrderViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Inventory> inventoryList;

    public ListInventoryOrderAdapter(Context context, ArrayList<Inventory> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.inventoryList = roomArrayList;
    }

    @NonNull
    @Override
    public InventoryOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_act_inventory_order, parent, false);
        return new InventoryOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryOrderViewHolder holder, int position) {
        Inventory ent = inventoryList.get(position);
        final InventoryOrderViewHolder viewHolder = (InventoryOrderViewHolder) holder;
        viewHolder.setInventory(ent);

        String desc = "UNIT PRICE : @" + AppUtils.formatNominal(ent.getUnitPrice());

        if (ent.getTotalDiscount() > 0) {
            desc = desc + " UNIT DISCOUNT : @" + AppUtils.formatNominal(ent.getUnitDiscount());
        }

        String total = "Total " + AppUtils.formatNominal(ent.getTotalAfterDiscount());
        String code;
        if(null!=ent.getSlipOrder() ){
             code = ent.getInventoryCode() + "|" + ent.getSlipOrder() ;
        }else{
            code = ent.getInventoryCode();
        }


        viewHolder.detailInventoryName.setText(ent.getInventoryName());
        viewHolder.detailInventoryTotal.setText(total);
        viewHolder.detailInventoryCode.setText(code);
        viewHolder.detailInventoryQty.setText("Qty "+(ent.getQty()-ent.getQtyOkd()));
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

        @BindView(R.id.bttn_do)
        TextView buttonDo;

        Inventory inventory;

        private InventoryOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            buttonDo.setOnClickListener(view -> {
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper.DeliveryOrderInventory(this.inventory));
            });
        }


        public Inventory getInventory() {
            return inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    }


}
