package com.ihp.frontoffice.view.fragment.operasional.transfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.ihp.frontoffice.data.remote.TransferClient;
import com.ihp.frontoffice.data.remote.respons.TransferRoomResponse;
import com.ihp.frontoffice.helper.Printer;
import com.ihp.frontoffice.helper.Printer58;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private TransferClient transferClient;
    private static String BASE_URL;
    private User USER_FO;
    private IhpRepository ihpRepository;
    private OtherViewModel otherViewModel;

    //pagination
    private BasePagination p;
    private int totalPages;
    private int currentPage = 0;
    private int durasiJamtransfer;
    private Boolean isLobby = false;

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
//        setDataMember();
        roomType = roomOrder.getCheckinRoomType();
        oldRoomBeforeTransfer = roomOrder.getOldRoomBeforeTransfer();

        if(Objects.equals(oldRoomBeforeTransfer.getRoomType(), "LOBBY") ||
                Objects.equals(oldRoomBeforeTransfer.getRoomType(), "BAR") ||
                Objects.equals(oldRoomBeforeTransfer.getRoomType(), "SOFA") ||
                Objects.equals(oldRoomBeforeTransfer.getRoomType(), "LOUNGE") ||
                Objects.equals(oldRoomBeforeTransfer.getRoomType(), "RESTO")){
            isLobby = true;
        }

        Log.d("DEBUGGING old room type", oldRoomBeforeTransfer.getRoomType());

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
        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) requireActivity().getApplicationContext()).getUserFo();
        otherViewModel = new ViewModelProvider(requireActivity()).get(OtherViewModel.class);
        init();
    }

    private void init() {
        roomViewModel = new ViewModelProvider(requireActivity())
                .get(RoomViewModel.class);
        ihpRepository = new IhpRepository();
        readyRoomSetupData();
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
        transferClient = ApiRestService.getClient(BASE_URL).create(TransferClient.class);

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

    @SuppressLint("SetTextI18n")
    private void setDataMember() {
        if(member.getFotoPathNode() !=null){
            Glide.with(requireActivity())
                    .load(member.getFotoPathNode())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(memberFoto);
        }
        memberName.setText(member.getFullName()!= null ? member.getFullName() : "");
        memberPhone.setText(member.getHp() != null ? member.getHp() : "");
        memberPoin.setText("Poin " + String.valueOf(member.getPointReward()));
    }


    private void readyRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel.getRoomReadyByTypeGrouping(roomType).observe(requireActivity(), roomResponse -> {
            progressBar.setVisibility(View.GONE);
            roomResponse.displayMessage(requireActivity());
            if (roomResponse.isOkay()) {
                roomArrayList.clear();
                List<Room> listRoom = roomResponse.getRooms();

                if(isLobby){
                    Log.d("DEBUGGING ATAS", "");
                    roomArrayList.addAll(listRoom.stream().filter(data -> Objects.equals(data.getRoomType(), "LOBBY") || Objects.equals(data.getRoomType(), "BAR") || Objects.equals(data.getRoomType(), "SOFA") || Objects.equals(data.getRoomType(), "LOUNGE") || Objects.equals(data.getRoomType(), "RESTO")).collect(Collectors.toList()));
                }else{
                    Log.d("DEBUGGING BAWAH", "");
                    roomArrayList.addAll(listRoom);
                }
                bindData(currentPage);
            }
        });
    }

    private void bindData(int page) {
        p = new BasePagination(roomArrayList);
        p.setItemsPerPage(9);
        totalPages = p.getTotalPages();
        roomAdapter = new ListOperasionalReadyRoomAdapter(requireActivity(), p.getCurrentData(page));
        readyRoomRecyclerView.setAdapter(roomAdapter);
        readyRoomRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
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
            layoutTransferDuration.setVisibility(View.GONE);
            durasiJamtransfer = 0;
//            layoutTransferDuration.setVisibility(View.VISIBLE);
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
                            Toasty.warning(requireActivity(), "Anda belum input user dan password ", Toast.LENGTH_SHORT, true)
                                    .show();
                            return;
                        }

//                        if(!oldRoomBeforeTransfer.isRoomNotLobby()){
//                            if(durasiJamtransfer<1){
//                                Toasty.warning(requireActivity(), "Anda belum isi durasi checkin ", Toast.LENGTH_SHORT, true)
//                                        .show();
//                                return;
//                            }else{
//                                roomOrder.setDurasiJam(durasiJamtransfer);
//                            }
//                        }

                        _loginProgress.setVisibility(View.VISIBLE);
                        UserClient userClient = ApiRestService.getClient(BASE_URL).create(UserClient.class);
                        Call<UserResponse> call = userClient.login(email, password);
                        call.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                UserResponse res = response.body();
                                _loginProgress.setVisibility(View.GONE);
                                res.displayMessage(requireActivity());
                                if (res.isOkay()) {
                                    User user = res.getUser();
                                    if (UserAuthRole.isAllowTransferRoom(user)) {
                                        roomOrder.setCheckinRoom(room);
                                        ihpRepository.submitApproval(BASE_URL, user.getUserId(), user.getLevelUser(), roomOrder.getCheckinRoom().getRoomCode(), "Transfer Room");
                                        submitTransferRoom(roomOrder);
                                        alertDialog.dismiss();
                                    } else {
                                        Toasty.warning(requireActivity(), "User tidak dapat melakukan operasi ini.", Toast.LENGTH_SHORT, true)
                                                .show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                _loginProgress.setVisibility(View.GONE);
                                Toasty.error(requireActivity(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT, true)
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

        if(Objects.equals(roomOrder.getOldRoomBeforeTransfer().getRoomType(), "LOBBY") ||
                Objects.equals(roomOrder.getOldRoomBeforeTransfer().getRoomType(), "BAR") ||
                Objects.equals(roomOrder.getOldRoomBeforeTransfer().getRoomType(), "LOUNGE") ||
                Objects.equals(roomOrder.getOldRoomBeforeTransfer().getRoomType(), "RESTO") &&

                Objects.equals(roomOrder.getCheckinRoom().getRoomType(), "LOBBY") ||
                Objects.equals(roomOrder.getCheckinRoom().getRoomType(), "BAR") ||
                Objects.equals(roomOrder.getCheckinRoom().getRoomType(), "LOUNGE") ||
                Objects.equals(roomOrder.getCheckinRoom().getRoomType(), "RESTO")){
            call = transferClient.transferLobbyToLobby(roomOrder.getOldRoomBeforeTransfer().getRoomCode(), roomOrder.getCheckinRoom().getRoomCode(), roomOrder.getChusr());
        }else{
            call = roomOrderClient.submitTransferRoomToRoom(roomOrder);
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
                Toasty.error(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT)
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

            SharedPreferences sharedPref = requireActivity().getSharedPreferences(getString(R.string.preference_print), Context.MODE_PRIVATE);
            int printerCode = sharedPref.getInt(getString(R.string.preference_print), 2);

            if(printerCode == 1){
                Printer printer = new Printer();
                printer.printerCheckinSlip(data, requireActivity());
            }else if(printerCode == 4){
                Printer58 printer = new Printer58();
                printer.printerCheckinSlip(data, requireActivity());
            }
            Navigation
                    .findNavController(requireView())
                    .navigate(
                            OperasionalListRoomAvailableTransferFragmentDirections
                                    .actionNavOperasionalTransferAvailableRoomFragmentToNavOperasionalListRoomToTransferFragment()
                    );
        });
    }
}