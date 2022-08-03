package com.ihp.frontoffice.view.fragment.status;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomType;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.view.listadapter.ListRoomStatusAdapter;
import com.ihp.frontoffice.viewmodel.RoomTypeViewModel;
import com.ihp.frontoffice.viewmodel.RoomViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomStatusFragment extends Fragment {


    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.room_list_recyclerview)
    RecyclerView roomCinRecyclerView;

    @BindView(R.id.room_progressbar)
    MKLoader progressBar;

    @BindView(R.id.search_room_by_code)
    SearchView searchView;

    @BindView(R.id.btn_pr)
    AppCompatButton btnFilterPr;

    @BindView(R.id.btn_room)
    AppCompatButton btnFilterRoom;

    @BindView(R.id.btn_sofa)
    AppCompatButton btnFilterSofa;

    @BindView(R.id.btn_bar)
    AppCompatButton btnFilterBar;

    @BindView(R.id.btn_clear_filter)
    AppCompatButton btnFilterClean;

    private ListRoomStatusAdapter roomCinAdapter;
    private ArrayList<Room> roomArrayList = new ArrayList<>();
    private RoomViewModel roomViewModel;
    private RoomTypeViewModel roomTypeViewModel;
    private ArrayList<RoomType> roomTypeArrayList = new ArrayList<>();
    private String roomCodeParam, roomTypeParam, roomCapacityParam;
    private static String BASE_URL;


    public RoomStatusFragment() {
        // Required empty public constructor
    }


    public static RoomStatusFragment newInstance() {
        RoomStatusFragment fragment = new RoomStatusFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_status, container, false);
        ButterKnife.bind(this, view);
/*        TransitionInflater inflateAnim = TransitionInflater.from(requireContext());
        setEnterTransition(inflateAnim.inflateTransition(R.transition.fade));*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
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
            public boolean onQueryTextChange(String keyword) {
                //progressBar.setVisibility(View.VISIBLE);
                btnFilterClean.setVisibility(View.GONE);
                roomTypeParam = "";
                roomCodeParam = "";
                if (keyword.length() == 0) {
                    roomCodeParam = "";
                }

                if (keyword.length() > 1) {
                    roomCodeParam = keyword;
                }
                allRoomCheckinSetupData();

                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                roomCodeParam = "";
                allRoomCheckinSetupData();
                return false;
            }
        });


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                allRoomCheckinSetupData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        roomViewModel = new ViewModelProvider(getActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);
        roomTypeViewModel = new ViewModelProvider(getActivity())
                .get(RoomTypeViewModel.class);
        roomTypeViewModel.init(BASE_URL);

        setupRoomRecyclerView();
        allRoomCheckinSetupData();

        btnFilterPr.setOnClickListener(view -> {
            roomTypeParam = "PR";
            btnFilterClean.setText("PR");
            btnFilterClean.setVisibility(View.VISIBLE);
            allRoomCheckinSetupData();
        });
        btnFilterRoom.setOnClickListener(view -> {
            roomTypeParam = "ROOM";
            btnFilterClean.setText("ROOM");
            btnFilterClean.setVisibility(View.VISIBLE);
            allRoomCheckinSetupData();
        });
        btnFilterSofa.setOnClickListener(view -> {
            roomTypeParam = "SOFA";
            btnFilterClean.setText("SOFA");
            btnFilterClean.setVisibility(View.VISIBLE);
            allRoomCheckinSetupData();
        });
        btnFilterBar.setOnClickListener(view -> {
            roomTypeParam = "BAR";
            btnFilterClean.setText("BAR");
            btnFilterClean.setVisibility(View.VISIBLE);
            allRoomCheckinSetupData();
        });
        btnFilterClean.setOnClickListener(view -> {
            btnFilterClean.setVisibility(View.GONE);
            roomTypeParam = "";
            roomCodeParam = "";
            allRoomCheckinSetupData();
        });
    }


    private void refreshRecyclerView() {
        roomArrayList.clear();
        roomCinAdapter.notifyDataSetChanged();
    }

    private void setupRoomRecyclerView() {
        if (roomCinAdapter == null) {
            roomCinAdapter = new ListRoomStatusAdapter(getContext(), roomArrayList);
            roomCinRecyclerView.setAdapter(roomCinAdapter);
            roomCinRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        } else {
            roomCinAdapter.notifyDataSetChanged();
        }
    }

    private void allRoomCheckinSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        roomArrayList.clear();
        refreshRecyclerView();
        roomViewModel
                .getRoomCheckinByType(roomCodeParam, roomCodeParam, roomTypeParam, roomCapacityParam)
                .observe(getActivity(), roomResponse -> {
                    roomResponse.displayMessage(getContext());
                    swipeContainer.setRefreshing(false);
                    if (roomResponse.isOkay()) {
                        roomArrayList.clear();
                        List<Room> listRoom = roomResponse.getRooms();
                        roomArrayList.addAll(listRoom);
                        reloadRecyclerView();
                    }

                    progressBar.setVisibility(View.GONE);
                });
    }

    private void reloadRecyclerView() {
        roomCinAdapter = new ListRoomStatusAdapter(getContext(), roomArrayList);
        roomCinRecyclerView.setAdapter(roomCinAdapter);
        roomCinRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        roomCinAdapter.notifyDataSetChanged();
    }


    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Monitoring"));
    }

    @Subscribe
    public void refreshPage(EventsWrapper.RefreshCurrentFragment refreshCurrentFragment) {
        btnFilterClean.setVisibility(View.GONE);
        roomTypeParam = "";
        roomCodeParam = "";
        allRoomCheckinSetupData();
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

   /* @Override
    public void onResume() {
        super.onResume();
        setupRoomRecyclerView();
        roomTypeSetupData();
    }*/
}