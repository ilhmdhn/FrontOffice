package livs.code.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinay.stepview.HorizontalStepView;
import com.vinay.stepview.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Inventory;
import livs.code.frontoffice.helper.AppUtils;

public class ListDetailInventoryOrderProgressAdapter extends RecyclerView.Adapter<ListDetailInventoryOrderProgressAdapter.InventoryOrderProgressViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Inventory> inventoryList;

    public ListDetailInventoryOrderProgressAdapter(Context context, ArrayList<Inventory> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.inventoryList = roomArrayList;
    }

    @NonNull
    @Override
    public InventoryOrderProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_detail_inventory_order_progress, parent, false);
        return new InventoryOrderProgressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryOrderProgressViewHolder holder, int position) {
        Inventory ent = inventoryList.get(position);
        final InventoryOrderProgressViewHolder viewHolder = holder;

        String desc = "UNIT PRICE : @" + AppUtils.formatNominal(ent.getUnitPrice());

        if (ent.getTotalDiscount() > 0) {
            desc = desc + " UNIT DISCOUNT : @" + AppUtils.formatNominal(ent.getUnitDiscount());
        }

        String total = "Qty " + ent.getQty() + " | Total : " + AppUtils.formatNominal(ent.getTotalAfterDiscount());
        //String code = ent.getInventoryCode() + " | " + ent.getSlipOrder() + " | " + ent.getOrderPenjualan() + " | "+ent.getOrderCancelation();
        String code = ent.getInventoryCode() + " | " + ent.getSlipOrder();

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


    class InventoryOrderProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_inventory_name_progress)
        TextView detailInventoryName;

        @BindView(R.id.detail_inventory_total_progress)
        TextView detailInventoryTotal;

        @BindView(R.id.detail_keterangan_inventory_progress)
        TextView detailKeteranganInventory;

        @BindView(R.id.detail_inventory_code_progress)
        TextView detailInventoryCode;

        @BindView(R.id.detail_inventory_step_progress)
        HorizontalStepView mHorizontalStepView;


        private InventoryOrderProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            List<Step> stepList = new ArrayList<>();
            stepList.add(new Step("Lorem", Step.State.CURRENT));
            stepList.add(new Step("Ipsum"));
            stepList.add(new Step("Dolor"));

// Set steps
            mHorizontalStepView.setSteps(stepList);

// Complete the first step and set the second step as currently active
            mHorizontalStepView.setStepState(Step.State.COMPLETED, 0);
            mHorizontalStepView.setStepState(Step.State.CURRENT, 1);

// OR update the List and call setSteps again
            stepList.get(0).setState(Step.State.COMPLETED);
            stepList.get(1).setState(Step.State.CURRENT);
            mHorizontalStepView.setSteps(stepList);

// OR provide a new step to replace the step at a position
            Step updatedStep = new Step("Updated Step Text", Step.State.CURRENT);
            mHorizontalStepView.setStep(updatedStep, 0);

        }


    }


}
