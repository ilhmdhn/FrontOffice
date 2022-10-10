package com.ihp.frontoffice.view.fragment.fnb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.view.listadapter.ListCheckinRoomAdapter;
import com.ihp.frontoffice.viewmodel.RoomViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListInventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListInventoryFragment extends Fragment {

    @BindView(R.id.room_list_recyclerview)
    RecyclerView roomRecyclerView;

    @BindView(R.id.room_progressbar)
    MKLoader progressBar;

    @BindView(R.id.search_room_by_code)
    SearchView searchView;



    private ListCheckinRoomAdapter roomAdapter;
    private RoomViewModel roomViewModel;
    ArrayList<Room> roomArrayList = new ArrayList<>();
    private String keyword="";
    private static String BASE_URL;

    public ListInventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListInventoryFragment.
     */

    public static ListInventoryFragment newInstance(String param1, String param2) {
        ListInventoryFragment fragment = new ListInventoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);
        ButterKnife.bind(this, view);



/*        TransitionInflater inflateAnim = TransitionInflater.from(requireContext());
        setEnterTransition(inflateAnim.inflateTransition(R.transition.fade));*/


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        setMainTitle();
        progressBar.setVisibility(View.VISIBLE);

        searchView.setQueryHint("Kode Room");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               /* if(query.length()>2){
                    cariData=query;
                }else {
                    cariData="";

                }*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String charArr) {
                //progressBar.setVisibility(View.VISIBLE);

                keyword = charArr;
                if (keyword.length() > 1) {
                    checkinRoomSetupData();
                } else {
                    keyword = "";
                }

                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                keyword = "";
                checkinRoomSetupData();
                return false;
            }
        });

        roomViewModel = new ViewModelProvider(requireActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);

        setupRoomRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkinRoomSetupData();

        roomAdapter = new ListCheckinRoomAdapter(getContext(), roomArrayList);
        roomRecyclerView.setAdapter(roomAdapter);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1));

        roomAdapter.notifyDataSetChanged();

    }

    private void checkinRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        refreshRecyclerView();
        roomViewModel.getRoomCheckin(keyword).observe(getViewLifecycleOwner(), roomResponse -> {
            roomResponse.displayMessage(getContext());
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                roomAdapter.notifyDataSetChanged();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void setupRoomRecyclerView() {
        if (roomAdapter == null) {
            roomAdapter = new ListCheckinRoomAdapter(getContext(), roomArrayList);
            roomRecyclerView.setAdapter(roomAdapter);
            roomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        } else {
            roomAdapter.notifyDataSetChanged();
        }
    }

    private void refreshRecyclerView() {
        roomArrayList.clear();
        roomAdapter.notifyDataSetChanged();
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Order Inventory"));
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void refreshPage(EventsWrapper.RefreshCurrentFragment refreshCurrentFragment) {
        checkinRoomSetupData();
    }


}