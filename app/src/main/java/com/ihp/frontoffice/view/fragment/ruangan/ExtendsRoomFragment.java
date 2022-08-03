package com.ihp.frontoffice.view.fragment.ruangan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.InventoryPromo;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomPromo;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.RoomOrderClient;
import com.ihp.frontoffice.data.remote.respons.RoomExtendResponse;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.HelperListView;
import com.ihp.frontoffice.view.listadapter.ListDetailRoomOrderExtendAdapter;
import com.ihp.frontoffice.view.listadapter.ListPromoInventoryAdapter;
import com.ihp.frontoffice.view.listadapter.ListPromoRoomAdapter;
import com.ihp.frontoffice.viewmodel.InventoryPromoViewModel;
import com.ihp.frontoffice.viewmodel.RoomPromoViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExtendsRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExtendsRoomFragment extends Fragment {

    @BindView(R.id.extendsMinHours)
    ImageView inputMinHoursExtends;
    @BindView(R.id.extendsPlusHours)
    ImageView inputPlusHoursExtends;
    @BindView(R.id.extendsCountHours)
    TextView inputCountHoursExtends;
    int jamXtnd = 0;

    @BindView(R.id.extendsMinMinutes)
    ImageView inputMinMinutesExtends;
    @BindView(R.id.extendsPlusMinutes)
    ImageView inputPlusMinutesExtends;
    @BindView(R.id.extendsCountMinutes)
    TextView inputCountMinutesExtends;
    int menitXtnd = 0;

    @BindView(R.id.bttnExtendPromoRoom)
    Button buttonPromoRoom;

    @BindView(R.id.txtExtendKodePromoRoom)
    TextView kodePromoRoom;

    @BindView(R.id.bttnExtendsPromoFood)
    Button buttonPromoFood;

    @BindView(R.id.txtExtendsKodePromoFood)
    TextView kodePromoFood;

    @BindView(R.id.extendsProgressbar)
    MKLoader progressBar;

    @BindView(R.id.buttonExtends)
    Button buttonSubmit;

    @BindView(R.id.extendsInputVisitorName)
    TextInputLayout textInputName;

    @BindView(R.id.infoRoomCodeXtnd)
    TextView infoRoomCode;

    @BindView(R.id.infoRoomTypeXtnd)
    TextView infoRoomType;

    @BindView(R.id.extendsLayoutHistory)
    View historyXtndView;

    @BindView(R.id.extends_history_checkin)
    ListView historyecyclerview;


    private ListPromoRoomAdapter listPromoRoomAdapter;
    private ArrayList<RoomPromo> promoRoomList = new ArrayList<>();
    private RoomPromoViewModel roomPromoViewModel;
    private String choicePromoRoom;

    private ListPromoInventoryAdapter listPromoInventoryAdapter;
    private ArrayList<InventoryPromo> promoFoodList = new ArrayList<>();
    private InventoryPromoViewModel inventoryPromoViewModel;
    private String choicePromoFood;

    private ListDetailRoomOrderExtendAdapter listDetailRoomOrderExtendAdapter;
    private final List<Room> listRoomExtendHistory = new ArrayList<>();

    private Room room;


    private String EMPTY_PROMO;
    private RoomOrderClient roomOrderClient;
    private static String BASE_URL;
    private User USER_FO;

    public ExtendsRoomFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExtendsRoomFragment.
     */

    public static ExtendsRoomFragment newInstance(String param1, String param2) {
        ExtendsRoomFragment fragment = new ExtendsRoomFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        room = ExtendsRoomFragmentArgs.fromBundle(getArguments()).getRoom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_extends_room, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
        historyXtndView.setVisibility(View.GONE);

        EMPTY_PROMO = getResources().getString(R.string.kode_promo);
        choicePromoRoom = EMPTY_PROMO;
        choicePromoFood = EMPTY_PROMO;
        buttonPromoRoom.setOnClickListener(view -> {
            if (choicePromoRoom.equals(EMPTY_PROMO)) {
                promoRoomSetupData();
            } else {
                promoRoomViewData(EMPTY_PROMO);
                buttonPromoRoom.setText("Pilih");
            }
        });
        buttonPromoFood.setOnClickListener(view -> {
            if (choicePromoFood.equals(EMPTY_PROMO)) {
                promoFoodSetupData();
            } else {
                promoFoodViewData(EMPTY_PROMO);
                buttonPromoFood.setText("Pilih");
            }
        });

        buttonSubmit.setOnClickListener(view -> {
            submitExtendsRoom();
        });


        roomPromoViewModel = new ViewModelProvider(getActivity())
                .get(RoomPromoViewModel.class);
        roomPromoViewModel.init(BASE_URL);

        inventoryPromoViewModel = new ViewModelProvider(getActivity())
                .get(InventoryPromoViewModel.class);
        inventoryPromoViewModel.init(BASE_URL);

        textInputName.getEditText().setText(room.getRoomGuessName());
        textInputName.setEnabled(false);

        infoRoomCode.setText(room.getRoomCode());
        infoRoomType.setText(room.getRoomType());
        initDurationXtndUI();

        initInfoExtendHistory();

    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment(room.getRoomType()+" "+room.getRoomCode()));
    }

    private void initInfoExtendHistory() {
        progressBar.setVisibility(View.VISIBLE);
        Call<RoomExtendResponse> req = roomOrderClient
                .getExtendInfo(room.getRoomCode());

        req.enqueue(new Callback<RoomExtendResponse>() {
            @Override
            public void onResponse(Call<RoomExtendResponse> call, Response<RoomExtendResponse> response) {
                progressBar.setVisibility(View.GONE);
                RoomExtendResponse res = response.body();
                if (!res.isOkay()) {
                    res.displayMessage(getContext());
                    return;
                }
                historyXtndView.setVisibility(View.VISIBLE);
                listRoomExtendHistory.addAll(res.getRoomOrderExtends());
                viewInfoHistoryExtend();

            }

            @Override
            public void onFailure(Call<RoomExtendResponse> call, Throwable t) {
                Toast.makeText(getContext(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void viewInfoHistoryExtend() {
        listDetailRoomOrderExtendAdapter = new ListDetailRoomOrderExtendAdapter(getContext(), listRoomExtendHistory);
        historyecyclerview.setAdapter(listDetailRoomOrderExtendAdapter);
        historyecyclerview.setDivider(null);
        listDetailRoomOrderExtendAdapter.notifyDataSetChanged();
        HelperListView.getListViewSize(historyecyclerview);
    }


    private void initDurationXtndUI() {
        inputMinHoursExtends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jamXtnd == 0) {
                    inputCountHoursExtends.setText(String.valueOf(jamXtnd));
                }

                if (jamXtnd > 0) {

                    jamXtnd = jamXtnd - 1;
                    inputCountHoursExtends.setText(String.valueOf(jamXtnd));
                }
            }
        });
        inputPlusHoursExtends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jamXtnd == 24) {
                    inputCountHoursExtends.setText(String.valueOf(jamXtnd));
                }

                if (jamXtnd < 24) {

                    jamXtnd = jamXtnd + 1;
                    inputCountHoursExtends.setText(String.valueOf(jamXtnd));
                }

            }
        });


        inputMinMinutesExtends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menitXtnd == 0) {
                    inputCountMinutesExtends.setText(String.valueOf(menitXtnd));
                }

                if (menitXtnd > 0) {

                    menitXtnd = menitXtnd - 1;
                    inputCountMinutesExtends.setText(String.valueOf(menitXtnd));
                }
            }
        });
        inputPlusMinutesExtends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menitXtnd == 59) {
                    jamXtnd = jamXtnd + 1;
                    inputCountMinutesExtends.setText(String.valueOf(menitXtnd));
                    return;
                }

                if (menitXtnd < 60) {

                    menitXtnd = menitXtnd + 1;
                    inputCountMinutesExtends.setText(String.valueOf(menitXtnd));
                }
            }
        });


    }

    private void promoFoodSetupData() {
        promoFoodList.clear();
        progressBar.setVisibility(View.VISIBLE);
        inventoryPromoViewModel
                .getFoodPromoResponseMutableLiveData(room.getRoomType(), room.getRoomCode())
                .observe(getActivity(), foodPromoResponse -> {
                    progressBar.setVisibility(View.GONE);
                    if (foodPromoResponse.isOkay()) {
                        this.promoFoodList
                                .addAll(foodPromoResponse.getInventoryPromos());
                        dialogPromoFood();
                    } else {
                        foodPromoResponse.displayMessage(getContext());
                    }
                });
    }

    private void dialogPromoFood() {
        listPromoInventoryAdapter = new ListPromoInventoryAdapter(getContext(), promoFoodList);
        listPromoInventoryAdapter.notifyDataSetChanged();

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Pilih Promo Food")
                .setSingleChoiceItems(listPromoInventoryAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        promoFoodViewData(listPromoInventoryAdapter.getItem(i).getFoodPromoName());
                        buttonPromoFood.setText("Hapus");
                        dialogInterface.dismiss();
                       /* Toast
                                .makeText(getContext(), choicePromoRoom, Toast.LENGTH_SHORT)
                                .show();*/
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void promoRoomViewData(String promo) {
        choicePromoRoom = promo;
        kodePromoRoom.setText(choicePromoRoom);
    }

    private void promoFoodViewData(String promo) {
        choicePromoFood = promo;
        kodePromoFood.setText(choicePromoFood);
    }

    private void promoRoomSetupData() {
        promoRoomList.clear();
        progressBar.setVisibility(View.VISIBLE);
        roomPromoViewModel
                .getRoomPromoResponseMutableLiveData(room.getRoomType())
                .observe(getActivity(), roomPromoResponse -> {
                    progressBar.setVisibility(View.GONE);
                    if (roomPromoResponse.isOkay()) {
                        List<RoomPromo> roomPromos = roomPromoResponse.getRoomPromos();
                        this.promoRoomList.addAll(roomPromos);
                        dialogPromoRoom();
                    } else {
                        roomPromoResponse.displayMessage(getContext());
                    }
                });
    }

    private void dialogPromoRoom() {
        listPromoRoomAdapter = new ListPromoRoomAdapter(getContext(), promoRoomList);
        listPromoRoomAdapter.notifyDataSetChanged();

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Pilih Promo Room")
                .setSingleChoiceItems(listPromoRoomAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        promoRoomViewData(listPromoRoomAdapter.getItem(i).getRoomPromoName());
                        buttonPromoRoom.setText("Hapus");
                        dialogInterface.dismiss();
                       /* Toast
                                .makeText(getContext(), choicePromoRoom, Toast.LENGTH_SHORT)
                                .show();*/
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void submitExtendsRoom() {
        if (jamXtnd == 0 && menitXtnd == 0) {
            Toast
                    .makeText(getContext(), "Mohon Periksa Kembali", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        List<String> promoSelected = new ArrayList<>();
        if (!choicePromoFood.equals(EMPTY_PROMO)) {
            promoSelected.add(choicePromoFood);
        }
        if (!choicePromoRoom.equals(EMPTY_PROMO)) {
            promoSelected.add(choicePromoRoom);
        }

        new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme)
                .setTitle("Extend Room")
                .setMessage("Anda ingin melanjutkan")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        Call<RoomOrderResponse> req = roomOrderClient
                                .submitExtendsRoom(
                                        room.getRoomCode(),
                                        String.valueOf(jamXtnd),
                                        String.valueOf(menitXtnd),
                                        promoSelected,
                                        USER_FO.getUserId(),
                                        false);

                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                                progressBar.setVisibility(View.GONE);
                                RoomOrderResponse res = response.body();
                                if (!res.isOkay()) {
                                    res.displayMessage(getContext());
                                    return;
                                }
                                navToMain();
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void navToMain() {
       /* Navigation
                .findNavController(this.getView())
                .navigate(ExtendsRoomFragmentDirections
                        .actionNavExtendsRoomFragmentToNavListRoomFragment());*/
    }




}