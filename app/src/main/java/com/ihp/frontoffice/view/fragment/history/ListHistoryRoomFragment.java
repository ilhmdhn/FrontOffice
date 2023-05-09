package com.ihp.frontoffice.view.fragment.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
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
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.RoomOrderClient;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.view.listadapter.ListRoomHistoryAdapter;
import com.ihp.frontoffice.viewmodel.RoomTypeViewModel;
import com.ihp.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListHistoryRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListHistoryRoomFragment extends Fragment {


    @BindView(R.id.chip_group_room_type)
    ChipGroup chipGroupRoomType;

    @BindView(R.id.room_list_recyclerview_history)
    RecyclerView roomRecyclerView;

    @BindView(R.id.togglegroup_state_room_history)
    MaterialButtonToggleGroup materialButtonToggleGroup;

    @BindView(R.id.room_progressbar)
    MKLoader progressBar;

    @BindView(R.id.search_room_by_code)
    SearchView searchView;

    //@BindView(R.id.toogle_all_room_history_)
    //MaterialButtonToggleGroup toogle_all_room_history_;

    @BindView(R.id.toogle_all_room_history)
    MaterialButton viewAllRoom;

    @BindView(R.id.toogle_all_room_ready_history)
    MaterialButton viewAllRoomReady;

    @BindView(R.id.toogle_all_room_chekin_history_)
    MaterialButton viewAllRoomChekin;

    @BindView(R.id.toogle_all_room_paid_history)
    MaterialButton viewAllRoomPaid;

    @BindView(R.id.toogle_all_room_clean_history)
    MaterialButton viewAllRoomClean;

    @BindView(R.id.toogle_all_checkin_room_history)
    MaterialButton viewAllCheckinRoomHistory;

//    @BindView(R.id.horizontalScrollViewHistory)
//    HorizontalScrollView horizontalScrollViewHistory;

    private ListRoomHistoryAdapter roomAdapter;
    private ArrayList<Room> roomArrayList = new ArrayList<>();
    private String TAG = "ListHistoryRoomFragment";
    private RoomViewModel roomViewModel;
    private RoomTypeViewModel roomTypeViewModel;
    private ArrayList<RoomType> roomTypeArrayList = new ArrayList<>();
    private String cariData;


    RoomOrderClient roomOrderClient;
    private MaterialAlertDialogBuilder dialogBuilder;
    private LayoutInflater dialogInflater;
    private View dialogView;
    private TextInputLayout inputNameVisitor,
            inputPhoneVisitor,
            inputRoom,
            inputCheckoutTime,
            inputCleanRoomCode;
    private static String BASE_URL;
    private User USER_FO;

    public ListHistoryRoomFragment() {
        // Required empty public constructor
    }


    public static ListHistoryRoomFragment newInstance() {
        ListHistoryRoomFragment fragment = new ListHistoryRoomFragment();
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
        View view = inflater.inflate(R.layout.fragment_list_history_room, container, false);
        ButterKnife.bind(this, view);
/*        TransitionInflater inflateAnim = TransitionInflater.from(requireContext());
        setEnterTransition(inflateAnim.inflateTransition(R.transition.fade));*/
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) requireActivity().getApplicationContext()).getUserFo();
        progressBar.setVisibility(View.VISIBLE);

        viewAllRoom.setVisibility(View.GONE);
        viewAllRoomReady.setVisibility(View.GONE);
        viewAllRoomChekin.setVisibility(View.GONE);
        viewAllRoomPaid.setVisibility(View.GONE);
        viewAllRoomClean.setVisibility(View.GONE);
