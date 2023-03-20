package com.ihp.frontoffice.view.fragment.operasional.info;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihp.frontoffice.helper.DataUtils;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.MemberClient;
import com.ihp.frontoffice.data.remote.respons.MemberResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.RoomState;
import com.ihp.frontoffice.view.component.BasePagination;
import com.ihp.frontoffice.view.listadapter.ListOperasionalCheckinRoomAdapter;
import com.ihp.frontoffice.viewmodel.RoomOrderViewModel;
import com.ihp.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OperasionalListRoomToEditInfoFragment extends Fragment {

    @BindView(R.id.room_list_recyclerview)
    RecyclerView roomRecyclerView;

    @BindView(R.id.room_progressbar)
    MKLoader progressBar;

    @BindView(R.id.search_room_by_code)
    SearchView searchView;

    @BindView(R.id.bttn_previous)
    ImageButton buttonPrevious;

    @BindView(R.id.bttn_next)
    ImageButton buttonNext;

    //pagination
    private BasePagination p;
    private int totalPages;
    private int currentPage = 0;


    private String cariData = "";
    private ListOperasionalCheckinRoomAdapter roomAdapter;
    private ArrayList<Room> roomArrayList = new ArrayList<>();
    private static String BASE_URL;
    private MemberClient memberClient;
    private RoomViewModel roomViewModel;
    private RoomOrderViewModel roomOrderViewModel;
    private RoomOrder roomOrder;

    public OperasionalListRoomToEditInfoFragment() {
        // Required empty public constructor
    }


    public static OperasionalListRoomToEditInfoFragment newInstance() {
        OperasionalListRoomToEditInfoFragment fragment = new OperasionalListRoomToEditInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_operasional_list_room_to_edit_info, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        roomViewModel = new ViewModelProvider(requireActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);

        roomOrderViewModel = new ViewModelProvider(requireActivity())
                .get(RoomOrderViewModel.class);
        roomOrderViewModel.init(BASE_URL);

        memberClient = ApiRestService.getClient(BASE_URL).create(MemberClient.class);
        searchView.setQueryHint("Kode Room");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                //progressBar.setVisibility(View.VISIBLE);

                cariData = keyword;
                if (keyword.length() > 1) {
                    checkinRoomSetupData();
                } else {
                    cariData = "";
                }


                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cariData = "";
                checkinRoomSetupData();
                return false;
            }
        });

        //checkinRoomSetupData();

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage += 1;
                bindData(currentPage);
                toggleButtons();
            }
        });
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage -= 1;
                bindData(currentPage);
                toggleButtons();
            }
        });

    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Tambah Info"));
    }

    private void checkinRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel.getRoomCheckin(cariData).observe(getViewLifecycleOwner(), roomResponse -> {
            roomResponse.displayMessage(requireActivity());
            roomArrayList.clear();
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                ArrayList<Room> filterListRoomCheckin = (ArrayList<Room>) listRoom.stream()
                        .filter(room -> room.getRoomState() == RoomState.CHECKIN.getState())
                        .collect(Collectors.toList());

                if (filterListRoomCheckin.size() < 1) {
                    Toasty.info(requireActivity(), "Data Kosong").show();
                    roomAdapter.notifyDataSetChanged();
                    //return;
                }
                progressBar.setVisibility(View.GONE);
                roomArrayList.addAll(filterListRoomCheckin);
                bindData(currentPage);


            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void bindData(int page) {
        p = new BasePagination(roomArrayList);
        p.setItemsPerPage(9);
        totalPages = p.getTotalPages();

        //filterBilled
        DataUtils dataUtils = new DataUtils();
        ArrayList<Room> filteredData = dataUtils.filterBilled(p.getCurrentData(page));
        roomAdapter = new ListOperasionalCheckinRoomAdapter(requireActivity(), filteredData);

//        roomAdapter = new ListOperasionalCheckinRoomAdapter(requireActivity(), p.getCurrentData(page));
        roomRecyclerView.setAdapter(roomAdapter);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
        roomAdapter.notifyDataSetChanged();
        toggleButtons();
    }

    private void toggleButtons() {
        //SINGLE PAGE DATA
        Log.i("PAGING", "totalPages = " + totalPages);
        Log.i("PAGING", "currentPage = " + currentPage);
        if (totalPages <= 1) {
           /* buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(false);*/
            buttonNext.setVisibility(View.GONE);
            buttonPrevious.setVisibility(View.GONE);
        }
        //LAST PAGE
        else if ((totalPages - currentPage) == 1) {
            buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(true);
        }
        //FIRST PAGE
        else if (currentPage == 0) {
            buttonPrevious.setEnabled(false);
            buttonNext.setEnabled(true);
        }
        //SOMEWHERE IN BETWEEN
        else if (currentPage >= 1 && (totalPages - currentPage) == 1) {
            buttonNext.setEnabled(true);
            buttonPrevious.setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        roomAdapter = new ListOperasionalCheckinRoomAdapter(requireActivity(), null);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
        roomRecyclerView.setAdapter(roomAdapter);
        checkinRoomSetupData();
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

    @Subscribe
    public void operasionalCheckinRoom(EventsWrapper.OperasionalBusCheckinRoom operasionalBusCheckinRoom) {
        getRoomOrderDetail(operasionalBusCheckinRoom.getRoom());
    }

    private void getRoomOrderDetail(Room room){
        roomOrder = new RoomOrder();
        progressBar.setVisibility(View.VISIBLE);
        roomOrderViewModel
                .getRoomOrderResponseMutableLiveData(room.getRoomCode())
                .observe(getViewLifecycleOwner(), roomOrderResponse -> {
                    roomOrderResponse.displayMessage(requireActivity());
                    if (roomOrderResponse.isOkay()) {
                        roomOrder = roomOrderResponse.getRoomOrder();
                        getMemberCheckinRoom(roomOrder);
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void getMemberCheckinRoom(RoomOrder roomOrder) {
        Call<MemberResponse> call = memberClient.checkMember(roomOrder.getCheckinRoom().getMemberCode());
        call.enqueue(new Callback<MemberResponse>() {
            @Override
            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                progressBar.setVisibility(View.GONE);
                MemberResponse res = response.body();
                res.displayMessage(requireActivity());
                if (!res.isOkay()) {
                    return;
                }
                roomOrder.setMember(res.getMember());
                navigateToAddInfo(roomOrder);
            }

            @Override
            public void onFailure(Call<MemberResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void navigateToAddInfo(RoomOrder roomOrder) {
        Navigation
                .findNavController(this.getView())
                .navigate(
                        OperasionalListRoomToEditInfoFragmentDirections
                                .actionNavOperasionalListRoomToEditInfoFragmentToNavOperasionalCheckinEditInfoFragment(roomOrder)
                );
    }
}