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

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Inventory;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.view.listadapter.ListInventoryOrderCancelAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FnbCancelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FnbCancelFragment extends Fragment {

    @BindView(R.id.detail_recycle_orderinventory_cancelation)
    RecyclerView detailRecycleOrderInventoryCancelation;

    @BindView(R.id.warning_layout)
    View warnLayout;



    private Room room;

    private ListInventoryOrderCancelAdapter listDetailInventoryOrderCancelAdapter;
    private ArrayList<Inventory> listOrderInventoryCancel = new ArrayList<>();
    private static final String ARG_PARAM1 = "ROOM_ORDER";
    private RoomOrder roomOrder;

    public FnbCancelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomOrder Parameter 1.
     * @return A new instance of fragment RoomOrderInventoryCancelFragment.
     */

    public static FnbCancelFragment newInstance(RoomOrder roomOrder) {
        FnbCancelFragment fragment = new FnbCancelFragment();
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
        View view = inflater.inflate(R.layout.fragment_fnb_cancel, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        room = roomOrder.getCheckinRoom();
        setRecycleOrderInventoryCancel();
    }

    private void setRecycleOrderInventoryCancel() {
        listOrderInventoryCancel = (ArrayList<Inventory>) roomOrder.getInventoryCancelation();
        if (listOrderInventoryCancel.size() > 0) {
            if (null == listDetailInventoryOrderCancelAdapter) {
                listDetailInventoryOrderCancelAdapter = new ListInventoryOrderCancelAdapter(getContext(), listOrderInventoryCancel);
                detailRecycleOrderInventoryCancelation.setAdapter(listDetailInventoryOrderCancelAdapter);
                detailRecycleOrderInventoryCancelation.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            listDetailInventoryOrderCancelAdapter.notifyDataSetChanged();
        }else{
            warnLayout.setVisibility(View.VISIBLE);
        }

    }
}