package com.ihp.frontoffice.view.fragment.operasional.fnb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Inventory;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.helper.InventoryLocationProduction;
import com.ihp.frontoffice.helper.InventoryState;
import com.ihp.frontoffice.view.listadapter.ListInventoryOrderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FnbConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FnbConfirmFragment extends Fragment {


    @BindView(R.id.detail_recycle_orderinventory)
    RecyclerView detailRecycleOrderInventory;

    @BindView(R.id.warning_layout)
    View warnLayout;



    private Room room;
    private ListInventoryOrderAdapter inventoryOrderAdapter;
    private ArrayList<Inventory> listOrderInventory = new ArrayList<>();
    private static final String ARG_PARAM1 = "ROOM_ORDER";
    private RoomOrder roomOrder;
    private ArrayList<Inventory> filterListOrderInventory = new ArrayList<>();

    public FnbConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomOrder Parameter 1.
     * @return A new instance of fragment RoomOrderInventoryFragment.
     */

    public static FnbConfirmFragment newInstance(RoomOrder roomOrder) {
        FnbConfirmFragment fragment = new FnbConfirmFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, roomOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomOrder = (RoomOrder) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fnb_confirm, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        room = roomOrder.getCheckinRoom();

        setRecycleOrderInventory();
    }

    private void setRecycleOrderInventory() {
        listOrderInventory = (ArrayList<Inventory>) roomOrder.getInventoryOnOrderProgress();

        filterListOrderInventory = (ArrayList<Inventory>) listOrderInventory.stream()
                .filter(inventory ->
                        (inventory.getInventoryState()!=InventoryState.ORDER_COMPLETED.getState())&&
                                (inventory.getLocationCode()== InventoryLocationProduction.CASHIER_FO.getState()))
                .collect(Collectors.toList());

        if (filterListOrderInventory.size() > 0) {
            if (null == inventoryOrderAdapter) {
                inventoryOrderAdapter = new ListInventoryOrderAdapter(getContext(), filterListOrderInventory);
                detailRecycleOrderInventory.setAdapter(inventoryOrderAdapter);
                detailRecycleOrderInventory.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            inventoryOrderAdapter.notifyDataSetChanged();
        } else {
            warnLayout.setVisibility(View.VISIBLE);
        }
    }
}