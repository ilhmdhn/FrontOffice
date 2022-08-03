package com.ihp.frontoffice.view.fragment.operasional.checkin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Member;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.entity.RoomType;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.RoomOrderClient;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.view.component.BasePagination;
import com.ihp.frontoffice.view.listadapter.ListOperasionalReadyRoomAdapter;
import com.ihp.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalListRoomAvailableCheckinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalListRoomAvailableCheckinFragment extends Fragment {
    @BindView(R.id.image_member)
    ImageView memberFoto;

    @BindView(R.id.name_member)
    TextView memberName;

    @BindView(R.id.phone_member)
    TextView memberPhone;

    @BindView(R.id.checkin_poin_member)
    TextView memberPoin;

    @BindView(R.id.ready_room_recyclerview)
    RecyclerView readyRoomRecyclerView;

    @BindView(R.id.bttn_previous)
    ImageButton buttonPrevious;

    @BindView(R.id.bttn_next)
    ImageButton buttonNext;

    @BindView(R.id.progressbar)
    MKLoader progressBar;

    private RoomViewModel roomViewModel;
    private ListOperasionalReadyRoomAdapter roomAdapter;
    private Member member;
    private RoomType roomType;
    private RoomOrder roomOrder;
    private ArrayList<Room> roomArrayList = new ArrayList<>();
    private RoomOrderClient roomOrderClient;
    private static String BASE_URL;
    //pagination
    private BasePagination p;
    private int totalPages;
    private int currentPage = 0;

    private TextView typeRoom, countHours;
    private ImageView increment, decrement;
    private AppCompatButton  buttonCheckin, buttonCancel;


    private int jamCheckin;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater dialogInflater;
    private View dialogView;
    private TextView label;


    public OperasionalListRoomAvailableCheckinFragment() {
        // Required empty public constructor
    }


    public static OperasionalListRoomAvailableCheckinFragment newInstance() {
        OperasionalListRoomAvailableCheckinFragment fragment = new OperasionalListRoomAvailableCheckinFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomOrder = OperasionalListRoomAvailableCheckinFragmentArgs.fromBundle(getArguments()).getRoomOrder();
        member = roomOrder.getMember();
        roomType = roomOrder.getCheckinRoomType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operasional_checkin_available_room, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        setMainTitle();
        init();
    }

    private void init() {
        roomViewModel = new ViewModelProvider(getActivity())
                .get(RoomViewModel.class);

        buttonPrevious.setEnabled(false);

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
        setDataMember();
        readyRoomSetupData();
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment(roomType.getRoomType()));
    }

    private void setDataMember() {
        Glide.with(getContext())
                .load(member.getFotoPathNode())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(memberFoto);
        memberName.setText(member.getFullName());
        memberPhone.setText(member.getHp());
        memberPoin.setText("Poin " + String.valueOf(member.getPointReward()));
    }

    private void readyRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel.getRoomReadyByTypeGrouping(roomType).observe(getActivity(), roomResponse -> {
            progressBar.setVisibility(View.GONE);
            roomResponse.displayMessage(getContext());
            if (roomResponse.isOkay()) {
                roomArrayList.clear();
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                bindData(currentPage);
            }
        });
    }

    private void bindData(int page) {
        p = new BasePagination(roomArrayList);
        p.setItemsPerPage(9);
        totalPages = p.getTotalPages();
        roomAdapter = new ListOperasionalReadyRoomAdapter(getContext(), p.getCurrentData(page));
        readyRoomRecyclerView.setAdapter(roomAdapter);
        readyRoomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
    public void operasionalCheckinRoom(EventsWrapper.OperasionalBusCheckinRoom operasionalBusCheckinRoom) {
        Room room = operasionalBusCheckinRoom.getRoom();
        roomOrder.setCheckinRoom(room);
        jamCheckin = 0;

        dialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme);
        dialogInflater = this.getLayoutInflater();
        dialogView = dialogInflater.inflate(R.layout.dialog_operasional_checkin_duration, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("");

        typeRoom = dialogView.findViewById(R.id.label_checkin_room_type);
        typeRoom.setText(room.getRoomCode());
        label = dialogView.findViewById(R.id.label_info);


        countHours = dialogView.findViewById(R.id.checkin_count_hours);
        countHours.setText(String.valueOf(jamCheckin));

        increment = dialogView.findViewById(R.id.checkin_duration_increment);
        decrement = dialogView.findViewById(R.id.checkin_duration_decrement);

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jamCheckin == 0) {
                    countHours.setText(String.valueOf(jamCheckin));
                }

                if (jamCheckin > 0) {
                    jamCheckin = jamCheckin - 1;
                    countHours.setText(String.valueOf(jamCheckin));
                }
            }
        });
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jamCheckin == 24) {
                    countHours.setText(String.valueOf(jamCheckin));
                }

                if (jamCheckin < 24) {
                    jamCheckin = jamCheckin + 1;
                    countHours.setText(String.valueOf(jamCheckin));
                }

            }
        });

        if(!room.isRoomNotLobby()){
            countHours.setVisibility(View.GONE);
            increment.setVisibility(View.GONE);
            decrement.setVisibility(View.GONE);
            label.setText("Transaksi F&B");
        }else{
            jamCheckin = 2;
            countHours.setText(String.valueOf(jamCheckin));
        }

        buttonCheckin = dialogView.findViewById(R.id.btn_chekin);
        buttonCancel = dialogView.findViewById(R.id.bttn_cancel);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(dialogInterface -> {

            buttonCheckin.setOnClickListener(view -> {
                progressBar.setVisibility(View.VISIBLE);

                if(room.isRoomNotLobby()){
                    if (jamCheckin < 1) {
                        Toasty.warning(getContext(), "Durasi jam belum sesuai")
                                .show();
                        return;
                    }
                    alertDialog.dismiss();
                    roomOrder.setDurasiJam(jamCheckin);
                    submitCheckinRoom(roomOrder);
                }else{
                    alertDialog.dismiss();
                    roomOrder.setDurasiJam(jamCheckin);
                    submitCheckinLobby(roomOrder);
                }

            });


            buttonCancel.setOnClickListener(view -> {
                alertDialog.dismiss();
            });
        });

        alertDialog.show();

    }

    private void submitCheckinLobby(RoomOrder roomOrder) {
        progressBar.setVisibility(View.VISIBLE);
        Call<RoomOrderResponse> call = roomOrderClient.submitOrderLobbyMemberCheckin(roomOrder);

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
                                OperasionalListRoomAvailableCheckinFragmentDirections
                                        .actionNavOperasionalCheckinAvailableRoomFragmentToNavOperasionalCheckinAddInfoFragment(res.getRoomOrder())
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

    private void submitCheckinRoom(RoomOrder roomOrder) {
        progressBar.setVisibility(View.VISIBLE);
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
                                OperasionalListRoomAvailableCheckinFragmentDirections
                                .actionNavOperasionalCheckinAvailableRoomFragmentToNavOperasionalCheckinAddInfoFragment(res.getRoomOrder())
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
        readyRoomSetupData();
    }
}