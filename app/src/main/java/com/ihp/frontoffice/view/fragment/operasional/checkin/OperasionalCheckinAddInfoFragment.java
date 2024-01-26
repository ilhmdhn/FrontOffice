package com.ihp.frontoffice.view.fragment.operasional.checkin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.ihp.frontoffice.helper.Printer58;
import com.ihp.frontoffice.helper.Printer;
import com.ihp.frontoffice.viewmodel.OtherViewModel;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.InventoryPromo;
import com.ihp.frontoffice.data.entity.Member;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.entity.RoomPromo;
import com.ihp.frontoffice.data.entity.TypeEdc;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.entity.Voucher;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.MemberClient;
import com.ihp.frontoffice.data.remote.RoomOrderClient;
import com.ihp.frontoffice.data.remote.respons.EdcTypeResponse;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.data.remote.respons.VoucherResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.helper.QRScanType;
import com.ihp.frontoffice.view.listadapter.ListEdcTypeAdapter;
import com.ihp.frontoffice.view.listadapter.ListPromoInventoryAdapter;
import com.ihp.frontoffice.view.listadapter.ListPromoRoomAdapter;
import com.ihp.frontoffice.viewmodel.InventoryPromoViewModel;
import com.ihp.frontoffice.viewmodel.RoomPromoViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalCheckinAddInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalCheckinAddInfoFragment extends Fragment {
    @BindView(R.id.checkin_add_info_member_name)
    TextView infoNamaMember;

    @BindView(R.id.checkin_add_info_member_phone)
    TextView infoPhoneMember;

    @BindView(R.id.checkin_add_info_member_gender)
    TextView infoGenderMember;

    @BindView(R.id.checkin_add_info_type_room)
    TextView infoCheckinRoomType;

    @BindView(R.id.checkin_add_info_room_code)
    TextView infoCodeMember;

    @BindView(R.id.checkin_add_info_durasi_checkin)
    TextView infoDurasiCheckin;

    //TODO : voucher init view
    @BindView(R.id.checkin_button_scan_qrcode_voucher)
    AppCompatButton buttonScanQRCodeVoucher;
    @BindView(R.id.checkin_check_code_voucher)
    AppCompatButton buttonCheckCodeVoucher;
    @BindView(R.id.checkin_reset_code_voucher)
    AppCompatButton buttonResetCodeVoucher;
    @BindView(R.id.checkin_input_code_voucher)
    TextInputLayout inputCodeVoucher;
    @BindView(R.id.view_output_cek_code_voucher)
    View viewOutputCekCodeVoucher;
    @BindView(R.id.img_output_cek_code_voucher)
    ImageView outputCheckImgVoucher;
    @BindView(R.id.txt_output_cek_code_voucher)
    TextView outputCheckTxtVoucher;
    private boolean isVoucherScanActive;

    // TODO : init view jumlah pengunjung

    @BindView(R.id.checkinMinQmAnak)
    ImageView inputMinQmAnak;
    @BindView(R.id.checkinPlusQmAnak)
    ImageView inputPlusQmAnak;
    @BindView(R.id.checkinCountQmAnak)
    TextView inputCountQmAnak;
    int qmAnak = 0;

    @BindView(R.id.checkinMinQmRemaja)
    ImageView inputMinQmRemaja;
    @BindView(R.id.checkinPlusQmRemaja)
    ImageView inputPlusQmRemaja;
    @BindView(R.id.checkinCountQmRemaja)
    TextView inputCountQmRemaja;
    int qmRemaja = 0;

    @BindView(R.id.checkinMinQmDewasa)
    ImageView inputMinQmDewasa;
    @BindView(R.id.checkinPlusQmDewasa)
    ImageView inputPlusQmDewasa;
    @BindView(R.id.checkinCountQmDewasa)
    EditText inputCountQmDewasa;
    int qmDewasa = 0;

    @BindView(R.id.checkinMinQmTua)
    ImageView inputMinQmTua;
    @BindView(R.id.checkinPlusQmTua)
    ImageView inputPlusQmTua;
    @BindView(R.id.checkinCountQmTua)
    TextView inputCountQmTua;
    int qmTua = 0;

    @BindView(R.id.checkinMinQfAnak)
    ImageView inputMinQfAnak;
    @BindView(R.id.checkinPlusQfAnak)
    ImageView inputPlusQfAnak;
    @BindView(R.id.checkinCountQfAnak)
    TextView inputCountQfAnak;
    int qfAnak = 0;

    @BindView(R.id.checkinMinQfRemaja)
    ImageView inputMinQfRemaja;
    @BindView(R.id.checkinPlusQfRemaja)
    ImageView inputPlusQfRemaja;
    @BindView(R.id.checkinCountQfRemaja)
    TextView inputCountQfRemaja;
    int qfRemaja = 0;

    @BindView(R.id.checkinMinQfDewasa)
    ImageView inputMinQfDewasa;
    @BindView(R.id.checkinPlusQfDewasa)
    ImageView inputPlusQfDewasa;
    @BindView(R.id.checkinCountQfDewasa)
    TextView inputCountQfDewasa;
    int qfDewasa = 0;

    @BindView(R.id.checkinMinQfTua)
    ImageView inputMinQfTua;
    @BindView(R.id.checkinPlusQfTua)
    ImageView inputPlusQfTua;
    @BindView(R.id.checkinCountQfTua)
    TextView inputCountQfTua;
    int qfTua = 0;

    @BindView(R.id.overpack_warning)
    TextView overpackWarning;

    // TODO : init view keterangan&event
    @BindView(R.id.checkin_input_desc)
    TextInputLayout inputDesc;
    @BindView(R.id.checkin_input_event_desc)
    TextInputLayout inputEventDesc;
    @BindView(R.id.checkin_input_event_name_alias)
    TextInputLayout inputEventOwner;

    // TODO : init view uang muka
    @BindView(R.id.input_checkin_type_dp)
    RadioGroup inputDpGroup;
    @BindView(R.id.checkin_input_dp_nominal)
    TextInputLayout inputDpNominal;
    @BindView(R.id.view_debet_credit)
    View viewDpDebetCredit;
    @BindView(R.id.view_transfer)
    View viewDpTransfer;
    @BindView(R.id.button_choose_edc)
    AppCompatButton buttonChooseEdc;
    @BindView(R.id.bttnChooseCard)
    AppCompatButton buttonChooseCard;
    @BindView(R.id.bttn_choose_date_dp)
    AppCompatButton buttonDateDpExpired;
    @BindView(R.id.inputNamaUmDebetCredit)
    TextInputLayout inputDpNamaDebetCredit;
    @BindView(R.id.inputApprovalNumberUmDebetCredit)
    TextInputLayout inputDpApprovalCodeDebetCredit;
    @BindView(R.id.inputCardNumberUmDebetCredit)
    TextInputLayout inputDpCardNumberDebetCredit;
    @BindView(R.id.input_expired_date_dp_debet_credit)
    TextInputLayout inputDpExpireDate;
    @BindView(R.id.input_bank_account_name)
    TextInputLayout inputBankAccountNameTf;
    @BindView(R.id.input_bank_name)
    TextInputLayout inputBankNameTf;
    @BindView(R.id.txt_edc)
    TextView inputDpTextEdc;
    @BindView(R.id.txtCard)
    TextView inputDpTextCard;


    //TODO : init view promo
    @BindView(R.id.btn_promo_room)
    Button buttonPromoRoom;

    @BindView(R.id.layout_head_promo)
    View layoutPromoRoom;

    @BindView(R.id.txt_kode_promo_room)
    TextView kodePromoRoom;

    @BindView(R.id.bttnPromoFood)
    Button buttonPromoFood;

    @BindView(R.id.txtKodePromoFood)
    TextView kodePromoFood;

    @BindView(R.id.btn_submit)
    AppCompatButton buttonSubmit;

    @BindView(R.id.checkinProgressbar)
    MKLoader progressBar;

    //TODO : init  data
    private int mYear, mMonth, mDay;
    private ListPromoRoomAdapter listPromoRoomAdapter;
    private ArrayList<RoomPromo> promoRoomList = new ArrayList<>();
    private RoomPromoViewModel roomPromoViewModel;
    private String choicePromoRoom;

    private ListPromoInventoryAdapter listPromoInventoryAdapter;
    private ArrayList<InventoryPromo> promoFoodList = new ArrayList<>();
    private InventoryPromoViewModel inventoryPromoViewModel;
    private String choicePromoFood;

    private ListEdcTypeAdapter listEdcTypeAdapter;
    private ArrayList<TypeEdc> typesListEdc = new ArrayList<>();
    private TypeEdc choiceTypeEdc = null;
    private String choicecard;
    String keteranganUangMuka = "";
    String input1JenisKartu = "";
    String input2Nama = "";
    String input3NomorKartu = "";
    String input4NomorApproval = "";
    String edcMachine = "";
    String nominalDp = "";

    RoomOrderClient roomOrderClient;
    private MemberClient memberClient;
    private RoomOrder roomOrder;
    private Member member;
    private Room room;

    private static String EMPTY_PROMO;
    private static String EMPTY_EDC_DP;
    private static String EMPTY_CARD_DP;


    private String[] banks;
    private ArrayAdapter<String> adapterBanks;
    private boolean isUseDP = false;
    private boolean isUseDpCard = false;
    private boolean isUseDpTransfer = false;
    private String dpType;
    private String finalVoucherCode = "";
    private String errorMessageVoucher;
    private final String TAG = "OperasionalCheckinEditInfoFragment";
    private static String BASE_URL;
    private User USER_FO;
    private String current = "";
    private OtherViewModel otherViewModel;
    public OperasionalCheckinAddInfoFragment() {
        // Required empty public constructor
    }

    public static OperasionalCheckinAddInfoFragment newInstance() {
        OperasionalCheckinAddInfoFragment fragment = new OperasionalCheckinAddInfoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomOrder = OperasionalCheckinAddInfoFragmentArgs
                .fromBundle(getArguments())
                .getRoomOrder();
        member = roomOrder.getMember();
        room = roomOrder.getCheckinRoom();

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Toasty.warning(requireActivity(), "Mohon Lengkapi Data", Toasty.LENGTH_SHORT).show();
                return;
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operasional_checkin_add_info, container, false);
        ButterKnife.bind(this, view);
        setMainTitle();
        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) requireActivity().getApplicationContext()).getUserFo();
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
        memberClient = ApiRestService.getClient(BASE_URL).create(MemberClient.class);
        roomPromoViewModel = new ViewModelProvider(requireActivity())
                .get(RoomPromoViewModel.class);
        roomPromoViewModel.init(BASE_URL);

        inventoryPromoViewModel = new ViewModelProvider(requireActivity())
                .get(InventoryPromoViewModel.class);
        inventoryPromoViewModel.init(BASE_URL);

        otherViewModel = new OtherViewModel();



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isVoucherScanActive = false;

        EMPTY_PROMO = getResources().getString(R.string.kode_promo);
        choicePromoRoom = EMPTY_PROMO;
        choicePromoFood = EMPTY_PROMO;

        EMPTY_EDC_DP = getResources().getString(R.string.edc_mesin);
        EMPTY_CARD_DP = getResources().getString(R.string.type_card);
        choicecard = EMPTY_CARD_DP;

        initVoucherValidationUI();

        initQmUI();
        initQfUI();
        initDownPaymentUI();
        setVisitorData();

        buttonSubmit.setOnClickListener(view -> {
            submitCheckin();
        });

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

        buttonChooseEdc.setOnClickListener(view -> {
            if (inputDpTextEdc.getText().toString().equals(EMPTY_EDC_DP)) {
                dialogEdcType();
            } else {
                edcTypeViewData(null);
                buttonChooseEdc.setText("Pilih");
                buttonChooseEdc.setTextColor(Color.GREEN);

            }
        });

        buttonChooseCard.setOnClickListener(view -> {
            if (choicecard.equals(EMPTY_CARD_DP)) {
                dialogCardType();
            } else {
                cardDpViewData(EMPTY_CARD_DP);
                buttonChooseCard.setText("Pilih");
                buttonChooseCard.setTextColor(Color.GREEN);
            }
        });

        inputDpNominal.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().length() > 11) {
                    inputDpNominal.getEditText().setText(current);
                    inputDpNominal.getEditText().setSelection(current.length());
                    Toasty
                            .warning(requireActivity(), "Pembayaran Maksimal Ratusan Juta")
                            .show();
                    return;
                }
                if (!s.toString().equals(current)) {
                    inputDpNominal.getEditText().removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formattedDotWithRp = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String formattedDotNonRp = formattedDotWithRp.replaceAll(replace, "");

                    current = formattedDotNonRp;
                    inputDpNominal.getEditText().setText(formattedDotNonRp);
                    inputDpNominal.getEditText().setSelection(formattedDotNonRp.length());
                    inputDpNominal.getEditText().addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void cardDpViewData(String card) {
        choicecard = card;
        inputDpTextCard.setText(card);
    }

    private void dialogCardType() {
        banks = getResources().getStringArray(R.array.bank_values);
        adapterBanks = new ArrayAdapter<String>(requireActivity(),
                android.R.layout.simple_list_item_1, banks);

        new MaterialAlertDialogBuilder(requireActivity(),R.style.MaterialAlertDialogDarkTheme)
                .setTitle("Pilih Bank")
                .setSingleChoiceItems(adapterBanks, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cardDpViewData(adapterBanks.getItem(i));
                        buttonChooseCard.setText("HAPUS");
                        buttonChooseCard.setTextColor(Color.RED);
                        dialogInterface.dismiss();
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


    private void dialogEdcType() {
        if (typesListEdc.size() < 1) {
            initEdcType();
        }

        listEdcTypeAdapter = new ListEdcTypeAdapter(requireActivity(), typesListEdc);
        listEdcTypeAdapter.notifyDataSetChanged();

        new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme)
                .setTitle("Pilih EDC")
                .setSingleChoiceItems(listEdcTypeAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edcTypeViewData(listEdcTypeAdapter.getItem(i));
                        buttonChooseEdc.setText("HAPUS");
                        buttonChooseEdc.setTextColor(Color.RED);
                        dialogInterface.dismiss();
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

    private void edcTypeViewData(TypeEdc typeEdc) {
        if (null == typeEdc) {
            inputDpTextEdc.setText(EMPTY_EDC_DP);
            return;
        }
        choiceTypeEdc = typeEdc;
        inputDpTextEdc.setText(choiceTypeEdc.getEdcName());
    }

    private void promoFoodSetupData() {
        promoFoodList.clear();
        visibleProgressBar();
        inventoryPromoViewModel
                .getFoodPromoResponseMutableLiveData(room.getRoomType(), room.getRoomCode())
                .observe(getViewLifecycleOwner(), foodPromoResponse -> {
                    invisibleProgressBar();
                    if (foodPromoResponse.isOkay()) {
                        this.promoFoodList
                                .addAll(foodPromoResponse.getInventoryPromos());
                        dialogPromoFood();
                    } else {
                        foodPromoResponse.displayMessage(requireActivity());
                    }
                });
    }

    private void dialogPromoFood() {
        listPromoInventoryAdapter = new ListPromoInventoryAdapter(requireActivity(), promoFoodList);
        listPromoInventoryAdapter.notifyDataSetChanged();

        new MaterialAlertDialogBuilder(requireActivity(),R.style.MaterialAlertDialogDarkTheme)
                .setTitle("Pilih Promo Food")
                .setSingleChoiceItems(listPromoInventoryAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        promoFoodViewData(listPromoInventoryAdapter.getItem(i).getFoodPromoName());
                        buttonPromoFood.setText("Hapus");
                        dialogInterface.dismiss();
                       /* Toast
                                .makeText(requireActivity(), choicePromoRoom, Toast.LENGTH_SHORT)
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

    private void promoFoodViewData(String promo) {
        choicePromoFood = promo;
        kodePromoFood.setText(choicePromoFood);
    }

    private void promoRoomSetupData() {
        promoRoomList.clear();
        visibleProgressBar();
        roomPromoViewModel
                .getRoomPromoResponseMutableLiveData(room.getRoomType())
                .observe(getViewLifecycleOwner(), roomPromoResponse -> {
                    invisibleProgressBar();
                    if (roomPromoResponse.isOkay()) {
                        List<RoomPromo> roomPromos = roomPromoResponse.getRoomPromos();
                        this.promoRoomList.addAll(roomPromos);
                        dialogPromoRoom();
                    } else {
                        roomPromoResponse.displayMessage(requireActivity());
                    }
                });
    }

    private void dialogPromoRoom() {
        listPromoRoomAdapter = new ListPromoRoomAdapter(requireActivity(), promoRoomList);
        listPromoRoomAdapter.notifyDataSetChanged();

        new MaterialAlertDialogBuilder(requireActivity(),R.style.MaterialAlertDialogDarkTheme)
                .setTitle("Pilih Promo Room")
                .setSingleChoiceItems(listPromoRoomAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        promoRoomViewData(listPromoRoomAdapter.getItem(i).getRoomPromoName());
                        buttonPromoRoom.setText("Hapus");
                        dialogInterface.dismiss();
                       /* Toast
                                .makeText(requireActivity(), choicePromoRoom, Toast.LENGTH_SHORT)
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

    private void countTotalVisitor() {
        int total = qmAnak + qmDewasa + qmRemaja + qmTua + qfAnak + qfDewasa + qfRemaja + qfTua;
        if (total > room.getRoomCapacity()) {
            int over = total - room.getRoomCapacity();
            overpackWarning.setVisibility(View.VISIBLE);
            overpackWarning.setText(" Overpack " + String.valueOf(over));
        } else {
            overpackWarning.setVisibility(View.GONE);
            overpackWarning.setText("");
        }
    }

    private void initQmUI() {
        inputMinQmAnak.setOnClickListener(view -> {
            if (qmAnak == 0) {
                inputCountQmAnak.setText(String.valueOf(qmAnak));
            }

            if (qmAnak > 0) {

                qmAnak = qmAnak - 1;
                inputCountQmAnak.setText(String.valueOf(qmAnak));
            }
            countTotalVisitor();
        });
        inputPlusQmAnak.setOnClickListener(view -> {
            if (qmAnak == 10) {
                inputCountQmAnak.setText(String.valueOf(qmAnak));
            }

            if (qmAnak < 10) {
                qmAnak = qmAnak + 1;
                inputCountQmAnak.setText(String.valueOf(qmAnak));
            }
            countTotalVisitor();
        });
        inputMinQmRemaja.setOnClickListener(view -> {
            if (qmRemaja == 0) {
                inputCountQmRemaja.setText(String.valueOf(qmRemaja));
            }

            if (qmRemaja > 0) {

                qmRemaja = qmRemaja - 1;
                inputCountQmRemaja.setText(String.valueOf(qmRemaja));
            }
            countTotalVisitor();
        });
        inputPlusQmRemaja.setOnClickListener(view -> {
            if (qmRemaja == 10) {
                inputCountQmRemaja.setText(String.valueOf(qmRemaja));
            }

            if (qmRemaja < 10) {
                qmRemaja = qmRemaja + 1;
                inputCountQmRemaja.setText(String.valueOf(qmRemaja));
            }
            countTotalVisitor();
        });


        inputCountQmDewasa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String sCounted = charSequence.toString();

                if (sCounted.length() > 0) {
                    try {
                        qmDewasa = Integer.parseInt(sCounted);
                    } catch (Exception x) {
                        qmDewasa = 0;
                        inputCountQmDewasa.setText(String.valueOf(qmDewasa));

                    }

                } else {
                    qmDewasa = 0;
                    inputCountQmDewasa.setText(String.valueOf(qmDewasa));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputMinQmDewasa.setOnClickListener(view -> {
            if (qmDewasa == 0) {
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }

            if (qmDewasa > 0) {

                qmDewasa = qmDewasa - 1;
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }
            countTotalVisitor();
        });
        inputPlusQmDewasa.setOnClickListener(view -> {
            /*if (qmDewasa == 10) {
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }*/
            qmDewasa = qmDewasa + 1;
            inputCountQmDewasa.setText(String.valueOf(qmDewasa));
           /* if (qmDewasa < 10) {
                qmDewasa = qmDewasa + 1;
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }*/
            countTotalVisitor();
        });
        inputMinQmTua.setOnClickListener(view -> {
            if (qmTua == 0) {
                inputCountQmTua.setText(String.valueOf(qmTua));
            }

            if (qmTua > 0) {

                qmTua = qmTua - 1;
                inputCountQmTua.setText(String.valueOf(qmTua));
            }
            countTotalVisitor();
        });
        inputPlusQmTua.setOnClickListener(view -> {
            if (qmTua == 10) {
                inputCountQmTua.setText(String.valueOf(qmTua));
            }

            if (qmTua < 10) {
                qmTua = qmTua + 1;
                inputCountQmTua.setText(String.valueOf(qmTua));
            }
            countTotalVisitor();
        });
    }

    private void initQfUI() {
        inputMinQfAnak.setOnClickListener(view -> {
            if (qfAnak == 0) {
                inputCountQfAnak.setText(String.valueOf(qfAnak));
            }

            if (qfAnak > 0) {

                qfAnak = qfAnak - 1;
                inputCountQfAnak.setText(String.valueOf(qfAnak));
            }
            countTotalVisitor();
        });
        inputPlusQfAnak.setOnClickListener(view -> {
            if (qfAnak == 10) {
                inputCountQfAnak.setText(String.valueOf(qfAnak));
            }

            if (qfAnak < 10) {
                qfAnak = qfAnak + 1;
                inputCountQfAnak.setText(String.valueOf(qfAnak));
            }
            countTotalVisitor();
        });
        inputMinQfRemaja.setOnClickListener(view -> {
            if (qfRemaja == 0) {
                inputCountQfRemaja.setText(String.valueOf(qfRemaja));
            }

            if (qfRemaja > 0) {

                qfRemaja = qfRemaja - 1;
                inputCountQfRemaja.setText(String.valueOf(qfRemaja));
            }
            countTotalVisitor();
        });
        inputPlusQfRemaja.setOnClickListener(view -> {
            if (qfRemaja == 10) {
                inputCountQfRemaja.setText(String.valueOf(qfRemaja));
            }

            if (qfRemaja < 10) {
                qfRemaja = qfRemaja + 1;
                inputCountQfRemaja.setText(String.valueOf(qfRemaja));
            }
            countTotalVisitor();
        });
        inputMinQfDewasa.setOnClickListener(view -> {
            if (qfDewasa == 0) {
                inputCountQfDewasa.setText(String.valueOf(qfDewasa));
            }

            if (qfDewasa > 0) {

                qfDewasa = qfDewasa - 1;
                inputCountQfDewasa.setText(String.valueOf(qfDewasa));
            }
            countTotalVisitor();
        });
        inputPlusQfDewasa.setOnClickListener(view -> {
            if (qfDewasa == 10) {
                inputCountQfDewasa.setText(String.valueOf(qfDewasa));
            }

            if (qfDewasa < 10) {
                qfDewasa = qfDewasa + 1;
                inputCountQfDewasa.setText(String.valueOf(qfDewasa));
            }
            countTotalVisitor();
        });
        inputMinQfTua.setOnClickListener(view -> {
            if (qfTua == 0) {
                inputCountQfTua.setText(String.valueOf(qfTua));
            }

            if (qfTua > 0) {

                qfTua = qfTua - 1;
                inputCountQfTua.setText(String.valueOf(qfTua));
            }
            countTotalVisitor();
        });
        inputPlusQfTua.setOnClickListener(view -> {
            if (qfTua == 10) {
                inputCountQfTua.setText(String.valueOf(qfTua));
            }

            if (qfTua < 10) {
                qfTua = qfTua + 1;
                inputCountQfTua.setText(String.valueOf(qfTua));
            }
            countTotalVisitor();
        });
    }

    private void initVoucherValidationUI() {
        buttonScanQRCodeVoucher.setOnClickListener(view -> {
            if (!isVoucherScanActive) {
                try {
                    visibleProgressBar();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            GlobalBus
                                    .getBus()
                                    .post(new EventsWrapper
                                            .XZscan(QRScanType.VOUCHER.getType()));
                            isVoucherScanActive = true;
                        }
                    }, 500);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(requireActivity(), "Error Karena " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonResetCodeVoucher.setOnClickListener(view -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttonScanQRCodeVoucher.setVisibility(View.VISIBLE);
                    buttonResetCodeVoucher.setVisibility(View.GONE);
                    viewOutputCekCodeVoucher.setVisibility(View.GONE);
                    isVoucherScanActive = false;

                    inputCodeVoucher.setFocusable(false);
                    inputCodeVoucher.setEnabled(true);
                    inputCodeVoucher.getEditText().setText("");
                }
            }, 500);
        });

        buttonCheckCodeVoucher.setOnClickListener(view -> {
            String codeVcr = inputCodeVoucher.getEditText().getText().toString();
            if (codeVcr.equals("") || codeVcr == "") {
                Toast.makeText(requireActivity(),
                        "Kode Masih Kosong",
                        Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            visibleProgressBar();

            Call<VoucherResponse> call = memberClient.checkVoucherMembership( codeVcr);
            call.enqueue(new Callback<VoucherResponse>() {
                @Override
                public void onResponse(Call<VoucherResponse> call, Response<VoucherResponse> response) {
                   invisibleProgressBar();
                    errorMessageVoucher = "";
                    viewOutputCekCodeVoucher.setVisibility(View.VISIBLE);
                    VoucherResponse res = response.body();
                    if (!res.isOkay()) {
                        errorMessageVoucher = res.getMessage();
                        outputCheckImgVoucher.setImageResource(R.drawable.ic_unverified);
                        outputCheckTxtVoucher.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast
                                        .makeText(requireActivity(),
                                                res.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                                outputCheckTxtVoucher.setText(errorMessageVoucher);
                                outputCheckTxtVoucher.setText("Invalid");
                                visibleProgressBar();
                                checkLocalVoucher(codeVcr);
                            }
                        }, 100);

                        return;
                    }
                    finalVoucherCode = codeVcr;
                    outputCheckImgVoucher.setImageResource(R.drawable.ic_verified);
                    outputCheckTxtVoucher.setText("Valid");
                    outputCheckTxtVoucher.setTextColor(ContextCompat.getColor(requireActivity(), R.color.green));
                    buttonResetCodeVoucher.setVisibility(View.VISIBLE);
                    confirmVoucher(res.getVoucher());
                }

                @Override
                public void onFailure(Call<VoucherResponse> call, Throwable t) {

                    Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                    invisibleProgressBar();
                }
            });
        });
    }

    private void checkLocalVoucher(String codVcr) {
        Call<VoucherResponse> call = memberClient.checkVoucherOutlet(codVcr);
        call.enqueue(new Callback<VoucherResponse>() {
            @Override
            public void onResponse(Call<VoucherResponse> call, Response<VoucherResponse> response) {
               invisibleProgressBar();
                viewOutputCekCodeVoucher.setVisibility(View.VISIBLE);
                VoucherResponse res = response.body();
                if (!res.isOkay()) {
                    errorMessageVoucher = errorMessageVoucher + "" + res.getMessage();
                    Toast
                            .makeText(requireActivity(),
                                    res.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                    outputCheckImgVoucher.setImageResource(R.drawable.ic_unverified);
                    outputCheckTxtVoucher.setText(errorMessageVoucher);
                    outputCheckTxtVoucher.setText("Invalid");
                    outputCheckTxtVoucher.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red));
                    return;
                }

                finalVoucherCode = codVcr;
                outputCheckImgVoucher.setImageResource(R.drawable.ic_verified);
                outputCheckTxtVoucher.setText("Valid");
                outputCheckTxtVoucher.setTextColor(ContextCompat.getColor(requireActivity(), R.color.green));
                confirmVoucher(res.getVoucher());

            }

            @Override
            public void onFailure(Call<VoucherResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                invisibleProgressBar();
            }
        });
    }

    private void confirmVoucher(Voucher voucher) {
        if (!voucher.getJenis_kamar().equals("")) {
            String voucherRoom = voucher.getJenis_kamar().toUpperCase();
            if (!voucherRoom.contains(room.getRoomType())) {
                new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme)
                        .setTitle("Jenis Kamar Voucher Tidak Sama")
                        .setMessage("Voucher berlaku untuk Room " + voucher.getJenis_kamar() + ", lanjutkan transaksi ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        }

    }

    private void setVisitorData() {
        infoNamaMember.setText(member.getFullName().toUpperCase());
        infoNamaMember.setEnabled(false);
        infoPhoneMember.setText(member.getHp());
        infoPhoneMember.setEnabled(false);
        infoCheckinRoomType.setText(room.getRoomType().toUpperCase() + " " + room.getRoomCode());
        infoCheckinRoomType.setEnabled(false);
        infoCodeMember.setText(member.getMemberCode());
        infoCodeMember.setEnabled(false);
        if (member.getSex().equals("L")) {
            infoGenderMember.setText("PRIA");
        } else {
            infoGenderMember.setText("PEREMPUAN");
        }
        infoDurasiCheckin.setText(AppUtils.formatSisaWaktuCheckin(room));
        infoDurasiCheckin.setEnabled(false);
       /* if (room.getQf1() > 0 || room.getQf2() > 0 || room.getQf3() > 0 || room.getQf4() > 0 ||
                room.getQm1() > 0 || room.getQm2() > 0 || room.getQm3() > 0 || room.getQm4() > 0) {*/
        inputCountQmAnak.setText(String.valueOf(room.getQm1()));
        inputCountQmRemaja.setText(String.valueOf(room.getQm2()));
        inputCountQmDewasa.setText(String.valueOf(room.getQm3()));
        inputCountQmTua.setText(String.valueOf(room.getQm4()));
        inputCountQfAnak.setText(String.valueOf(room.getQf1()));
        inputCountQfRemaja.setText(String.valueOf(room.getQf2()));
        inputCountQfDewasa.setText(String.valueOf(room.getQf3()));
        inputCountQfTua.setText(String.valueOf(room.getQf4()));
        qfAnak = room.getQf1();
        qfRemaja = room.getQf2();
        qfDewasa = room.getQf3();
        qfTua = room.getQf4();
        qmAnak = room.getQm1();
        qmRemaja = room.getQm2();
        qmDewasa = room.getQm3();
        qmTua = room.getQm4();

        inputDesc.getEditText().setText(room.getDesc());
        inputEventDesc.getEditText().setText(room.getEventDesc());
        inputEventOwner.getEditText().setText(member.getFullName().toUpperCase());

        if (!room.isRoomNotLobby()) {
            inputEventDesc.setVisibility(View.GONE);
            layoutPromoRoom.setVisibility(View.GONE);
            buttonPromoRoom.setVisibility(View.GONE);
            kodePromoRoom.setVisibility(View.GONE);
            infoDurasiCheckin.setVisibility(View.GONE);
        }

        double dpPayment = room.getDpPaymentNominal();
        if (dpPayment > 0) {
            inputDpNominal.getEditText().setText(String.valueOf(dpPayment));
            inputDpNominal.setEnabled(false);

            if (room.getDpPaymentType().equals("DEBET") || room.getDpPaymentType().equals("CREDIT")) {
                inputDpTextCard.setText(room.getDpCardType());
                inputDpTextEdc.setText(room.getDpEdc());
                buttonChooseCard.setEnabled(false);
                buttonChooseCard.setText("Card ");
                buttonChooseEdc.setEnabled(false);
                buttonChooseEdc.setText("Edc ");

                inputDpNamaDebetCredit.getEditText()
                        .setText(room.getDpCardName());
                inputDpCardNumberDebetCredit.getEditText()
                        .setText(room.getDpCardNumber());
                inputDpApprovalCodeDebetCredit.getEditText()
                        .setText(room.getDpCardApprovalNumber());

                if (room.getDpPaymentType().equals("DEBET")) {
                    inputDpGroup.check(R.id.um_debet);
                    dpType = "DEBET CARD";
                    isUseDP = true;
                    isUseDpCard = true;
                    isUseDpTransfer = false;
                    viewDpTransfer.setVisibility(View.GONE);
                    inputDpNominal.setVisibility(View.VISIBLE);
                    viewDpDebetCredit.setVisibility(View.VISIBLE);
                }

                if (room.getDpPaymentType().equals("CREDIT")) {
                    inputDpGroup.check(R.id.um_credit);
                    dpType = "CREDIT CARD";
                    isUseDP = true;
                    isUseDpCard = true;
                    isUseDpTransfer = false;
                    viewDpTransfer.setVisibility(View.GONE);
                    inputDpNominal.setVisibility(View.VISIBLE);
                    viewDpDebetCredit.setVisibility(View.VISIBLE);
                }

            } else if (room.getDpPaymentType().equals("CASH")) {
                inputDpGroup.check(R.id.um_cash);
                dpType = "CASH";
                isUseDP = true;
                isUseDpCard = false;
                isUseDpTransfer = false;
                viewDpTransfer.setVisibility(View.GONE);
                inputDpNominal.setVisibility(View.VISIBLE);
                viewDpDebetCredit.setVisibility(View.GONE);

            } else if(room.getDpPaymentType().equals("TRANSFER")){
                inputDpGroup.check(R.id.um_transfer);
                dpType = "TRANSFER";
                isUseDP = true;
                isUseDpCard = false;
                isUseDpTransfer = true;
                inputDpNominal.setVisibility(View.VISIBLE);
                viewDpTransfer.setVisibility(View.VISIBLE);
                viewDpDebetCredit.setVisibility(View.GONE);
            }
            for (int i = 0; i < inputDpGroup.getChildCount(); i++) {
                inputDpGroup.getChildAt(i).setEnabled(false);
            }
        } else {
            dpType = "";
            isUseDP = false;
            isUseDpCard = false;
            isUseDpTransfer = false;
            viewDpDebetCredit.setVisibility(View.GONE);
            inputDpNominal.setVisibility(View.GONE);
            viewDpTransfer.setVisibility(View.GONE);
        }

    }


    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Lengkapi Data"));
    }

    private void initDownPaymentUI() {
        initEdcType();
        inputDpGroup.clearCheck();
        inputDpGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (typesListEdc.size() < 1) {
                initEdcType();
            }
            if (i == R.id.um_non) {
                dpType = "";
                isUseDP = false;
                isUseDpCard = false;
                isUseDpTransfer = false;
                viewDpDebetCredit.setVisibility(View.GONE);
                inputDpNominal.setVisibility(View.GONE);
                viewDpTransfer.setVisibility(View.GONE);
            }
            if (i == R.id.um_cash) {
                dpType = "CASH";
                isUseDP = true;
                isUseDpCard = false;
                inputDpNominal.setVisibility(View.VISIBLE);
                viewDpDebetCredit.setVisibility(View.GONE);
                viewDpTransfer.setVisibility(View.GONE);
            }
            if (i == R.id.um_debet) {
                dpType = "DEBET CARD";
                isUseDP = true;
                isUseDpCard = true;
                isUseDpTransfer = false;
                inputDpNominal.setVisibility(View.VISIBLE);
                viewDpDebetCredit.setVisibility(View.VISIBLE);
                viewDpTransfer.setVisibility(View.GONE);
            }
            if (i == R.id.um_credit) {
                dpType = "CREDIT CARD";
                isUseDP = true;
                isUseDpCard = true;
                isUseDpTransfer = false;
                inputDpNominal.setVisibility(View.VISIBLE);
                viewDpDebetCredit.setVisibility(View.VISIBLE);
                viewDpTransfer.setVisibility(View.GONE);
            }
            if (i == R.id.um_transfer) {
                dpType = "TRANSFER";
                isUseDP = true;
                isUseDpCard = false;
                isUseDpTransfer = true;
                inputDpNominal.setVisibility(View.VISIBLE);
                viewDpDebetCredit.setVisibility(View.GONE);
                viewDpTransfer.setVisibility(View.VISIBLE);
            }

        });

        buttonDateDpExpired.setVisibility(View.GONE);
        inputDpExpireDate.setVisibility(View.GONE);
        buttonDateDpExpired.setOnClickListener(view -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            inputDpExpireDate.getEditText()
                                    .setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });
    }

    private void initEdcType() {
        visibleProgressBar();
        Call<EdcTypeResponse> call = memberClient.getEdc();
        call.enqueue(new Callback<EdcTypeResponse>() {
            @Override
            public void onResponse(Call<EdcTypeResponse> call, Response<EdcTypeResponse> response) {

                EdcTypeResponse res = response.body();
                if (!res.isOkay()) {
                    res.displayMessage(requireActivity());

                    return;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        invisibleProgressBar();
                        typesListEdc.addAll(res.getTypeEdcs());
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<EdcTypeResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean isInputValid(EditText input) {
        String inputVal = input.getText().toString().trim();
        if (inputVal.isEmpty()) {

            //input.setError("Harap di isi");
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }

    private boolean isInputValidApprovalCode(EditText input) {
        String inputVal = input.getText().toString().trim();
        if (inputVal.isEmpty()) {

            //input.setError("Harap di isi");
            return false;
        } else {
            //input.setError(null);
            if (inputVal.length() != 6) {
                return false;
            }
            return true;
        }
    }

    private boolean isInputValidNumberCard(EditText input) {
        String inputVal = input.getText().toString().trim();
        if (inputVal.isEmpty()) {

            //input.setError("Harap di isi");
            return false;
        } else {
            //input.setError(null);
            if (inputVal.length() != 4) {
                return false;
            }
            return true;
        }
    }

    private void submitCheckin() {

        List<String> promoSelected = new ArrayList<>();
        if (!choicePromoFood.equals(EMPTY_PROMO)) {
            promoSelected.add(choicePromoFood);
        }
        if (!choicePromoRoom.equals(EMPTY_PROMO)) {
            promoSelected.add(choicePromoRoom);
        }

        if (isUseDP) {
            if (!isInputValid(inputDpNominal.getEditText())) {
                Toast
                        .makeText(requireActivity(), "Mohon Periksa Kembali Nominal Uang Muka", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                nominalDp = inputDpNominal
                        .getEditText()
                        .getText()
                        .toString()
                        .replaceAll("[.]", "");
            } catch (Exception e) {
                Toasty
                        .warning(requireActivity(), "Mohon Isi Nominal", Toast.LENGTH_SHORT)
                        .show();
            }


            keteranganUangMuka = dpType;
            Toast
                    .makeText(requireActivity(), keteranganUangMuka, Toast.LENGTH_SHORT)
                    .show();
            if (isUseDpCard) {

                if (!isInputValid(inputDpNamaDebetCredit.getEditText()) ||
                        !isInputValidNumberCard(inputDpCardNumberDebetCredit.getEditText()) ||
                        !isInputValidApprovalCode(inputDpApprovalCodeDebetCredit.getEditText()) ||
                        null == choiceTypeEdc ||
                        choicecard.equals(EMPTY_CARD_DP)) {
                    Toasty
                            .warning(requireActivity(), "Mohon Periksa Kembali Input Uang Muka", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                input1JenisKartu = choicecard;
                input2Nama = inputDpNamaDebetCredit.getEditText().getText().toString();
                input3NomorKartu = inputDpCardNumberDebetCredit.getEditText().getText().toString();
                input4NomorApproval = inputDpApprovalCodeDebetCredit.getEditText().getText().toString();
                edcMachine = choiceTypeEdc.getEdcCode();
            } else if (isUseDpTransfer) {
                if (!isInputValid(inputBankNameTf.getEditText()) ||
                        !isInputValid(inputBankAccountNameTf.getEditText()) ) {
                    Toasty
                            .warning(requireActivity(), "Mohon Periksa Kembali Input Uang Muka", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                input1JenisKartu = inputBankNameTf.getEditText().getText().toString();
                input2Nama =  inputBankAccountNameTf.getEditText().getText().toString();
                edcMachine = "0";
            }
        }

        if (inputCodeVoucher.getEditText().getText().toString().length() > 0) {
            finalVoucherCode = inputCodeVoucher.getEditText().getText().toString();
        }else{
            finalVoucherCode = "";
        }


        if (isEmptyVisitor()) {
            Toasty.warning(requireActivity(), "Anda belum input jumlah tamu ", Toast.LENGTH_SHORT, true)
                    .show();
            return;
        }

        new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme)
                .setTitle("Simpan Data Checkin")
                .setMessage("Anda ingin melanjutkan")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       visibleProgressBar();
                        Call<RoomOrderResponse> req = roomOrderClient
                                .submitEditOrderRoom(
                                        room.getRoomCode(),
                                        String.valueOf(qmAnak),
                                        String.valueOf(qmRemaja),
                                        String.valueOf(qmDewasa),
                                        String.valueOf(qmTua),
                                        String.valueOf(qfAnak),
                                        String.valueOf(qfRemaja),
                                        String.valueOf(qfDewasa),
                                        String.valueOf(qfTua),
                                        member.getHp(),
                                        nominalDp,
                                        inputDesc.getEditText().getText().toString(),
                                        inputEventDesc.getEditText().getText().toString(),
                                        inputEventOwner.getEditText().getText().toString(),
                                        USER_FO.getUserId(),
                                        finalVoucherCode,
                                        keteranganUangMuka,
                                        input1JenisKartu,
                                        input2Nama,
                                        input3NomorKartu,
                                        input4NomorApproval,
                                        edcMachine,
                                        "",
                                        promoSelected
                                );


                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                                invisibleProgressBar();
                                RoomOrderResponse res = response.body();
                                res.displayMessage(requireActivity());
                                if (!res.isOkay()) {
                                    return;
                                }
                                navToMain(res.getRoomOrder().getCheckinRoom().getRoomRcp());
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                                invisibleProgressBar();
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

    private boolean isEmptyVisitor() {
        int total = qmAnak + qmDewasa + qmRemaja + qmTua +
                qfAnak + qfDewasa + qfRemaja + qfTua;
        if (total < 1) {
            return true;
        }
        return false;
    }


    private void navToMain(String rcp) {
        otherViewModel.checkinSlip(BASE_URL, rcp).observe(getViewLifecycleOwner(), data->{
            SharedPreferences sharedPref = requireActivity().getSharedPreferences(getString(R.string.preference_print), requireActivity().MODE_PRIVATE);
            int printSetting = sharedPref.getInt(getString(R.string.preference_print), 2);
            if(printSetting == 1){
                Printer printer = new Printer();
                printer.printerCheckinSlip(data, requireActivity());
            }else if(printSetting == 4){
                Printer58 printer = new Printer58();
                printer.printerCheckinSlip(data, requireActivity());
            }
            Navigation
                    .findNavController(requireView())
                    .navigate(OperasionalCheckinAddInfoFragmentDirections
                            .actionNavOperasionalCheckinAddInfoFragmentToNavOperasionalFragment());
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!GlobalBus.getBus().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        GlobalBus.getBus().unregister(this);
    }

    /*  @Override
      public void onStart() {
          super.onStart();
          if (!GlobalBus.getBus().isRegistered(this))
              EventBus.getDefault().register(this);
      }

      @Override
      public void onStop() {
          super.onStop();
          GlobalBus.getBus().unregister(this);
      }
 */
    @Subscribe
    public void setDataQrCode(EventsWrapper.ScanResult scanResult) {
        invisibleProgressBar();
        if (!scanResult.isSuccess()) {
            isVoucherScanActive = false;
            return;
        }
        if (isVoucherScanActive) {
            buttonScanQRCodeVoucher.setVisibility(View.GONE);
            buttonResetCodeVoucher.setVisibility(View.VISIBLE);

            inputCodeVoucher.setFocusable(true);
            inputCodeVoucher.setEnabled(false);
            inputCodeVoucher.getEditText().setText(scanResult.getData());
            isVoucherScanActive = false;
        }

    }

    private void visibleProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void invisibleProgressBar(){

        progressBar.setVisibility(View.GONE);
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


}