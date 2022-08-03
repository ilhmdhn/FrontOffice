package com.ihp.frontoffice.view.fragment.operasional.transfer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Member;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.entity.RoomType;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.RoomOrderClient;
import com.ihp.frontoffice.data.remote.MemberClient;
import com.ihp.frontoffice.data.remote.respons.MemberResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.view.component.BasePagination;
import com.ihp.frontoffice.view.listadapter.ListOperasionalReadyRoomTypeAdapter;
import com.ihp.frontoffice.viewmodel.RoomTypeViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalListRoomTypeToTransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalListRoomTypeToTransferFragment extends Fragment {

    @BindView(R.id.image_member)
    ImageView memberFoto;

    @BindView(R.id.name_member)
    TextView memberName;

    @BindView(R.id.phone_member)
    TextView memberPhone;

    @BindView(R.id.checkin_poin_member)
    TextView memberPoin;

    @BindView(R.id.input_alasan_transfer)
    RadioGroup inputReasonGroup;

    @BindView(R.id.ready_room_recyclerview)
    RecyclerView readyRoomTypeRecyclerView;

    @BindView(R.id.bttn_previous)
    ImageButton buttonPrevious;

    @BindView(R.id.bttn_next)
    ImageButton buttonNext;

    @BindView(R.id.progressbar)
    MKLoader progressBar;


    private RoomTypeViewModel roomTypeViewModel;
    private MemberClient memberClient;
    private Room oldRoom;

    private ArrayList<RoomType> roomTypeArrayList = new ArrayList<>();
    private ListOperasionalReadyRoomTypeAdapter listOperasionalReadyRoomTypeAdapter;

    private String selectedReason = "OVERPAX";

    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater dialogInflater;
    private View dialogView;
    private Member member;
    private TextView descTransfer, residualTime, reasonTransfer;
    private AppCompatButton buttonChooseRoom, buttonCancel;
    private RoomOrderClient roomOrderClient;
    private static String BASE_URL;
    private User USER_FO;
    //pagination
    private BasePagination p;
    private int totalPages;
    private int currentPage = 0;
    public OperasionalListRoomTypeToTransferFragment() {
        // Required empty public constructor
    }


    public static OperasionalListRoomTypeToTransferFragment newInstance() {
        OperasionalListRoomTypeToTransferFragment fragment = new OperasionalListRoomTypeToTransferFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldRoom = OperasionalListRoomTypeToTransferFragmentArgs.fromBundle(getArguments()).getRoom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_operasional_transfer_available_room_type, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();
        roomTypeViewModel = new ViewModelProvider(getActivity())
                .get(RoomTypeViewModel.class);
        roomTypeViewModel.init(BASE_URL);

        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
        memberClient = ApiRestService.getClient(BASE_URL).create(MemberClient.class);

        //NAVIGATE
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
        getMemberCheckin(oldRoom.getMemberCode());

        inputReasonGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.transfer_overpax) {
                selectedReason = "OVERPAX";
            }
            if (i == R.id.transfer_request) {
                selectedReason = "PERMINTAAN TAMU";
            }
            if (i == R.id.transfer_vod_trouble) {
                selectedReason = "VOD BERMASALAH";
            }
        });

    }

    private void getMemberCheckin(String codeMbr) {
        progressBar.setVisibility(View.VISIBLE);
        Call<MemberResponse> call = memberClient.checkMember(codeMbr);
        call.enqueue(new Callback<MemberResponse>() {
            @Override
            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                progressBar.setVisibility(View.GONE);
                MemberResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    return;
                }
                member = res.getMember();
                setDataMember();
            }

            @Override
            public void onFailure(Call<MemberResponse> call, Throwable t) {

                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
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

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment(oldRoom.getRoomType() + " " + oldRoom.getRoomCode()));
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
                        ArrayList<RoomType> filterListRoomCheckin = (ArrayList<RoomType>) listRoom.stream()
                                .filter(data ->
                                        (!data.getRoomType().equals("SOFA")&&!data.getRoomType().equals("BAR"))
                                ).collect(Collectors.toList());
                        roomTypeArrayList.addAll(filterListRoomCheckin);
                        bindData(currentPage);
                    }
                });
    }

    private void bindData(int page) {
        p = new BasePagination(roomTypeArrayList);
        p.setItemsPerPage(4);
        totalPages = p.getTotalPages();
        listOperasionalReadyRoomTypeAdapter = new ListOperasionalReadyRoomTypeAdapter(getContext(), p.getCurrentData(page));
        readyRoomTypeRecyclerView.setAdapter(listOperasionalReadyRoomTypeAdapter);
        readyRoomTypeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        listOperasionalReadyRoomTypeAdapter.notifyDataSetChanged();
        toggleButtons();
    }

    private void toggleButtons() {

        if (totalPages <= 1) {
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
        listOperasionalReadyRoomTypeAdapter = new ListOperasionalReadyRoomTypeAdapter(getContext(), roomTypeArrayList);
        readyRoomTypeRecyclerView.setAdapter(listOperasionalReadyRoomTypeAdapter);
        readyRoomTypeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        setDataReadyRoomType();
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
    public void operasionalCheckinRoomType(EventsWrapper.OperasionalBusRoomType operasionalBusRoomType) {
        RoomType roomType = operasionalBusRoomType.getRoomType();
        RoomOrder roomOrder = new RoomOrder();
        roomOrder.setMember(member);
        roomOrder.setCheckinRoomType(roomType);
        roomOrder.setChuser(USER_FO.getUserId());
        roomOrder.setTransferReason(selectedReason);
        roomOrder.setOldRoomBeforeTransfer(oldRoom);

        //dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme);
        dialogInflater = this.getLayoutInflater();
        dialogView = dialogInflater.inflate(R.layout.dialog_operasional_transfer_duration, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("");

        descTransfer = dialogView.findViewById(R.id.label_checkin_room_type);
        residualTime = dialogView.findViewById(R.id.sisa_waktu);
        reasonTransfer = dialogView.findViewById(R.id.alasan_transfer);

        buttonChooseRoom = dialogView.findViewById(R.id.btn_choose_room);
        buttonCancel = dialogView.findViewById(R.id.bttn_cancel);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            reasonTransfer.setText("Alasan : " + roomOrder.getTransferReason());
            String infoTransfer =  oldRoom.getRoomType() + " " + oldRoom.getRoomCode();
            descTransfer.setText(infoTransfer);
            String infoTime = "Sisa waktu " + oldRoom.getRoomResidualCheckinHoursTime() + " Jam "
                    + oldRoom.getRoomResidualCheckinHoursMinutesTime() + " Menit";
            residualTime.setText(infoTime);

            buttonChooseRoom.setOnClickListener(view -> {
                alertDialog.dismiss();
                Navigation
                        .findNavController(getView())
                        .navigate(
                                OperasionalListRoomTypeToTransferFragmentDirections
                                        .actionNavOperasionalRoomTypeToTransferFragmentToNavOperasionalTransferAvailableRoomFragment(roomOrder));
            });

            buttonCancel.setOnClickListener(view -> {
                alertDialog.dismiss();
            });
        });

        alertDialog.show();
    }

    @Subscribe
    public void refreshPage(EventsWrapper.RefreshCurrentFragment refreshCurrentFragment) {
        setDataReadyRoomType();
    }

    private void navBack() {
        Navigation
                .findNavController(this.getView())
                .popBackStack();
    }
}