package com.ihp.frontoffice.view.fragment.operasional.transfer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ihp.frontoffice.data.remote.respons.TransferRoomResponse;
import com.ihp.frontoffice.helper.Printer;
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
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.RoomOrderClient;
import com.ihp.frontoffice.data.remote.UserClient;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.data.remote.respons.UserResponse;
import com.ihp.frontoffice.data.repository.IhpRepository;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.UserAuthRole;
import com.ihp.frontoffice.view.component.BasePagination;
import com.ihp.frontoffice.view.listadapter.ListOperasionalReadyRoomAdapter;
import com.ihp.frontoffice.viewmodel.OtherViewModel;
import com.ihp.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalListRoomAvailableTransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalListRoomAvailableTransferFragment extends Fragment {
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
    private Room oldRoomBeforeTransfer;
    private ArrayList<Room> roomArrayList = new ArrayList<>();
    private RoomOrderClient roomOrderClient;
    private static String BASE_URL;
    private User USER_FO;
    private IhpRepository ihpRepository;
    private OtherViewModel otherViewModel;
    Printer printer;

    //pagination
    private BasePagination p;
    private int totalPages;
    private int currentPage = 0;
    private int durasiJamtransfer;

    public OperasionalListRoomAvailableTransferFragment() {
        // Required empty public constructor
    }


    public static OperasionalListRoomAvailableTransferFragment newInstance() {
        OperasionalListRoomAvailableTransferFragment fragment = new OperasionalListRoomAvailableTransferFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomOrder = OperasionalListRoomAvailableTransferFragmentArgs.fromBundle(getArguments()).getRoomOrder();
        member = roomOrder.getMember();
        roomType = roomOrder.getCheckinRoomType();
        oldRoomBeforeTransfer = roomOrder.getOldRoomBeforeTransfer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operasional_transfer_available_room, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();
        otherViewModel = new ViewModelProvider(getActivity()).get(OtherViewModel.class);
        printer = new Printer();
        init();
    }

    private void init() {
        roomViewModel = new ViewModelProvider(getActivity())
                .get(RoomViewModel.class);
        setDataMember();
        ihpRepository = new IhpRepository();
        readyRoomSetupData();
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);

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
                        .TitleFragment(oldRoomBeforeTransfer.getRoomCode()));
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


    @SuppressLint("SetTextI18n")
    @Subscribe
    public void otorisasiOperasionalTransferRoom(EventsWrapper.OperasionalBusCheckinRoom operasionalBusCheckinRoom) {
        Room room = operasionalBusCheckinRoom.getRoom();

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_otorisasi_transfer, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(oldRoomBeforeTransfer.getRoomCode());

        AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
        AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);

        View layoutTransferDuration = dialogView.findViewById(R.id.linearLayoutTransfer);
        ImageView buttonMinHours = dialogView.findViewById(R.id.transferMinHours);
        ImageView buttonPlusHours = dialogView.findViewById(R.id.transferPlusHours);
        TextView transferCountHours = dialogView.findViewById(R.id.transferCountHours);


        if(oldRoomBeforeTransfer.isRoomNotLobby()){
            layoutTransferDuration.setVisibility(View.GONE);
        }else{
            durasiJamtransfer = 0;
            layoutTransferDuration.setVisibility(View.VISIBLE);
            transferCountHours.setText(String.valueOf(durasiJamtransfer));
            buttonMinHours.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (durasiJamtransfer == 0) {
                        transferCountHours.setText(String.valueOf(durasiJamtransfer));
                    }

                    if (durasiJamtransfer > 0) {

                        durasiJamtransfer = durasiJamtransfer - 1;
                        transferCountHours.setText(String.valueOf(durasiJamtransfer));
                    }
                }
            });
            buttonPlusHours.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (durasiJamtransfer == 24) {
                        transferCountHours.setText(String.valueOf(durasiJamtransfer));
                    }

                    if (durasiJamtransfer < 24) {

                        durasiJamtransfer = durasiJamtransfer + 1;
                        transferCountHours.setText(String.valueOf(durasiJamtransfer));
                    }

                }
            });

        }

        TextView infoText = dialogView.findViewById(R.id.text_info_transfer);
        infoText.setText("Transfer ke " + room.getRoomCode());

        EditText _usernameTxt = dialogView.findViewById(R.id.input_username_otorisasi);
        EditText _passwordTxt = dialogView.findViewById(R.id.input_password_otorisasi);
        MKLoader _loginProgress = dialogView.findViewById(R.id.progress_dialog);

        otherViewModel.getJumlahApproval(BASE_URL, USER_FO.getUserId()).observe(getViewLifecycleOwner(), data->{
            boolean kasirApproval = data.getState();
            if (kasirApproval) {

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme);
                builder.setMessage(R.string.transfer_confirmation);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        roomOrder.setCheckinRoom(room);
                        submitTransferRoom(roomOrder);
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else{
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(dialogInterface -> {
                    buttonOk.setOnClickListener(view -> {
                        String email = _usernameTxt.getText().toString();
                        String password = _passwordTxt.getText().toString();
                        if (email.isEmpty() && password.isEmpty()) {
                            Toasty.warning(getContext(), "Anda belum input user dan password ", Toast.LENGTH_SHORT, true)
                                    .show();
                            return;
                        }

                        if(!oldRoomBeforeTransfer.isRoomNotLobby()){
                            if(durasiJamtransfer<1){
                                Toasty.warning(getContext(), "Anda belum isi durasi checkin ", Toast.LENGTH_SHORT, true)
                                        .show();
                                return;
                            }else{
                                roomOrder.setDurasiJam(durasiJamtransfer);
                            }
                        }

                        _loginProgress.setVisibility(View.VISIBLE);
                        UserClient userClient = ApiRestService.getClient(BASE_URL).create(UserClient.class);
                        Call<UserResponse> call = userClient.login(email, password);
                        call.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                UserResponse res = response.body();
                                _loginProgress.setVisibility(View.GONE);
                                res.displayMessage(getContext());
                                if (res.isOkay()) {
                                    User user = res.getUser();
                                    if (UserAuthRole.isAllowTransferRoom(user)) {
                                        roomOrder.setCheckinRoom(room);
                                        ihpRepository.submitApproval(BASE_URL, user.getUserId(), user.getLevelUser(), roomOrder.getCheckinRoom().getRoomCode(), "Transfer Room");
                                        submitTransferRoom(roomOrder);
                                        alertDialog.dismiss();
                                    } else {
                                        Toasty.warning(getContext(), "User tidak dapat melakukan operasi ini", Toast.LENGTH_SHORT, true)
                                                .show();
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                _loginProgress.setVisibility(View.GONE);
                                Toasty.error(getContext(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT, true)
                                        .show();
                            }
                        });

                    });

                    buttonCancel.setOnClickListener(view -> {
                        _loginProgress.setVisibility(View.GONE);
                        alertDialog.dismiss();
                    });
                });

                alertDialog.show();
            }
        });
    }

    private void submitTransferRoom(RoomOrder roomOrder) {
        progressBar.setVisibility(View.VISIBLE);
        Call<TransferRoomResponse> call;

        if(roomOrder.getOldRoomBeforeTransfer().isRoomNotLobby()){
            call = roomOrderClient.submitTransferRoomToRoom(roomOrder);
        }else{
            call = roomOrderClient.submitTransferLobbyToRoom(roomOrder);
        }

        call.enqueue(new Callback<TransferRoomResponse>() {
            @Override
            public void onResponse(Call<TransferRoomResponse> call, Response<TransferRoomResponse> response) {
                progressBar.setVisibility(View.GONE);
                TransferRoomResponse res = response.body();
                if (!res.getState()) {
                    Toasty.error(requireActivity(), res.getMessage(), Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                ihpRepository.submitApproval(BASE_URL, USER_FO.getUserId(), USER_FO.getLevelUser(), roomOrder.getCheckinRoom().getRoomCode(), "Transfer Room");

                //disini
                printCheckinSlip(res.getData().getKodeRcp());


               /* Navigation
                        .findNavController(getView())
                        .popBackStack();*/

            }

            @Override
            public void onFailure(Call<TransferRoomResponse> call, Throwable t) {
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

    private void printCheckinSlip(String rcp){
        otherViewModel.checkinSlip(BASE_URL, rcp).observe(getViewLifecycleOwner(), data->{
            printer.printerCheckinSlip(data, requireActivity());
            Navigation
                    .findNavController(getView())
                    .navigate(
                            OperasionalListRoomAvailableTransferFragmentDirections
                                    .actionNavOperasionalTransferAvailableRoomFragmentToNavOperasionalListRoomToTransferFragment()
                    );
        });
    }
}