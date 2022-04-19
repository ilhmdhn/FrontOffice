package livs.code.frontoffice.view.fragment.operasional.checkin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Member;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.entity.RoomType;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.RoomOrderClient;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.helper.RoomState;
import livs.code.frontoffice.view.component.BasePagination;
import livs.code.frontoffice.view.listadapter.ListOperasionalReadyRoomTypeAdapter;
import livs.code.frontoffice.view.listadapter.ListOperasionalWaitingRoomAdapter;
import livs.code.frontoffice.viewmodel.RoomTypeViewModel;
import livs.code.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalListRoomTypeToCheckinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalListRoomTypeToCheckinFragment extends Fragment {

    @BindView(R.id.image_member)
    ImageView memberFoto;

    @BindView(R.id.name_member)
    TextView memberName;

    @BindView(R.id.phone_member)
    TextView memberPhone;

    @BindView(R.id.checkin_poin_member)
    TextView memberPoin;

    @BindView(R.id.ready_room_recyclerview)
    RecyclerView readyRoomTypeRecyclerView;

    @BindView(R.id.bttn_previous)
    ImageButton buttonPrevious;

    @BindView(R.id.bttn_next)
    ImageButton buttonNext;

    @BindView(R.id.togglegroup_info_room)
    MaterialButtonToggleGroup materialButtonToggleGroup;

    @BindView(R.id.room_checkin_recyclerview)
    RecyclerView waitingRoomInfoRecyclerView;


    @BindView(R.id.progressbar)
    MKLoader progressBar;


    private RoomTypeViewModel roomTypeViewModel;
    private RoomViewModel roomViewModel;
    private Member member;
    private ArrayList<RoomType> roomTypeArrayList = new ArrayList<>();
    private ListOperasionalReadyRoomTypeAdapter listOperasionalReadyRoomTypeAdapter;
    private ListOperasionalWaitingRoomAdapter listOperasionalWaitingRoomAdapter;
    private ArrayList<Room> waitRoomArrayList = new ArrayList<>();



    private RoomOrderClient roomOrderClient;
    private String TAG = "OperasionalListRoomTypeToCheckinFragment";
    private static String BASE_URL;
    private User USER_FO;

    //pagination
    private BasePagination p;
    private int totalPages;
    private int currentPage = 0;

    public OperasionalListRoomTypeToCheckinFragment() {
    }
    // Required empty public constructor

    public static OperasionalListRoomTypeToCheckinFragment newInstance() {
        OperasionalListRoomTypeToCheckinFragment fragment = new OperasionalListRoomTypeToCheckinFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        member = OperasionalListRoomTypeToCheckinFragmentArgs.fromBundle(getArguments()).getMember();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_operasional_checkin_available_room_type, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();
        setMainTitle();
        roomTypeViewModel = new ViewModelProvider(getActivity())
                .get(RoomTypeViewModel.class);
        roomTypeViewModel.init(BASE_URL);

        roomViewModel = new ViewModelProvider(getActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);
        setDataMember();
        setDataReadyRoomType();
        setDataWaitingInfoRoomCheckin();
        setDataWaitingInfoRoomClean();

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

        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                Log.i(TAG, "onButtonChecked: ");
                if (!isChecked) {
                    return;
                }

                        /* MaterialButton button = group.findViewById(checkedId);
                        button.setBackground(R.drawable.background_shape_transparance_black);*/

                switch (checkedId) {
                    case R.id.toogle_room_chekin:

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                setDataWaitingInfoRoomCheckin();

                            }
                        }, 100);

                        break;
                    case R.id.toogle_room_clean:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setDataWaitingInfoRoomClean();
                            }
                        }, 100);
                        break;
                    default:
                }


            }
        });
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
    }


    @Override
    public void onResume() {
        super.onResume();
        listOperasionalReadyRoomTypeAdapter = new ListOperasionalReadyRoomTypeAdapter(getContext(), roomTypeArrayList);
        readyRoomTypeRecyclerView.setAdapter(listOperasionalReadyRoomTypeAdapter);
        readyRoomTypeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        listOperasionalWaitingRoomAdapter = new ListOperasionalWaitingRoomAdapter(getContext(), waitRoomArrayList);
        waitingRoomInfoRecyclerView.setAdapter(listOperasionalWaitingRoomAdapter);
        waitingRoomInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        setDataMember();
        setDataReadyRoomType();
        setDataWaitingInfoRoomCheckin();
        setDataWaitingInfoRoomClean();
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Pilih Type Room"));
    }

    private void setDataWaitingInfoRoomClean() {
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel.getRoomCheckout("").observe(getActivity(), roomResponse -> {
            progressBar.setVisibility(View.GONE);
            roomResponse.displayMessage(getContext());
            if (roomResponse.isOkay()) {
                waitRoomArrayList.clear();
                List<Room> listRoom = roomResponse.getRooms();
                ArrayList<Room> filterListRoomCheckin = (ArrayList<Room>) listRoom.stream()
                        .filter(room ->room.isRoomNotLobby())
                        .collect(Collectors.toList());
                waitRoomArrayList.addAll(filterListRoomCheckin);
                setupWaitingInfoRoomTypeRecyclerView();
            }
        });
    }

    private void setDataWaitingInfoRoomCheckin() {
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel.getRoomCheckin("").observe(getActivity(), roomResponse -> {
            progressBar.setVisibility(View.GONE);
            roomResponse.displayMessage(getContext());
            if (roomResponse.isOkay()) {
                waitRoomArrayList.clear();
                List<Room> listRoom = roomResponse.getRooms();

                ArrayList<Room> filterListRoomCheckin = (ArrayList<Room>) listRoom.stream()
                        .filter(room ->room.isRoomNotLobby())
                        .collect(Collectors.toList());
                waitRoomArrayList.addAll(filterListRoomCheckin);
                setupWaitingInfoRoomTypeRecyclerView();
            }
        });
    }

    private void setupWaitingInfoRoomTypeRecyclerView() {
        if (listOperasionalWaitingRoomAdapter == null) {
            listOperasionalWaitingRoomAdapter = new ListOperasionalWaitingRoomAdapter(getContext(), waitRoomArrayList);
            waitingRoomInfoRecyclerView.setAdapter(listOperasionalWaitingRoomAdapter);
            waitingRoomInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        }
        listOperasionalWaitingRoomAdapter.notifyDataSetChanged();

    }

    private void setDataReadyRoomType() {
        progressBar.setVisibility(View.VISIBLE);
        roomTypeViewModel
                .getGroupingRoomTypeReadyLiveData()
                .observe(getActivity(), roomTypeResponse -> {
                    progressBar.setVisibility(View.GONE);
                    roomTypeResponse.displayMessage(getContext());
                    if (roomTypeResponse.isOkay()) {
                        roomTypeArrayList.clear();
                        List<RoomType> listRoom = roomTypeResponse.getRoomTypes();
                        roomTypeArrayList.addAll(listRoom);
                        bindData(currentPage);
                    }
                });
    }

    private void bindData(int page) {
        p = new BasePagination(roomTypeArrayList);
        p.setItemsPerPage(6);
        totalPages = p.getTotalPages();
        listOperasionalReadyRoomTypeAdapter = new ListOperasionalReadyRoomTypeAdapter(getContext(), p.getCurrentData(page));
        readyRoomTypeRecyclerView.setAdapter(listOperasionalReadyRoomTypeAdapter);
        readyRoomTypeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        listOperasionalReadyRoomTypeAdapter.notifyDataSetChanged();
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

    private void setDataMember() {
        Glide.with(getContext())
                .load(member.getFotoPathNode())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.user)
                .skipMemoryCache(true)
                .into(memberFoto);
        memberName.setText(member.getFullName());
        memberPhone.setText(member.getHp());
        memberPoin.setText(" Poin " + String.valueOf(member.getPointReward()));
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
    public void operasionalCheckinRoomType(EventsWrapper.OperasionalBusRoomType operasionalCheckinRoomType) {


        RoomType roomType = operasionalCheckinRoomType.getRoomType();
        RoomOrder roomOrder = new RoomOrder();
        roomOrder.setMember(member);
        roomOrder.setCheckinRoomType(roomType);
        roomOrder.setChuser(USER_FO.getUserId());

        Navigation
                .findNavController(getView())
                .navigate(
                        OperasionalListRoomTypeToCheckinFragmentDirections
                                .actionNavOperasionalListRoomTypeToCheckinFragmentToNavOperasionalCheckinAvailableRoomFragment(roomOrder));



    }

    private void submitCheckin(RoomOrder roomOrder) {

        Call<RoomOrderResponse> call = roomOrderClient.submitOrderRoomMemberCheckin(roomOrder);
        call.enqueue(new Callback<RoomOrderResponse>() {
            @Override
            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                progressBar.setVisibility(View.GONE);
                RoomOrderResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    return;
                }
                Navigation
                        .findNavController(getView())
                        .navigate(
                                OperasionalListRoomTypeToCheckinFragmentDirections
                                        .actionNavOperasionalListRoomTypeToCheckinFragmentToNavOperasionalCheckinAddInfoFragment(res.getRoomOrder())
                        );
            }

            @Override
            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                Toasty.error(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT)
                        .show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Subscribe
    public void refreshPage(EventsWrapper.RefreshCurrentFragment refreshCurrentFragment) {
        setDataReadyRoomType();
        setDataWaitingInfoRoomCheckin();
    }


}