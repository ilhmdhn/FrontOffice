package com.ihp.frontoffice.view.fragment.operasional.fnb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Inventory;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.helper.UserAuthRole;
import com.ihp.frontoffice.view.listadapter.ListInventoryOrderProgressAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FnbProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FnbProgressFragment extends Fragment {

    @BindView(R.id.detail_recycle_orderinventory_progress)
    RecyclerView detailRecycleOrderInventoryProgress;

    @BindView(R.id.warning_layout)
    View warnLayout;

    @BindView(R.id.total_order)
    TextView tvOrderDone;

    @BindView(R.id.total_process)
    TextView tvOrderProcess;

    private Room room;
    private User USER_FO;

    private ListInventoryOrderProgressAdapter listDetailInventoryOrderProgressAdapter;
    private ArrayList<Inventory> listOrderInventoryProgress = new ArrayList<>();

    private static final String ARG_PARAM1 = "ROOM_ORDER";
    private RoomOrder roomOrder;
    private ArrayList<Inventory> filterListOrderInventory = new ArrayList<>();


    public FnbProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomOrder Parameter 1.
     * @return A new instance of fragment RoomOrderInventoryProgressFragment.
     */

    public static FnbProgressFragment newInstance(RoomOrder roomOrder) {
        FnbProgressFragment fragment = new FnbProgressFragment();
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
        View view = inflater.inflate(R.layout.fragment_fnb_progress, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Double progressPrice  = 0.0;
        room = roomOrder.getCheckinRoom();
        USER_FO = ((MyApp) requireActivity().getApplicationContext()).getUserFo();
        setRecycleOrderInventoryProgress();
        for (Inventory order : roomOrder.getInventoryOnOrderProgress()) {
            if (order.getInventoryState() == 3) {
                progressPrice += (order.unitPrice*order.qty);
            }
        }
        tvOrderDone.setText(AppUtils.formatNominal(Double.parseDouble(roomOrder.getCheckinRoom().getChargePenjualan())));
        tvOrderProcess.setText(AppUtils.formatNominal(progressPrice));
    }

    //TODO : Cancel Order Inventory
    private void setRecycleOrderInventoryProgress() {
        listOrderInventoryProgress = (ArrayList<Inventory>) roomOrder.getInventories();

        Boolean canCancel = true;

        if(UserAuthRole.isAllowTransactionFnbAll(USER_FO)){
            canCancel = true;
        }else{
            canCancel = false;
        }

        filterListOrderInventory = (ArrayList<Inventory>) listOrderInventoryProgress.stream()
                .filter(inventory ->
                        (inventory.getQtyBeforeOcd()>0))
                .collect(Collectors.toList());

        if (filterListOrderInventory.size() > 0) {
            if (null == listDetailInventoryOrderProgressAdapter) {
                listDetailInventoryOrderProgressAdapter = new ListInventoryOrderProgressAdapter(getContext(), filterListOrderInventory, canCancel);
                detailRecycleOrderInventoryProgress.setAdapter(listDetailInventoryOrderProgressAdapter);
                detailRecycleOrderInventoryProgress.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            listDetailInventoryOrderProgressAdapter.notifyDataSetChanged();
        }else{
            warnLayout.setVisibility(View.VISIBLE);
        }

    }

}