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
import livs.code.frontoffice.data.entity.Inventory;
import livs.code.frontoffice.helper.AppUtils;

public class ListDetailInventoryOrderAdapter extends RecyclerView.Adapter<ListDetailInventoryOrderAdapter.InventoryOrderViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Inventory> inventoryList;

    public ListDetailInventoryOrderAdapter(Context context, ArrayList<Inventory> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.inventoryList = roomArrayList;
    }

    @NonNull
    @Override
    public InventoryOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_detail_inventory_order, parent, false);
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

        String total = "Qty " + ent.getQty() + " | Total : " + AppUtils.formatNominal(ent.getTotalAfterDiscount());
        String code = ent.getInventoryCode() + " | " + ent.getSlipOrder() + " | " + ent.getOrderPenjualan();

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


    class InventoryOrderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_inventory_name)
        TextView detailInventoryName;

        @BindView(R.id.detail_inventory_total)
        TextView detailInventoryTotal;

        @BindView(R.id.detail_keterangan_inventory)
        TextView detailKeteranganInventory;

        @BindView(R.id.detail_inventory_code)
        TextView detailInventoryCode;


        private InventoryOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }


}