//        horizontalScrollViewHistory.setVisibility(View.GONE);
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
                    case R.id.toogle_all_room_history:

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                allRoomSetupData();
                            }
                        }, 100);

                        break;
                    case R.id.toogle_all_room_ready_history:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                chipGroupRoomType.clearCheck();
                                readyRoomSetupData();
                            }
                        }, 100);

                        break;
                    case R.id.toogle_all_room_chekin_history_:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                chipGroupRoomType.clearCheck();
                                checkinRoomSetupData();
                            }
                        }, 100);
                        break;
                    case R.id.toogle_all_room_paid_history:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                chipGroupRoomType.clearCheck();
                                paidRoomSetupData();
                            }
                        }, 100);
                        break;
                    case R.id.toogle_all_room_clean_history:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                chipGroupRoomType.clearCheck();
                                cleanRoomSetupData();
                            }
                        }, 100);
                        break;
                        case R.id.toogle_all_checkin_room_history:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                chipGroupRoomType.clearCheck();
                                historyRoomSetupData();
                            }
                        }, 100);
                        break;
                    default:
                }


            }
        });

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
                chipGroupRoomType.clearCheck();
                cariData = keyword;
                if (keyword.length() > 1) {
                    //allRoomSetupData();
                    historyRoomSetupData();
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
                //allRoomSetupData();
                historyRoomSetupData();
                chipGroupRoomType.clearCheck();
                return false;
            }
        });

        roomViewModel = new ViewModelProvider(requireActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);
        roomTypeViewModel = new ViewModelProvider(requireActivity())
                .get(RoomTypeViewModel.class);
        roomTypeViewModel.init(BASE_URL);
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
        setupRoomRecyclerView();
        //allRoomSetupData();
        historyRoomSetupData();
        roomTypeSetupData();
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("History Room"));
    }

    @Override
    public void onResume() {
        super.onResume();
        //allRoomSetupData();
        historyRoomSetupData();
        roomAdapter = new ListRoomHistoryAdapter(getContext(), roomArrayList);
        roomRecyclerView.setAdapter(roomAdapter);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        roomAdapter.notifyDataSetChanged();

        roomTypeSetupData();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //isPotraitLayout = false;
            Log.i(TAG, "LANDSCAPE");
        } else {
            Log.i(TAG, "POTRAIT");
            //isPotraitLayout = true;
        }
    }


    private void roomTypeSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        chipGroupRoomType.clearCheck();
        roomTypeArrayList.clear();
        roomTypeViewModel
                .getRoomTypeLiveData()
                .observe(getViewLifecycleOwner(), roomTypeRespon -> {
                    if (roomTypeRespon.isOkay()) {
                        List<RoomType> roomTypes = roomTypeRespon.getRoomTypes();
                        roomTypeArrayList.addAll(roomTypes);
                        setupChipGroup();
                    } else {
                        roomTypeRespon.displayMessage(getContext());
                    }
                });

    }

    private void readyRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        refreshRecyclerView();
        roomViewModel.getRoomReady().observe(getViewLifecycleOwner(), roomResponse -> {
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                roomAdapter.notifyDataSetChanged();
            } else {
                roomResponse.displayMessage(getContext());
                //allRoomSetupData();

            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void checkinRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        refreshRecyclerView();
        roomViewModel.getRoomCheckin("").observe(getViewLifecycleOwner(), roomResponse -> {
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                roomAdapter.notifyDataSetChanged();
            } else {
                roomResponse.displayMessage(getContext());
                //allRoomSetupData();
            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void paidRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        refreshRecyclerView();
        roomViewModel.getRoomPaid(cariData).observe(getViewLifecycleOwner(), roomResponse -> {
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                roomAdapter.notifyDataSetChanged();
            } else {
                roomResponse.displayMessage(getContext());
                //allRoomSetupData();
            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void cleanRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        refreshRecyclerView();
        roomViewModel.getRoomCheckout(cariData).observe(getViewLifecycleOwner(), roomResponse -> {
            if (roomResponse.isOkay()) {
                roomResponse.displayMessage(getContext());
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                roomAdapter.notifyDataSetChanged();
            } else {
                roomResponse.displayMessage(getContext());
                //allRoomSetupData();

            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void historyRoomSetupData() {
        chipGroupRoomType.clearCheck();
        progressBar.setVisibility(View.VISIBLE);
        refreshRecyclerView();
        roomViewModel
                .getAllRoomHistory(cariData)
                .observe(getViewLifecycleOwner(), roomResponse -> {
                    roomResponse.displayMessage(getContext());
                    if (roomResponse.isOkay()) {
                        List<Room> listRoom = roomResponse.getRooms();
                        roomArrayList.clear();
                        roomArrayList.addAll(listRoom);
                        roomAdapter.notifyDataSetChanged();
                    }

                    progressBar.setVisibility(View.GONE);
                });
    }

    private void allRoomSetupData() {
        chipGroupRoomType.clearCheck();
        progressBar.setVisibility(View.VISIBLE);
        refreshRecyclerView();
        roomViewModel
                .getAllRoom(cariData)
                .observe(getViewLifecycleOwner(), roomResponse -> {
                    roomResponse.displayMessage(getContext());
                    if (roomResponse.isOkay()) {
                        List<Room> listRoom = roomResponse.getRooms();
                        roomArrayList.addAll(listRoom);
                        roomAdapter.notifyDataSetChanged();
                    }

                    progressBar.setVisibility(View.GONE);
                });
    }

    private void roomByTypeSetupData(RoomType roomType) {
        progressBar.setVisibility(View.VISIBLE);
        refreshRecyclerView();
        roomViewModel.getRoomByType(roomType).observe(getViewLifecycleOwner(), roomResponse -> {
            roomResponse.displayMessage(getContext());
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);

                roomAdapter.notifyDataSetChanged();
            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void refreshRecyclerView() {
        roomArrayList.clear();
        roomAdapter.notifyDataSetChanged();
    }

    private void setupRoomRecyclerView() {
        if (roomAdapter == null) {
            roomAdapter = new ListRoomHistoryAdapter(getContext(), roomArrayList);
            roomRecyclerView.setAdapter(roomAdapter);
            roomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        } else {
            roomAdapter.notifyDataSetChanged();
        }
    }

    private void setupChipGroup() {
        chipGroupRoomType.removeAllViews();
        for (RoomType roomType : roomTypeArrayList) {
            final Chip chip = new Chip(requireActivity());

            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 2,
                    getResources().getDisplayMetrics()
            );

            //chip.setTextSize(12);
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);

            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setText(roomType.getRoomType() + " " + roomType.getRoomTypeCapacity());
            chip.setChipBackgroundColorResource(R.color.colorPrimary);
            chip.setTextColor(Color.WHITE);
            chip.setTag(roomType);
            //chip.setChipStrokeWidth(2);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    refreshRecyclerView();
                    if (b) {
                        RoomType roomTypeSelected = (RoomType) compoundButton.getTag();
                        roomByTypeSetupData(roomTypeSelected);
                    }/*else{
                        allRoomSetupData();
                    }*/
                }
            });
            chipGroupRoomType.addView(chip);
        }
    }


    private void checkoutRoomDialog(Room room) {
        progressBar.setVisibility(View.GONE);
        dialogBuilder = new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme);
        dialogInflater = this.getLayoutInflater();
        dialogView = dialogInflater.inflate(R.layout.dialog_checkout_room, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("Checkout Room?");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);

        inputNameVisitor = dialogView.findViewById(R.id.checkout_name);
        inputPhoneVisitor = dialogView.findViewById(R.id.checkout_phone);
        inputRoom = dialogView.findViewById(R.id.checkout_room);
        inputCheckoutTime = dialogView.findViewById(R.id.checkout_time_out);

        inputNameVisitor.getEditText().setText(room.getRoomGuessName());
        inputPhoneVisitor.getEditText().setText(room.getRoomGuessHp());
        inputRoom.getEditText().setText(room.getRoomCode());
        inputCheckoutTime
                .getEditText()
                .setText(AppUtils
                        .getTanggal(room
                                .getRoomCheckoutHours()));

        inputNameVisitor.setEnabled(false);
        inputPhoneVisitor.setEnabled(false);
        inputRoom.setEnabled(false);
        inputCheckoutTime.setEnabled(false);

        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        Call<RoomOrderResponse> req = roomOrderClient
                                .checkoutRoom(room.getRoomCode(),USER_FO.getUserId());

                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                                progressBar.setVisibility(View.GONE);
                                RoomOrderResponse res = response.body();
                                res.displayMessage(getContext());
                                if (res.isOkay()) {
                                    paidRoomSetupData();
                                }
                                alertDialog.dismiss();
                                //submitImageSign(res.getRoomOrder().getKodeRcp());
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void cleanRoomDialog(Room room) {
        progressBar.setVisibility(View.GONE);
        dialogBuilder = new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme);
        dialogInflater = this.getLayoutInflater();
        dialogView = dialogInflater.inflate(R.layout.dialog_clean_room, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("Room Clean?");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);

        inputCleanRoomCode = dialogView.findViewById(R.id.clean_room_code);
        inputCleanRoomCode.getEditText().setText(room.getRoomCode());
        inputCleanRoomCode.setEnabled(false);

        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        Call<RoomOrderResponse> req = roomOrderClient
                                .cleanRoom(room.getRoomCode(), USER_FO.getUserId());

                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                                progressBar.setVisibility(View.GONE);
                                RoomOrderResponse res = response.body();
                                res.displayMessage(getContext());
                                if (res.isOkay()) {
                                    cleanRoomSetupData();
                                }

                                alertDialog.dismiss();
                                //submitImageSign(res.getRoomOrder().getKodeRcp());
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });

        alertDialog.show();
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
        //allRoomSetupData();
    }

    @Subscribe
    public void checkoutRoom(EventsWrapper.CheckoutRoom checkoutRoom) {
        checkoutRoomDialog(checkoutRoom.getRoom());
    }

    @Subscribe
    public void cleanRoom(EventsWrapper.CleanRoom cleanRoom) {
        cleanRoomDialog(cleanRoom.getRoom());
    }
}