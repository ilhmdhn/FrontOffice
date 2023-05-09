package com.ihp.frontoffice.view.fragment.operasional.checkout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.RoomOrderClient;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.view.component.BasePagination;
import com.ihp.frontoffice.view.listadapter.ListOperasionalCheckoutRoomAdapter;
import com.ihp.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OperasionalListRoomToCheckoutFragment extends Fragment {

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

//    @BindView(R.id.text_bayar_room)
//    TextView textBayarRoom;

//    @BindView(R.id.text_clean_room)
//    TextView textCleanRoom;

    //pagination
    private BasePagination p;
    private int totalPages=0;
    private int currentPage = 0;

    private RoomViewModel roomViewModel;
    private String cariData = "";
    private ListOperasionalCheckoutRoomAdapter roomAdapter;
    private ArrayList<Room> roomArrayList = new ArrayList<>();

    //checkout
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

    public OperasionalListRoomToCheckoutFragment() {
        // Required empty public constructor
    }


    public static OperasionalListRoomToCheckoutFragment newInstance() {
        OperasionalListRoomToCheckoutFragment fragment = new OperasionalListRoomToCheckoutFragment();
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
        View view = inflater.inflate(R.layout.fragment_operasional_list_room_to_checkout, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) requireActivity().getApplicationContext()).getUserFo();
        roomViewModel = new ViewModelProvider(requireActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
        searchView.setQueryHint("Kode Room");
//        textCleanRoom.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                //progressBar.setVisibility(View.VISIBLE);

                cariData = keyword;
                if (keyword.length() <= 1) {
                    cariData = "";
                }
                paymentRoomSetupData();


                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cariData = "";
                paymentRoomSetupData();
                return false;
            }
        });

//        textBayarRoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view)
//                        .navigate(OperasionalListRoomToCheckoutFragmentDirections
//                                .navOperasionalListRoomToCheckoutFragmentToNavOperasionalListRoomToPaymentFragment()
//                        );
//            }
//        });

//        textCleanRoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view)
//                        .navigate(OperasionalListRoomToCheckoutFragmentDirections
//                                .actionNavOperasionalListRoomToCheckoutFragmentToNavOperasionalListRoomToCleanFragment()
//                        );
//            }
//        });

        //paymentRoomSetupData();

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
                        .TitleFragment("Checkout"));
    }

    private void paymentRoomSetupData() {
        if(roomAdapter.getItemCount()>0){
            roomAdapter.clearItem();
        }
        roomArrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel.getRoomPaid(cariData).observe(requireActivity(), roomResponse -> {
            roomResponse.displayMessage(getContext());
            progressBar.setVisibility(View.GONE);
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                bindData(0);
            }
            toggleButtons();
        });
    }


    private void bindData(int page) {
        p = new BasePagination(roomArrayList);
        p.setItemsPerPage(9);
        totalPages = p.getTotalPages();
        roomAdapter = new ListOperasionalCheckoutRoomAdapter(getContext(), p.getCurrentData(page));
        roomRecyclerView.setAdapter(roomAdapter);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        roomAdapter.notifyDataSetChanged();

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
        roomArrayList.clear();
        roomAdapter = new ListOperasionalCheckoutRoomAdapter(getContext(), null);
        roomRecyclerView.setAdapter(roomAdapter);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        roomAdapter.notifyDataSetChanged();
        paymentRoomSetupData();
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
        paymentRoomSetupData();
    }

    @Subscribe
    public void operasionalCheckinRoom(EventsWrapper.OperasionalBusCheckinRoom operasionalBusCheckinRoom) {
        Room room = operasionalBusCheckinRoom.getRoom();
        checkoutRoomDialog(room);
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
                        alertDialog.dismiss();
                        lastConfirmCheckOut(room);
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void lastConfirmCheckOut(Room roomx){
        progressBar.setVisibility(View.VISIBLE);
        new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme)
                .setTitle("Proses Checkout")
                .setMessage("Pastikan Waktu Habis/Pengunjung Pulang")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<RoomOrderResponse> req = roomOrderClient
                                .checkoutRoom(roomx.getRoomCode(), USER_FO.getUserId());

                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {

                                progressBar.setVisibility(View.GONE);

                                RoomOrderResponse res = response.body();
                                res.displayMessage(getContext());
                               /* if (res.isOkay()) {

                                }*/
                                paymentRoomSetupData();

                                //submitImageSign(res.getRoomOrder().getKodeRcp());
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .show();
    }



}