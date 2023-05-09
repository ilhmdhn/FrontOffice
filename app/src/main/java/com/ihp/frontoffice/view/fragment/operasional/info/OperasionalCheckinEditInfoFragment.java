package com.ihp.frontoffice.view.fragment.operasional.info;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.ihp.frontoffice.events.DataBusEvent;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
import com.ihp.frontoffice.data.remote.UserClient;
import com.ihp.frontoffice.data.remote.CheckinDirectClient;
import com.ihp.frontoffice.data.remote.respons.EdcTypeResponse;
import com.ihp.frontoffice.data.remote.respons.MemberResponse;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.data.remote.respons.UserResponse;
import com.ihp.frontoffice.data.remote.respons.VoucherResponse;
import com.ihp.frontoffice.data.repository.IhpRepository;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.helper.QRScanType;
import com.ihp.frontoffice.helper.UserAuthRole;
import com.ihp.frontoffice.view.listadapter.ListEdcTypeAdapter;
import com.ihp.frontoffice.view.listadapter.ListPromoInventoryAdapter;
import com.ihp.frontoffice.view.listadapter.ListPromoRoomAdapter;
import com.ihp.frontoffice.viewmodel.InventoryPromoViewModel;
import com.ihp.frontoffice.viewmodel.OtherViewModel;
import com.ihp.frontoffice.viewmodel.RoomPromoViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalCheckinEditInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalCheckinEditInfoFragment extends Fragment {

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
    int qmAnak = 0;
    int qmRemaja = 0;
    @BindView(R.id.checkinMinQmDewasa)
    ImageView inputMinQmDewasa;
    @BindView(R.id.checkinPlusQmDewasa)
    ImageView inputPlusQmDewasa;
    @BindView(R.id.checkinCountQmDewasa)
    EditText inputCountQmDewasa;
    int qmDewasa = 0;
    int qmTua = 0;
    int qfAnak = 0;
    int qfRemaja = 0;
    int qfDewasa = 0;
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
    @BindView(R.id.layout_head_promo)
    View layoutPromoRoom;

    @BindView(R.id.btn_promo_room)
    Button buttonPromoRoom;

    @BindView(R.id.btn_cancel_promo_room)
    Button btnCancelPromoRoom;

    @BindView(R.id.btn_cancel_promo_food)
    Button btnCancelPromoFood;

    @BindView(R.id.txt_kode_promo_room)
    TextView kodePromoRoom;

    @BindView(R.id.bttnPromoFood)
    Button buttonPromoFood;

    @BindView(R.id.txtKodePromoFood)
    TextView kodePromoFood;

    @BindView(R.id.btn_submit)
    AppCompatButton buttonSubmit;

    //Linked Member Dummy
    @BindView(R.id.button_scan_qrcode_member)
    AppCompatButton buttonScanQRCodeMember;
    @BindView(R.id.button_check_code_member)
    AppCompatButton buttonCheckCodeMember;
    @BindView(R.id.checkin_reset_code_member)
    AppCompatButton buttonResetCodeMember;
    @BindView(R.id.checkin_input_code_member)
    TextInputLayout inputCodeMember;
    @BindView(R.id.view_output_cek_code_member)
    View viewOutputCekCodeMember;
    @BindView(R.id.img_output_cek_code_member)
    ImageView outputCheckImgMember;
    @BindView(R.id.txt_output_cek_code_member)
    TextView outputCheckTxtMember;
    private boolean isMemberScanActive;
    @BindView(R.id.extendsMinHours)
    ImageView btnMinHours;
    @BindView(R.id.extendsPlusHours)
    ImageView btnPlusHour;
    @BindView(R.id.extendsCountHours)
    TextView tvMinHour;
    @BindView(R.id.btn_reduce_duration)
    Button btnReduceDuration;

    Integer jamMinus = 0;
    private EditText _usernameTxt, _passwordTxt;
    private TextInputLayout _parentPassword;


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
    private OtherViewModel otherViewModel;
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
    private Room currentRoomCheckin;

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
    private String finalMemberRefCode = "";
    private String errorMessageVoucher;
    private final String TAG = "OperasionalCheckinEditInfoFragment";
    private static String BASE_URL;
    private User USER_FO;
    private String current="";
    private IhpRepository ihpRepository;

    public OperasionalCheckinEditInfoFragment() {
        // Required empty public constructor
    }

    public static OperasionalCheckinEditInfoFragment newInstance() {
        OperasionalCheckinEditInfoFragment fragment = new OperasionalCheckinEditInfoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomOrder = OperasionalCheckinEditInfoFragmentArgs
                .fromBundle(getArguments())
                .getRoomOrder();
        member = roomOrder.getMember();
        currentRoomCheckin = roomOrder.getCheckinRoom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operasional_checkin_edit_info, container, false);
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

        otherViewModel = new ViewModelProvider(requireActivity()).get(OtherViewModel.class);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isVoucherScanActive = false;

        ihpRepository = new IhpRepository();

        EMPTY_PROMO = getResources().getString(R.string.kode_promo);
        choicePromoRoom = EMPTY_PROMO;
        choicePromoFood = EMPTY_PROMO;

        EMPTY_EDC_DP = getResources().getString(R.string.edc_mesin);
        EMPTY_CARD_DP = getResources().getString(R.string.type_card);
        choicecard = EMPTY_CARD_DP;

        btnPlusHour.setOnClickListener(view ->{
            jamMinus++;
            tvMinHour.setText(String.valueOf(jamMinus));
        });

        btnMinHours.setOnClickListener(view -> {
            if (jamMinus >= 1){
                jamMinus--;
            }
            tvMinHour.setText(String.valueOf(jamMinus));
        });

        int promoRoomSize = roomOrder.getRoomPromos().size();
        if (promoRoomSize > 0) {
            btnReduceDuration.setOnClickListener(view ->{
            Toasty.warning(requireActivity(), "Hapus promo terlebih dahulu, setelah durasi dikurangi silahkan input promo kembali", Toasty.LENGTH_SHORT, true).show();
            });
        } else{
            btnReduceDuration.setOnClickListener(view ->{
                reduceDuration();
            });
        }

        btnCancelPromoRoom.setOnClickListener(view -> {
            removePromo();
        });

        btnCancelPromoFood.setOnClickListener(view ->{
            removePromoFood();
        });

        inputDpNominal.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().length()>11){
                    inputDpNominal.getEditText().setText(current);
                    inputDpNominal.getEditText().setSelection(current.length());
                    Toasty
                            .warning(requireActivity(),"Pembayaran Maksimal Ratusan Juta")
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

        initVoucherValidationUI();
        initQmUI();
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

        initMemberValidationUI();

        if(inputCodeVoucher.getEditText().getText().toString() == currentRoomCheckin.getCodeMemberRef()){
            inputCodeVoucher.getEditText().setText("");
        }
    }

    private void reduceDuration() {
        if(currentRoomCheckin.getRoomResidualCheckinHoursTime() - jamMinus < 1){
            if (currentRoomCheckin.getRoomResidualCheckinHoursTime() - jamMinus < 0){
                Toasty.warning(requireActivity(), "Pastikan setelah dikurangi sisa waktu lebih dari 10 menit", Toasty.LENGTH_SHORT, true).show();
                return;
            } else if(currentRoomCheckin.getRoomResidualCheckinHoursMinutesTime()<10){
                Toasty.warning(requireActivity(), "Pastikan setelah dikurangi sisa waktu lebih dari 10 menit", Toasty.LENGTH_SHORT, true).show();
                return;
            }
        }

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_otorisasi, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
        AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);

        _usernameTxt = dialogView.findViewById(R.id.input_username_otorisasi);
        _passwordTxt = dialogView.findViewById(R.id.input_password_otorisasi);
        _parentPassword = dialogView.findViewById(R.id.ed_parent_password);

        otherViewModel.getJumlahApproval(BASE_URL, USER_FO.getUserId()).observe(getViewLifecycleOwner(), data ->{
            boolean kasirApproval = data.getState();
            if (kasirApproval) {
//                alert dialog

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme);
                builder.setMessage(R.string.reduce_duration);
// Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
                        Call<com.ihp.frontoffice.data.remote.respons.Response> responseReduceDuration = roomOrderClient.reduceDurationClient(currentRoomCheckin.getRoomRcp(), jamMinus.toString(), USER_FO.getUserId());
                        responseReduceDuration.enqueue(new Callback<com.ihp.frontoffice.data.remote.respons.Response>() {
                            @Override
                            public void onResponse(Call<com.ihp.frontoffice.data.remote.respons.Response> call, Response<com.ihp.frontoffice.data.remote.respons.Response> responsee) {
                                if (responsee.isSuccessful()){
                                    if (responsee.body() != null) {
                                        if(responsee.body().getState()){
                                            ihpRepository.submitApproval(BASE_URL, USER_FO.getUserId(),USER_FO.getLevelUser(), currentRoomCheckin.getRoomCode(), "Reduce Duration Checkin");
                                            Toasty.info(requireActivity(), "Berhasil Mengurangi Durasi, Silahkan Lengkapi Data Checkin", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toasty.info(requireActivity(), "Ulangi Proses", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<com.ihp.frontoffice.data.remote.respons.Response> call, Throwable t) {
                                Toasty.error(requireActivity(), "Mengurangi Durasi" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
// Set other dialog properties

// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

//                alert dialog
            }
            else{
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(dialogInterface -> {
                    buttonOk.setOnClickListener(it -> {
                        String email = _usernameTxt.getText().toString();
                        String password = _passwordTxt.getText().toString();
                        if (email.isEmpty() && password.isEmpty()) {
                            Toasty.warning(requireActivity(), "Anda belum input user dan password ", Toast.LENGTH_SHORT, true)
                                    .show();
                            return;
                        }
                        //_loginProgress.setVisibility(View.VISIBLE);
                        UserClient userClient = ApiRestService.getClient(BASE_URL).create(UserClient.class);
                        Call<UserResponse> call = userClient.login(email, password);
                        call.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                UserResponse res = response.body();
                                //_loginProgress.setVisibility(View.GONE);
                                res.displayMessage(requireActivity());
                                if (res.isOkay()) {
                                    User user = res.getUser();
                                    if (UserAuthRole.isAllowReduceCheckinDuration(user)) {
                                        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
                                        Call<com.ihp.frontoffice.data.remote.respons.Response> responseReduceDuration = roomOrderClient.reduceDurationClient(currentRoomCheckin.getRoomRcp(), jamMinus.toString(), USER_FO.getUserId());
                                        responseReduceDuration.enqueue(new Callback<com.ihp.frontoffice.data.remote.respons.Response>() {
                                            @Override
                                            public void onResponse(Call<com.ihp.frontoffice.data.remote.respons.Response> call, Response<com.ihp.frontoffice.data.remote.respons.Response> responsee) {
                                                if (responsee.isSuccessful()){
                                                    if (responsee.body() != null) {
                                                        if(responsee.body().getState()){
                                                            ihpRepository.submitApproval(BASE_URL, USER_FO.getUserId(), USER_FO.getLevelUser(),currentRoomCheckin.getRoomCode(), "Reduce Checkin Duration");
                                                            Toasty.info(requireActivity(), "Berhasil Mengurangi Durasi, Silahkan Lengkapi Data Checkin", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toasty.info(requireActivity(), "Ulangi Proses", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<com.ihp.frontoffice.data.remote.respons.Response> call, Throwable t) {
                                                Toasty.error(requireActivity(), "Mengurangi Durasi" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toasty.warning(requireActivity(), "User tidak dapat melakukan operasi ini", Toast.LENGTH_SHORT, true)
                                                .show();
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                //_loginProgress.setVisibility(View.GONE);
                                Toasty.error(requireActivity(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT, true)
                                        .show();
                            }
                        });
                        alertDialog.dismiss();
                    });

                    buttonCancel.setOnClickListener(view -> {
                        //_loginProgress.setVisibility(View.GONE);
                        alertDialog.dismiss();
                    });
                });

                alertDialog.show();
            }
        });
    }

    private void removePromo() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_otorisasi, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
        AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);

        _usernameTxt = dialogView.findViewById(R.id.input_username_otorisasi);
        _passwordTxt = dialogView.findViewById(R.id.input_password_otorisasi);
        _parentPassword = dialogView.findViewById(R.id.ed_parent_password);

        otherViewModel.getJumlahApproval(BASE_URL, USER_FO.getUserId()).observe(getViewLifecycleOwner(), data ->{
            boolean kasirApproval = data.getState();
            if (kasirApproval) {
//                alert dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage(R.string.remove_promo);
// Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ihpRepository.submitApproval(BASE_URL, USER_FO.getUserId(), USER_FO.getLevelUser(), currentRoomCheckin.getRoomCode(), "Remove Promo");
                        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
                        CheckinDirectClient checkinDirectClient = ApiRestService.getClient(BASE_URL).create(CheckinDirectClient.class);
                        Call<com.ihp.frontoffice.data.remote.respons.Response> responseRemovePromo = checkinDirectClient.removePromo(currentRoomCheckin.getRoomRcp());
                        responseRemovePromo.enqueue(new Callback<com.ihp.frontoffice.data.remote.respons.Response>() {
                            @Override
                            public void onResponse(Call<com.ihp.frontoffice.data.remote.respons.Response> call, Response<com.ihp.frontoffice.data.remote.respons.Response> responsee) {
                                if (responsee.isSuccessful()){
                                    if (responsee.body() != null) {
                                        if(responsee.body().getState()){
                                            roomOrder.getRoomPromos().clear();

                                            Navigation.findNavController(buttonSubmit)
                                                    .navigate(
                                                            OperasionalCheckinEditInfoFragmentDirections
                                                                    .actionNavOperasionalCheckinEditInfoFragmentSelf(roomOrder)
                                                    );
                                            Toasty.info(requireActivity(), "Berhasil Menghapus Promo, Silahkan Lengkapi Data Checkin", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toasty.info(requireActivity(), "Ulangi Hapus Promo", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<com.ihp.frontoffice.data.remote.respons.Response> call, Throwable t) {
                                Toasty.error(requireActivity(), "Gagal Mengapus Promo " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
// Set other dialog properties

// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

//                alert dialog
            } else{
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(dialogInterface -> {
                    buttonOk.setOnClickListener(it -> {
                        String email = _usernameTxt.getText().toString();
                        String password = _passwordTxt.getText().toString();
                        if (email.isEmpty() || password.isEmpty()) {
                            Toasty.warning(requireActivity(), "Anda belum input user dan password ", Toast.LENGTH_SHORT, true)
                                    .show();
                            return;
                        }
                        //_loginProgress.setVisibility(View.VISIBLE);
                        UserClient userClient = ApiRestService.getClient(BASE_URL).create(UserClient.class);
                        Call<UserResponse> call = userClient.login(email, password);
                        call.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                UserResponse res = response.body();
                                //_loginProgress.setVisibility(View.GONE);
                                res.displayMessage(requireActivity());
                                if (res.isOkay()) {
                                    User user = res.getUser();
                                    if (UserAuthRole.isAllowCancelPromotion(user)) {
                                        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
                                        CheckinDirectClient checkinDirectClient = ApiRestService.getClient(BASE_URL).create(CheckinDirectClient.class);
                                        Call<com.ihp.frontoffice.data.remote.respons.Response> responseRemovePromo = checkinDirectClient.removePromo(currentRoomCheckin.getRoomRcp());
                                        responseRemovePromo.enqueue(new Callback<com.ihp.frontoffice.data.remote.respons.Response>() {
                                            @Override
                                            public void onResponse(Call<com.ihp.frontoffice.data.remote.respons.Response> call, Response<com.ihp.frontoffice.data.remote.respons.Response> responsee) {
                                                if (responsee.isSuccessful()){
                                                    if (responsee.body() != null) {
                                                        if(responsee.body().getState()){
                                                            roomOrder.getRoomPromos().clear();
                                                            alertDialog.dismiss();
                                                            ihpRepository.submitApproval(BASE_URL, user.getUserId(), user.getLevelUser(), currentRoomCheckin.getRoomCode(), "Remove Promo");
                                                            Navigation.findNavController(buttonSubmit)
                                                                    .navigate(
                                                                            OperasionalCheckinEditInfoFragmentDirections
                                                                                    .actionNavOperasionalCheckinEditInfoFragmentSelf(roomOrder)
                                                                    );
                                                            Toasty.info(requireActivity(), "Berhasil Menghapus Promo, Silahkan Lengkapi Data Checkin", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toasty.info(requireActivity(), "Ulangi Hapus Promo", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<com.ihp.frontoffice.data.remote.respons.Response> call, Throwable t) {
                                                Toasty.error(requireActivity(), "Gagal Mengapus Promo " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toasty.warning(requireActivity(), "User tidak dapat melakukan operasi ini", Toast.LENGTH_SHORT, true)
                                                .show();
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                //_loginProgress.setVisibility(View.GONE);
                                Toasty.error(requireActivity(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT, true)
                                        .show();
                            }
                        });

                    });

                    buttonCancel.setOnClickListener(view -> {
                        //_loginProgress.setVisibility(View.GONE);
                        alertDialog.dismiss();
                    });
                });

                alertDialog.show();
            }
        });
    }

    private void removePromoFood(){
        otherViewModel.getJumlahApproval(BASE_URL, USER_FO.getUserId()).observe(getViewLifecycleOwner(), dataResponse->{
            boolean kasirApproval = dataResponse.getState();

            if(kasirApproval){
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

                builder.setMessage("Hapus Promo FnB");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        otherViewModel.removePromoFnB(BASE_URL, currentRoomCheckin.getRoomRcp()).observe(getViewLifecycleOwner(), removePromoResponse->{
                            if(removePromoResponse.getState()){
                                Toasty.info(requireActivity(), removePromoResponse.getMessage()).show();
                                roomOrder.getInventoryPromos().clear();
                                ihpRepository.submitApproval(BASE_URL, USER_FO.getUserId(), USER_FO.getLevelUser(),currentRoomCheckin.getRoomCode(), "Remove FnB Promo");
                                Navigation.findNavController(buttonSubmit)
                                        .navigate(
                                                OperasionalCheckinEditInfoFragmentDirections
                                                        .actionNavOperasionalCheckinEditInfoFragmentSelf(roomOrder)
                                        );
                            }else{
                                Toasty.error(requireActivity(), removePromoResponse.getMessage()).show();
                            }
                            dialog.dismiss();
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                builder.show();
            }else{
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme);
                LayoutInflater dialogInflater = this.getLayoutInflater();

                View dialogView = dialogInflater.inflate(R.layout.dialog_otorisasi, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);

                AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
                AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);

                _usernameTxt = dialogView.findViewById(R.id.input_username_otorisasi);
                _passwordTxt = dialogView.findViewById(R.id.input_password_otorisasi);
                _parentPassword = dialogView.findViewById(R.id.ed_parent_password);

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setOnShowListener(dialogInterface->{
                    buttonOk.setOnClickListener(it ->{
                        String email = _usernameTxt.getText().toString();
                        String password = _passwordTxt.getText().toString();

                        if(email.isEmpty() || password.isEmpty()){
                            Toasty.warning(requireActivity(), "email/ password empty").show();
                            return;
                        }

                        otherViewModel.checkUser(BASE_URL, email, password).observe(getViewLifecycleOwner(), userResponse->{
                            if(userResponse.isOkay()){
                                otherViewModel.removePromoFnB(BASE_URL, currentRoomCheckin.getRoomRcp()).observe(getViewLifecycleOwner(), removeResponse->{
                                    if(removeResponse.getState()){
                                        Toasty.info(requireActivity(), removeResponse.getMessage()).show();
                                        roomOrder.getInventoryPromos().clear();
                                        ihpRepository.submitApproval(BASE_URL, USER_FO.getUserId(), USER_FO.getLevelUser(),currentRoomCheckin.getRoomCode(), "Remove FnB Promo");
                                        Navigation.findNavController(buttonSubmit)
                                                .navigate(
                                                        OperasionalCheckinEditInfoFragmentDirections
                                                                .actionNavOperasionalCheckinEditInfoFragmentSelf(roomOrder)
                                                );
                                    }else{
                                        Toasty.error(requireActivity(), removeResponse.getMessage()).show();
                                    }
                                });
                            }else{
                                Toasty.warning(requireActivity(), userResponse.getMessage()).show();
                            }
                            alertDialog.dismiss();
                        });

                    });
                    buttonCancel.setOnClickListener(view->{
                        alertDialog.dismiss();
                    });
                });
                alertDialog.show();
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

        new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme)
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void visibleProgressBar(boolean isVisible){
        if(isVisible){
            progressBar.setVisibility(View.VISIBLE);
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else{
            progressBar.setVisibility(View.GONE);
            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }



    private void promoFoodSetupData() {
        promoFoodList.clear();
        visibleProgressBar(true);
        inventoryPromoViewModel
                .getFoodPromoResponseMutableLiveData(currentRoomCheckin.getRoomType(), currentRoomCheckin.getRoomCode())
                .observe(getViewLifecycleOwner(), foodPromoResponse -> {
                    visibleProgressBar(false);
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

        new MaterialAlertDialogBuilder(requireActivity(), R.style.CustomAlertDialogDarkForNoActionBAr)
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        visibleProgressBar(true);
        roomPromoViewModel
                .getRoomPromoResponseMutableLiveData(currentRoomCheckin.getRoomType())
                .observe(getViewLifecycleOwner(), roomPromoResponse -> {
                    visibleProgressBar(false);
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

        new MaterialAlertDialogBuilder(requireActivity(), R.style.CustomAlertDialogDarkForNoActionBAr)
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        if (total > currentRoomCheckin.getRoomCapacity()) {
            int over = total - currentRoomCheckin.getRoomCapacity();
            overpackWarning.setVisibility(View.VISIBLE);
            overpackWarning.setText(" Overpack " + String.valueOf(over));
        } else {
            overpackWarning.setVisibility(View.GONE);
            overpackWarning.setText("");
        }
    }

    private void initQmUI() {

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
            qmDewasa = qmDewasa + 1;
            inputCountQmDewasa.setText(String.valueOf(qmDewasa));
          /*  if (qmDewasa == 10) {
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }

            if (qmDewasa < 10) {
                qmDewasa = qmDewasa + 1;
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }*/
            countTotalVisitor();
        });

    }

    private void initVoucherValidationUI() {
        buttonScanQRCodeVoucher.setOnClickListener(view -> {
            if (!isVoucherScanActive) {
                try {
                    visibleProgressBar(true);
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
//          clean promo
            String codeVcr = inputCodeVoucher.getEditText().getText().toString();
            if (codeVcr.equals("") || codeVcr == "") {
                Toast.makeText(requireActivity(),
                        "Kode Masih Kosong",
                        Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            visibleProgressBar(true);

            Call<VoucherResponse> call = memberClient.checkVoucherMembership( codeVcr);
            call.enqueue(new Callback<VoucherResponse>() {
                @Override
                public void onResponse(Call<VoucherResponse> call, Response<VoucherResponse> response) {
                    visibleProgressBar(false);
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
                                visibleProgressBar(true);
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
                    visibleProgressBar(false);
                }
            });
        });
    }

    private void checkLocalVoucher(String codVcr) {
        Call<VoucherResponse> call = memberClient.checkVoucherOutlet(codVcr);
        call.enqueue(new Callback<VoucherResponse>() {
            @Override
            public void onResponse(Call<VoucherResponse> call, Response<VoucherResponse> response) {
                visibleProgressBar(false);
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
                visibleProgressBar(false);
            }
        });
    }

    private void confirmVoucher(Voucher voucher) {
        if (!voucher.getJenis_kamar().equals("")) {
            String voucherRoom = voucher.getJenis_kamar().toUpperCase();
            if (!voucherRoom.contains(currentRoomCheckin.getRoomType())) {
                new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
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
        infoCheckinRoomType.setText(currentRoomCheckin.getRoomType().toUpperCase() + " " + currentRoomCheckin.getRoomCode());
        infoCheckinRoomType.setEnabled(false);
        infoCodeMember.setText(member.getMemberCode());
        infoCodeMember.setEnabled(false);
        if (member.getSex().equals("L")) {
            infoGenderMember.setText("PRIA");
        } else {
            infoGenderMember.setText("PEREMPUAN");
        }


        infoDurasiCheckin.setText(AppUtils.formatSisaWaktuCheckin(currentRoomCheckin));
        infoDurasiCheckin.setEnabled(false);
       /* if (room.getQf1() > 0 || room.getQf2() > 0 || room.getQf3() > 0 || room.getQf4() > 0 ||
                room.getQm1() > 0 || room.getQm2() > 0 || room.getQm3() > 0 || room.getQm4() > 0) {*/

        inputCountQmDewasa.setText(String.valueOf(currentRoomCheckin.getQm3()));

        qfAnak = currentRoomCheckin.getQf1();
        qfRemaja = currentRoomCheckin.getQf2();
        qfDewasa = currentRoomCheckin.getQf3();
        qfTua = currentRoomCheckin.getQf4();
        qmAnak = currentRoomCheckin.getQm1();
        qmRemaja = currentRoomCheckin.getQm2();
        qmDewasa = currentRoomCheckin.getQm3();
        qmTua = currentRoomCheckin.getQm4();

        inputDesc.getEditText().setText(currentRoomCheckin.getDesc());
        inputEventDesc.getEditText().setText(currentRoomCheckin.getEventDesc());
        inputEventOwner.getEditText().setText(currentRoomCheckin.getEventOwner());

        if (!currentRoomCheckin.isRoomNotLobby()) {
            inputEventDesc.setVisibility(View.GONE);
            layoutPromoRoom.setVisibility(View.GONE);
            buttonPromoRoom.setVisibility(View.GONE);
            kodePromoRoom.setVisibility(View.GONE);
            infoDurasiCheckin.setVisibility(View.GONE);
        }

        if(currentRoomCheckin.getCodeMemberRef().length()>1){
            inputCodeMember.setFocusable(true);
            inputCodeMember.setEnabled(false);
            inputCodeMember.getEditText().setText(currentRoomCheckin.getCodeMemberRef());
            buttonScanQRCodeMember.setVisibility(View.GONE);
            buttonCheckCodeMember.setVisibility(View.GONE);
            buttonResetCodeMember.setVisibility(View.GONE);
            outputCheckTxtMember.setText("Valid");
            outputCheckTxtMember.setVisibility(View.VISIBLE);
            outputCheckTxtMember.setTextColor(ContextCompat.getColor(requireActivity(), R.color.green));
        }


        long dpPayment = currentRoomCheckin.getDpPaymentNominal();
        if (dpPayment > 0) {
            inputDpNominal.getEditText().setText(String.valueOf(dpPayment));
            inputDpNominal.setEnabled(false);


            if (currentRoomCheckin.getDpPaymentType().equals("DEBET") || currentRoomCheckin.getDpPaymentType().equals("CREDIT")) {
                inputDpTextCard.setText(currentRoomCheckin.getDpCardType());
                inputDpTextEdc.setText(currentRoomCheckin.getDpEdc());
                buttonChooseCard.setEnabled(false);
                buttonChooseCard.setText("Card ");
                buttonChooseEdc.setEnabled(false);
                buttonChooseEdc.setText("Edc ");

                inputDpNamaDebetCredit.getEditText()
                        .setText(currentRoomCheckin.getDpCardName());
                inputDpCardNumberDebetCredit.getEditText()
                        .setText(currentRoomCheckin.getDpCardNumber());
                inputDpApprovalCodeDebetCredit.getEditText()
                        .setText(currentRoomCheckin.getDpCardApprovalNumber());

                if (currentRoomCheckin.getDpPaymentType().equals("DEBET")) {
                    inputDpGroup.check(R.id.um_debet);
                    dpType = "DEBET CARD";
                    isUseDP = true;
                    isUseDpCard = true;
                    inputDpNominal.setVisibility(View.VISIBLE);
                    viewDpDebetCredit.setVisibility(View.VISIBLE);
                    //choiceTypeEdc = currentRoomCheckin.getDpEdc();
                }

                if (currentRoomCheckin.getDpPaymentType().equals("CREDIT")) {
                    inputDpGroup.check(R.id.um_credit);
                    dpType = "CREDIT CARD";
                    isUseDP = true;
                    isUseDpCard = true;
                    inputDpNominal.setVisibility(View.VISIBLE);
                    viewDpDebetCredit.setVisibility(View.VISIBLE);
                }

            } else if (currentRoomCheckin.getDpPaymentType().equals("CASH")) {
                inputDpGroup.check(R.id.um_cash);
                dpType = "CASH";
                isUseDP = true;
                isUseDpCard = false;
                inputDpNominal.setVisibility(View.VISIBLE);
                viewDpDebetCredit.setVisibility(View.GONE);
            }else if(currentRoomCheckin.getDpPaymentType().equals("TRANSFER")){
                inputDpGroup.check(R.id.um_transfer);
                dpType = "TRANSFER";
                isUseDP = true;
                isUseDpCard = false;
                isUseDpTransfer = true;

                inputBankNameTf.getEditText()
                        .setText(currentRoomCheckin.getDpCardType());
                inputBankNameTf.setEnabled(false);
                inputBankAccountNameTf.getEditText()
                        .setText(currentRoomCheckin.getDpCardName());
                inputBankAccountNameTf.setEnabled(false);

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
            viewDpDebetCredit.setVisibility(View.GONE);
            inputDpNominal.setVisibility(View.GONE);
        }

        if (!currentRoomCheckin.getVoucherCode().isEmpty()) {
//            Log.d("DEBUGGING voucherCode", currentRoomCheckin.getVoucherCode());
            //bukan ini
            inputCodeVoucher.getEditText().setText(currentRoomCheckin.getVoucherCode());
            inputCodeVoucher.setEnabled(false);
            buttonScanQRCodeVoucher.setVisibility(View.GONE);
            buttonCheckCodeVoucher.setVisibility(View.GONE);
            buttonResetCodeVoucher.setVisibility(View.GONE);
        }

        int size = roomOrder.getRoomPromos().size();
        if (size > 0) {
            RoomPromo roomPromo = roomOrder.getRoomPromos()
                    .get(size - 1);
            inputCodeVoucher.setEnabled(false);
            buttonScanQRCodeVoucher.setEnabled(false);
            buttonPromoRoom.setText("Terpilih");
            //disini
            btnCancelPromoRoom.setVisibility(View.VISIBLE);
            buttonPromoRoom.setEnabled(false);
            promoRoomViewData(
                    roomPromo.getRoomPromoName()+" "+roomPromo.getDiscountPercent()+"%");
        }
        size = roomOrder.getInventoryPromos().size();
        if (size > 0) {
            InventoryPromo invPromo = roomOrder.getInventoryPromos()
                    .get(size - 1);
            inputCodeVoucher.setEnabled(false);
            buttonScanQRCodeVoucher.setEnabled(false);
            buttonPromoFood.setText("Terpilih");
            btnCancelPromoFood.setVisibility(View.VISIBLE);
            buttonPromoFood.setEnabled(false);
            promoFoodViewData(
                    invPromo.getFoodPromoName()+" "+invPromo.getDiscountPercent()+"%");
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

    private void initMemberValidationUI() {
        buttonScanQRCodeMember.setOnClickListener(view -> {
            if (!isMemberScanActive) {
                try {
                    visibleProgressBar(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GlobalBus
                                    .getBus()
                                    .post(new EventsWrapper
                                            .XZscan(QRScanType.MEMBER_INFO.getType()));
                            isMemberScanActive = true;
                        }
                    }, 500);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(requireActivity(), "Error Karena " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonResetCodeMember.setOnClickListener(view -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttonScanQRCodeMember.setVisibility(View.VISIBLE);
                    buttonResetCodeMember.setVisibility(View.GONE);
                    viewOutputCekCodeMember.setVisibility(View.GONE);


                    isMemberScanActive = false;

                    inputCodeMember.setFocusable(false);
                    inputCodeMember.setEnabled(true);
                    inputCodeMember.getEditText().setText("");

                }
            }, 500);
        });

        buttonCheckCodeMember.setOnClickListener(view -> {
            setScanResult();
        });

    }

    private void initEdcType() {
        visibleProgressBar(true);
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
                        visibleProgressBar(false);
                        typesListEdc.addAll(res.getTypeEdcs());
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<EdcTypeResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                visibleProgressBar(false);
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
                    Toast
                            .makeText(requireActivity(), "Mohon Periksa Kembali Input Uang Muka", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                input1JenisKartu = choicecard;
                input2Nama = inputDpNamaDebetCredit.getEditText().getText().toString();
                input3NomorKartu = inputDpCardNumberDebetCredit.getEditText().getText().toString();
                input4NomorApproval = inputDpApprovalCodeDebetCredit.getEditText().getText().toString();
                edcMachine = choiceTypeEdc.getEdcCode();
            }else if (isUseDpTransfer) {
                if (!isInputValid(inputBankNameTf.getEditText()) ||
                        !isInputValid(inputBankAccountNameTf.getEditText()) ) {
                    Toasty
                            .warning(requireActivity(), "Mohon Periksa Kembali Input Uang Muka", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                input1JenisKartu = inputBankAccountNameTf.getEditText().getText().toString();
                input2Nama =  inputBankNameTf.getEditText().getText().toString();
                edcMachine = "0";
            }
        }

        if (inputCodeVoucher.getEditText().getText().toString().length() > 0) {
            finalVoucherCode = inputCodeVoucher.getEditText().getText().toString();
        }else{
            finalVoucherCode = "";
        }

        if(inputCodeMember.getEditText().getText().toString().length() > 0){
            if(outputCheckTxtMember.getText().equals("Invalid")){
                finalMemberRefCode = "";
            }else if(outputCheckTxtMember.getText().equals("Valid")){
                finalMemberRefCode = inputCodeMember.getEditText().getText().toString();
            }
        }

        if (isEmptyVisitor()) {
            Toasty.warning(requireActivity(), "Harap isi jumlah tamu", Toast.LENGTH_SHORT, true)
                    .show();
            return;
        }
        // TODO :: masih belum ngurus nama alias
        new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme)
                .setTitle("Simpan Data Checkin")
                .setMessage("Anda ingin melanjutkan")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        visibleProgressBar(true);
                        Call<RoomOrderResponse> req = roomOrderClient
                                .submitEditOrderRoom(
                                        currentRoomCheckin.getRoomCode(),
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
                                        finalMemberRefCode,
                                        promoSelected
                                );


                        Log.d("promoDipilih", promoSelected.toString());

                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                                visibleProgressBar(false);
                                RoomOrderResponse res = response.body();
                                res.displayMessage(requireActivity());
                                if (!res.isOkay()) {
                                    return;
                                }
                                navToMain();
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                                visibleProgressBar(false);
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void navToMain() {
//        Navigation
//                .findNavController(this.getView())
//                .popBackStack();
        Navigation.findNavController(getView())
                .navigate(
                        OperasionalCheckinEditInfoFragmentDirections
                                .actionNavOperasionalCheckinEditInfoFragmentToNavOperasionalListRoomToEditInfoFragment()
                );
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
     }*/

    private void setScanResult(){
        String codeMbr = inputCodeMember.getEditText().getText().toString();
        if (codeMbr.equals("") || codeMbr == "") {
            Toast.makeText(requireActivity(),
                    "Kode Masih Kosong",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        visibleProgressBar(true);

        Call<MemberResponse> call = memberClient.checkMember(codeMbr);
        call.enqueue(new Callback<MemberResponse>() {
            @Override
            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                visibleProgressBar(false);
                viewOutputCekCodeMember.setVisibility(View.VISIBLE);
                MemberResponse res = response.body();
                if (!res.isOkay()) {
                    res.displayMessage(requireActivity());
                    outputCheckImgMember.setImageResource(R.drawable.ic_unverified);
                    outputCheckTxtMember.setText("Invalid");
                    outputCheckTxtMember.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red));
                    return;
                }
                res.displayMessage(requireActivity());

                outputCheckImgMember.setImageResource(R.drawable.ic_verified);
                outputCheckTxtMember.setText("Valid");
                outputCheckTxtMember.setTextColor(ContextCompat.getColor(requireActivity(), R.color.green));
                buttonResetCodeMember.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MemberResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                Log.e(TAG, t.getMessage());
                Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                visibleProgressBar(false);
            }
        });
    }

    @Subscribe
    public void setDataQrCode(EventsWrapper.ScanResult scanResult) {
        visibleProgressBar(false);
        if (!scanResult.isSuccess()) {
            isMemberScanActive = false;
            isVoucherScanActive = false;
            return;
        }
        if (isMemberScanActive) {
            if (scanResult.getData() != null) {
                buttonResetCodeMember.setVisibility(View.VISIBLE);
                inputCodeMember.setFocusable(true);
                inputCodeMember.setEnabled(false);
                inputCodeMember.getEditText().setText(scanResult.getData());
                setScanResult();
            }

            isMemberScanActive = false;
        } else if (isVoucherScanActive) {
            Toast.makeText(requireActivity(), "isVoucherScanActive", Toast.LENGTH_SHORT).show();
            if (scanResult.getData() != null) {
                buttonScanQRCodeVoucher.setVisibility(View.GONE);
                buttonResetCodeVoucher.setVisibility(View.VISIBLE);

                inputCodeVoucher.setFocusable(true);
                inputCodeVoucher.setEnabled(false);
                Log.d("mosok scan", scanResult.getData());
                inputCodeVoucher.getEditText().setText(scanResult.getData());
            }
            isVoucherScanActive = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void approvalResponse(DataBusEvent.approvalResponse data){
        Log.d("approval sampe sini", data.toString());
        if (data.isApprove()){
            _usernameTxt.setText(data.getUser());
            _passwordTxt.setText(data.getPassword());
            _usernameTxt.setEnabled(false);
            _parentPassword.setEnabled(false);
        }else{
            Toast.makeText(requireActivity(), data.getUser() +" Menolak", Toast.LENGTH_SHORT).show();
        }
    }
}