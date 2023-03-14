package com.ihp.frontoffice.view.fragment.ruangan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.InventoryPromo;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomPromo;
import com.ihp.frontoffice.data.entity.TypeEdc;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.entity.Voucher;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.RoomOrderClient;
import com.ihp.frontoffice.data.remote.MemberClient;
import com.ihp.frontoffice.data.remote.respons.EdcTypeResponse;
import com.ihp.frontoffice.data.remote.respons.MemberResponse;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.data.remote.respons.VoucherResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.view.listadapter.ListEdcTypeAdapter;
import com.ihp.frontoffice.view.listadapter.ListPromoInventoryAdapter;
import com.ihp.frontoffice.view.listadapter.ListPromoRoomAdapter;
import com.ihp.frontoffice.viewmodel.InventoryPromoViewModel;
import com.ihp.frontoffice.viewmodel.RoomPromoViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckinRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckinRoomFragment extends Fragment {


    @BindView(R.id.checkin_button_scan_qrcode_member)
    AppCompatButton buttonScanQRCodeMember;
    @BindView(R.id.checkin_check_code_member)
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

    @BindView(R.id.checkin_button_scan_qrcode_reservation)
    AppCompatButton buttonScanQRCodeReservation;
    @BindView(R.id.checkin_check_code_reservation)
    AppCompatButton buttonCheckCodeReservation;
    @BindView(R.id.checkin_reset_code_reservation)
    AppCompatButton buttonResetCodeReservation;
    @BindView(R.id.checkin_input_code_reservation)
    TextInputLayout inputCodeReservation;
    @BindView(R.id.view_output_cek_code_reservation)
    View ViewOutputCekCodeReservation;
    @BindView(R.id.img_output_cek_code_reservation)
    ImageView outputCheckImgReservation;
    @BindView(R.id.txt_output_cek_code_reservation)
    TextView outputCheckTxtReservation;
    private boolean isReservationScanActive;

    @BindView(R.id.checkin_input_visitor_name)
    TextInputLayout inputVisitorName;
    @BindView(R.id.checkin_input_visitor_phone)
    TextInputLayout inputVisitorPhone;

    @BindView(R.id.checkin_min_hours)
    ImageView inputMinHoursCheckin;
    @BindView(R.id.checkin_plus_hours)
    ImageView inputPlusHoursCheckin;
    @BindView(R.id.checkin_count_hours)
    TextView inputCountHoursCheckin;
    int jamCheckin = 0;

    @BindView(R.id.checkin_min_minutes)
    ImageView inputMinMinutesCheckin;
    @BindView(R.id.checkin_plus_minutes)
    ImageView inputPlusMinutesCheckin;
    @BindView(R.id.checkin_count_minutes)
    TextView inputCountMinutesCheckin;
    @BindView(R.id.checkin_minutes_view)
    View viewMinutes;
    int menitCheckin = 0;

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

    @BindView(R.id.checkin_input_desc)
    TextInputLayout inputDesc;
    @BindView(R.id.checkin_input_event_desc)
    TextInputLayout inputEventDesc;


    @BindView(R.id.input_checkin_type_dp)
    RadioGroup inputDpGroup;
    @BindView(R.id.checkin_input_dp_nominal)
    TextInputLayout inputDpNominal;
    @BindView(R.id.view_debet_credit)
    View viewDpDebetCredit;
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
    @BindView(R.id.txt_edc)
    TextView inputDpTextEdc;
    @BindView(R.id.txtCard)
    TextView inputDpTextCard;

    private int mYear, mMonth, mDay;


    @BindView(R.id.btn_checkin)
    AppCompatButton buttonCheckin;

    @BindView(R.id.btn_checksound)
    AppCompatButton buttonChecksound;

    @BindView(R.id.btn_promo_room)
    Button buttonPromoRoom;

    @BindView(R.id.txt_kode_promo_room)
    TextView kodePromoRoom;

    @BindView(R.id.bttnPromoFood)
    Button buttonPromoFood;

    @BindView(R.id.txtKodePromoFood)
    TextView kodePromoFood;

    @BindView(R.id.checkinProgressbar)
    MKLoader progressBar;

    @BindView(R.id.overpack_warning)
    TextView overpackWarning;

    // TODO :: init sign pad
    @BindView(R.id.clear_button)
    AppCompatButton clearSign;

    @BindView(R.id.signature_pad)
    SignaturePad mSignaturePad;

    RoomOrderClient roomOrderClient;
    MemberClient memberClient;
    private Room room;

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

    private final String TAG = "CheckinRoomFragment";


    private static String EMPTY_PROMO;
    private static String EMPTY_EDC_DP;
    private static String EMPTY_CARD_DP;


    private boolean isUseDP = false;
    private String[] banks;
    private ArrayAdapter<String> adapterBanks;
    private boolean isUseDpCard = false;
    private String dpType;
    private String finalVoucherCode = "";
    private File imgSign;
    private boolean isSignatureOk = false;
    private String errorMessageVoucher;
    private static String BASE_URL;
    private static User USER_FO;

    public CheckinRoomFragment() {
        // Required empty public constructor
    }


    public static CheckinRoomFragment newInstance() {
        CheckinRoomFragment fragment = new CheckinRoomFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        room = CheckinRoomFragmentArgs.fromBundle(getArguments()).getRoom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_checkin, container, false);
        ButterKnife.bind(this, view);
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



        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setMainTitle();
        viewMinutes.setVisibility(View.GONE);
        isMemberScanActive = false;
        isReservationScanActive = false;
        isVoucherScanActive = false;

        EMPTY_PROMO = getResources().getString(R.string.kode_promo);
        choicePromoRoom = EMPTY_PROMO;
        choicePromoFood = EMPTY_PROMO;

        EMPTY_EDC_DP = getResources().getString(R.string.edc_mesin);
        EMPTY_CARD_DP = getResources().getString(R.string.type_card);
        choicecard = EMPTY_CARD_DP;

        initMemberValidationUI();
        initVoucherValidationUI();
        initReservationValidationUI();
        initDurationCheckinUI();
        initQmUI();
        initQfUI();
        initDownPaymentUI();

        buttonCheckin.setOnClickListener(view -> {
            submitCheckin();
        });

        buttonChecksound.setOnClickListener(view -> {
            submitChecksound();
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

        clearSign.setOnClickListener(view -> {
            mSignaturePad.clear();
        });

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

                //Toast.makeText(requireActivity(), "1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                //mSaveButton.setEnabled(true);
                isSignatureOk = true;
                Toast.makeText(requireActivity(), "Anda sudah TTD", Toast.LENGTH_SHORT).show();
                clearSign.setEnabled(true);
            }

            @Override
            public void onClear() {
                //mSaveButton.setEnabled(false);
                isSignatureOk = false;

                clearSign.setEnabled(false);
            }
        });


    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment(room.getRoomType() + " " + room.getRoomCode()));
    }

    private void submitChecksound() {
        new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle("Checksoun Room")
                .setMessage("Anda ingin melanjutkan ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        Call<RoomOrderResponse> req = roomOrderClient
                                .checksoundRoom(room.getRoomCode(),USER_FO.getUserId());

                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                                progressBar.setVisibility(View.GONE);
                                RoomOrderResponse res = response.body();
                                if (!res.isOkay()) {
                                    res.displayMessage(requireActivity());

                                    return;
                                }
                                res.displayMessage(requireActivity());
                                navToMain();
                                //submitImageSign(res.getRoomOrder().getKodeRcp());
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
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

    private void initEdcType() {
        progressBar.setVisibility(View.VISIBLE);
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
                        progressBar.setVisibility(View.GONE);
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
                viewDpDebetCredit.setVisibility(View.GONE);
                inputDpNominal.setVisibility(View.GONE);
            }
            if (i == R.id.um_cash) {
                dpType = "CASH";
                isUseDP = true;
                isUseDpCard = false;
                inputDpNominal.setVisibility(View.VISIBLE);
                inputDpNamaDebetCredit.getEditText().setText(dpType);
                viewDpDebetCredit.setVisibility(View.GONE);
            }
            if (i == R.id.um_debet) {
                dpType = "DEBET CARD";
                isUseDP = true;
                isUseDpCard = true;
                inputDpNominal.setVisibility(View.VISIBLE);
                inputDpNamaDebetCredit.getEditText().setText(dpType);
                viewDpDebetCredit.setVisibility(View.VISIBLE);
            }
            if (i == R.id.um_credit) {
                dpType = "CREDIT CARD";
                isUseDP = true;
                isUseDpCard = true;
                inputDpNominal.setVisibility(View.VISIBLE);
                inputDpNamaDebetCredit.getEditText().setText(dpType);
                viewDpDebetCredit.setVisibility(View.VISIBLE);
            }

        });


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

    private void edcTypeViewData(TypeEdc typeEdc) {
        if (null == typeEdc) {
            inputDpTextEdc.setText(EMPTY_EDC_DP);
            return;
        }
        choiceTypeEdc = typeEdc;
        inputDpTextEdc.setText(choiceTypeEdc.getEdcName());
    }

    private void cardDpViewData(String card) {
        choicecard = card;
        inputDpTextCard.setText(card);
    }

    private void dialogEdcType() {
        if (typesListEdc.size() < 1) {
            initEdcType();
        }

        listEdcTypeAdapter = new ListEdcTypeAdapter(requireActivity(), typesListEdc);
        listEdcTypeAdapter.notifyDataSetChanged();

        new MaterialAlertDialogBuilder(requireActivity())
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

    private void dialogCardType() {
        banks = getResources().getStringArray(R.array.bank_values);
        adapterBanks = new ArrayAdapter<String>(requireActivity(),
                android.R.layout.simple_list_item_1, banks);

        new MaterialAlertDialogBuilder(requireActivity())
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

    private void initMemberValidationUI() {
        buttonScanQRCodeMember.setOnClickListener(view -> {
            if (!isMemberScanActive) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //integrator.initiateScan();
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
                    inputVisitorName.getEditText().setText("");
                    inputVisitorPhone.getEditText().setText("");
                }
            }, 500);
        });

        buttonCheckCodeMember.setOnClickListener(view -> {
            String codeMbr = inputCodeMember.getEditText().getText().toString();
            if (codeMbr.equals("") || codeMbr == "") {
                Toast.makeText(requireActivity(),
                        "Kode Masih Kosong",
                        Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            Call<MemberResponse> call = memberClient.checkMember(codeMbr);
            call.enqueue(new Callback<MemberResponse>() {
                @Override
                public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                    progressBar.setVisibility(View.GONE);
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
                    inputVisitorName.getEditText().setText(res.getMember().getFullName());
                    inputVisitorPhone.getEditText().setText(res.getMember().getHp());
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
                    progressBar.setVisibility(View.GONE);
                }
            });
        });


    }

       private void initVoucherValidationUI() {
        buttonScanQRCodeVoucher.setOnClickListener(view -> {
            if (!isVoucherScanActive) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //integrator.initiateScan();
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
            progressBar.setVisibility(View.VISIBLE);

            Call<VoucherResponse> call = memberClient.checkVoucherMembership(codeVcr);
            call.enqueue(new Callback<VoucherResponse>() {
                @Override
                public void onResponse(Call<VoucherResponse> call, Response<VoucherResponse> response) {
                    progressBar.setVisibility(View.GONE);
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
                                progressBar.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
    }

    private void checkLocalVoucher(String codVcr) {
        Call<VoucherResponse> call = memberClient.checkVoucherOutlet(codVcr);
        call.enqueue(new Callback<VoucherResponse>() {
            @Override
            public void onResponse(Call<VoucherResponse> call, Response<VoucherResponse> response) {
                progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void confirmVoucher(Voucher voucher) {
        if (!voucher.getJenis_kamar().equals("")) {
            String voucherRoom = voucher.getJenis_kamar().toUpperCase();
            if (!voucherRoom.contains(room.getRoomType())) {
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

    private void initReservationValidationUI() {
        buttonScanQRCodeReservation.setOnClickListener(view -> {
            if (!isReservationScanActive) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //integrator.initiateScan();
                            isReservationScanActive = true;
                        }
                    }, 500);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(requireActivity(), "Error Karena " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonResetCodeReservation.setOnClickListener(view -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttonScanQRCodeReservation.setVisibility(View.VISIBLE);
                    buttonResetCodeReservation.setVisibility(View.GONE);
                    isReservationScanActive = false;

                    inputCodeReservation.setFocusable(false);
                    inputCodeReservation.setEnabled(true);
                    inputCodeReservation.getEditText().setText("");
                }
            }, 500);
        });

        buttonCheckCodeReservation.setOnClickListener(view -> {
            String codeRsv = inputCodeReservation.getEditText().getText().toString();
            if (codeRsv.equals("") || codeRsv == "") {
                Toast.makeText(requireActivity(),
                        "Kode Masih Kosong",
                        Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            // TODO : reservasi not handle on this fragment
            Call<MemberResponse> call = memberClient.checkReservasi(codeRsv);
            call.enqueue(new Callback<MemberResponse>() {
                @Override
                public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    //JsonElement jsonElement = response.body();
                    viewOutputCekCodeVoucher.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<MemberResponse> call, Throwable t) {
                    Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
    }


    private void promoFoodViewData(String promo) {
        choicePromoFood = promo;
        kodePromoFood.setText(choicePromoFood);
    }

    private void promoRoomViewData(String promo) {
        choicePromoRoom = promo;
        kodePromoRoom.setText(choicePromoRoom);
    }

    private void promoFoodSetupData() {
        promoFoodList.clear();
        progressBar.setVisibility(View.VISIBLE);
        inventoryPromoViewModel
                .getFoodPromoResponseMutableLiveData(room.getRoomType(), room.getRoomCode())
                .observe(getViewLifecycleOwner(), foodPromoResponse -> {
                    progressBar.setVisibility(View.GONE);
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

        new MaterialAlertDialogBuilder(requireActivity())
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

    private void promoRoomSetupData() {
        promoRoomList.clear();
        progressBar.setVisibility(View.VISIBLE);
        roomPromoViewModel
                .getRoomPromoResponseMutableLiveData(room.getRoomType())
                .observe(getViewLifecycleOwner(), roomPromoResponse -> {
                    progressBar.setVisibility(View.GONE);
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

        new MaterialAlertDialogBuilder(requireActivity())
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

    private void initDurationCheckinUI() {
        inputMinHoursCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jamCheckin == 0) {
                    inputCountHoursCheckin.setText(String.valueOf(jamCheckin));
                }

                if (jamCheckin > 0) {

                    jamCheckin = jamCheckin - 1;
                    inputCountHoursCheckin.setText(String.valueOf(jamCheckin));
                }
            }
        });
        inputPlusHoursCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jamCheckin == 24) {
                    inputCountHoursCheckin.setText(String.valueOf(jamCheckin));
                }

                if (jamCheckin < 24) {

                    jamCheckin = jamCheckin + 1;
                    inputCountHoursCheckin.setText(String.valueOf(jamCheckin));
                }

            }
        });

        inputMinMinutesCheckin.setEnabled(false);
        inputMinMinutesCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menitCheckin == 0) {
                    inputCountMinutesCheckin.setText(String.valueOf(menitCheckin));
                }

                if (menitCheckin > 0) {

                    menitCheckin = menitCheckin - 1;
                    inputCountMinutesCheckin.setText(String.valueOf(menitCheckin));
                }
            }
        });
        inputPlusMinutesCheckin.setEnabled(false);
        inputPlusMinutesCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menitCheckin == 59) {
                    jamCheckin = jamCheckin + 1;
                    inputCountMinutesCheckin.setText(String.valueOf(menitCheckin));
                    return;
                }

                if (menitCheckin < 60) {

                    menitCheckin = menitCheckin + 1;
                    inputCountMinutesCheckin.setText(String.valueOf(menitCheckin));
                }
            }
        });


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

                if(sCounted.length()>0){
                    try {
                        qmDewasa = Integer.parseInt(sCounted);
                    }catch (Exception x){
                        qmDewasa = 0;
                        inputCountQmDewasa.setText(String.valueOf(qmDewasa));
                         /* Toasty
                            .warning(requireActivity(), "Mohon Periksa Kembali Input Uang Muka", Toast.LENGTH_SHORT)
                            .show();*/
                    }

                }else{
                    qmDewasa = 0;
                    inputCountQmDewasa.setText(String.valueOf(qmDewasa));
                   /* Toasty
                            .warning(requireActivity(), "Mohon Periksa Kembali Input Uang Muka", Toast.LENGTH_SHORT)
                            .show();*/
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
           /* if (qmDewasa == 10) {
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }

            if (qmDewasa < 10) {
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

    private void submitCheckin() {
        if (!isInputValid(inputVisitorName.getEditText()) ||
                !isInputValid(inputVisitorPhone.getEditText()) ||
                !isInputValid(inputVisitorPhone.getEditText()) ||
                (jamCheckin == 0)) {
            Toast
                    .makeText(requireActivity(), "Mohon Periksa Kembali", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (!isSignatureOk) {
            Toast
                    .makeText(requireActivity(), "Mohon Periksa Kembali TTD", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String namaVisitor = inputVisitorName.getEditText().getText().toString();
        namaVisitor.replace("\'", "");
        namaVisitor.replace(",", "");

        String kodeMember = inputCodeMember.getEditText().getText().toString();
        if (kodeMember.isEmpty() || kodeMember.equals("")) {
            StringBuilder builder = new StringBuilder();
            builder.append(namaVisitor);
            builder.append("-1");
            kodeMember = builder.toString();

        }
        String finalMemberCode = kodeMember;

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

            nominalDp = inputDpNominal.getEditText().getText().toString();
            keteranganUangMuka = dpType;
            Toast
                    .makeText(requireActivity(), keteranganUangMuka, Toast.LENGTH_SHORT)
                    .show();
            if (isUseDpCard) {
                if (!isInputValid(inputDpNamaDebetCredit.getEditText()) ||
                        !isInputValid(inputDpCardNumberDebetCredit.getEditText()) ||
                        !isInputValid(inputDpApprovalCodeDebetCredit.getEditText()) ||
                        !isInputValid(inputDpExpireDate.getEditText()) ||
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
            }
        }

        if (inputCodeVoucher.getEditText().getText().toString().length() > 0) {
            finalVoucherCode = inputCodeVoucher.getEditText().getText().toString();
        }


        new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle("Simpan Checkin")
                .setMessage("Anda ingin melanjutkan")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        Call<RoomOrderResponse> req = roomOrderClient
                                .submitOrderRoom(
                                        room.getRoomCode(),
                                        namaVisitor,
                                        finalMemberCode,
                                        String.valueOf(jamCheckin),
                                        String.valueOf(menitCheckin),
                                        String.valueOf(qmAnak),
                                        String.valueOf(qmRemaja),
                                        String.valueOf(qmDewasa),
                                        String.valueOf(qmTua),
                                        String.valueOf(qfAnak),
                                        String.valueOf(qfRemaja),
                                        String.valueOf(qfDewasa),
                                        String.valueOf(qfTua),
                                        inputVisitorPhone.getEditText().getText().toString(),
                                        nominalDp,
                                        inputDesc.getEditText().getText().toString(),
                                        inputEventDesc.getEditText().getText().toString(),
                                        USER_FO.getUserId(),
                                        finalVoucherCode,
                                        keteranganUangMuka,
                                        input1JenisKartu,
                                        input2Nama,
                                        input3NomorKartu,
                                        input4NomorApproval,
                                        edcMachine,
                                        promoSelected,
                                        "");


                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                                progressBar.setVisibility(View.GONE);
                                RoomOrderResponse res = response.body();
                                if (!res.isOkay()) {
                                    res.displayMessage(requireActivity());

                                    return;
                                }
                                //navToMain();
                                submitImageSign(res.getRoomOrder().getKodeRcp());
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
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
        /*Navigation
                .findNavController(this.getView())
                .navigate(CheckinRoomFragmentDirections
                        .actionNavCheckinRoomFragmentToNavListRoomFragment());*/
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

   /* @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }
*/
    @Subscribe
    public void setDataQrCode(EventsWrapper.ScanResult scanResult) {
        progressBar.setVisibility(View.GONE);
        if (!scanResult.isSuccess()) {
            isMemberScanActive = false;
            isVoucherScanActive = false;
            isReservationScanActive = false;
            return;
        }
        if (isMemberScanActive) {
            if(scanResult.getData()!=null){
                buttonCheckCodeMember.setVisibility(View.VISIBLE);
                buttonResetCodeMember.setVisibility(View.VISIBLE);

                inputCodeMember.setFocusable(true);
                inputCodeMember.setEnabled(false);
                inputCodeMember.getEditText().setText(scanResult.getData());
            }



            isMemberScanActive = false;
        } else if (isVoucherScanActive) {
            if(scanResult.getData()!=null){
                buttonScanQRCodeVoucher.setVisibility(View.GONE);
                buttonResetCodeVoucher.setVisibility(View.VISIBLE);

                inputCodeVoucher.setFocusable(true);
                inputCodeVoucher.setEnabled(false);
                inputCodeVoucher.getEditText().setText(scanResult.getData());
            }

            isVoucherScanActive = false;
        } else if (isReservationScanActive) {
            if(scanResult.getData()!=null){
                buttonScanQRCodeReservation.setVisibility(View.GONE);
                buttonResetCodeReservation.setVisibility(View.VISIBLE);

                inputCodeReservation.setFocusable(true);
                inputCodeReservation.setEnabled(false);
                inputCodeReservation.getEditText().setText(scanResult.getData());
            }

            isReservationScanActive = false;
        }

    }


    private void submitImageSign(String kodeRcp) {
        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
        if (addJpgSignatureToGallery(signatureBitmap, kodeRcp)) {
            //Toast.makeText(requireActivity(), "Signature saved into Gallery "+kodeRcp, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Unable to store the signature", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imgSign);
        MultipartBody.Part surveyImages = MultipartBody
                .Part.createFormData("files", imgSign.getName(), surveyBody);

        Call<RoomOrderResponse> req = roomOrderClient.submitSignImg(surveyImages);
        req.enqueue(new Callback<RoomOrderResponse>() {
            @Override
            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                progressBar.setVisibility(View.GONE);
                RoomOrderResponse res = response.body();
                if (!res.isOkay()) {
                    res.displayMessage(requireActivity());
                    return;
                }
                navToMain();
            }

            @Override
            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public boolean addJpgSignatureToGallery(Bitmap signature, String rcp) {
        boolean result = false;
        try {
            //imgSign = new File(getAlbumStorageDir("RCPTTD"), String.format(rcp + ".jpg", System.currentTimeMillis()));
            File localFile = null;
            try {
                localFile = File.createTempFile(rcp, ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgSign = localFile;

            saveBitmapToJPG(signature, imgSign);
            scanMediaFile(imgSign);
            //Toast.makeText(requireActivity(), imgSign.getPath()/*+String.format("Signature_%d.jpg", System.currentTimeMillis())*/, Toast.LENGTH_SHORT).show();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireActivity(), "error karena " + e, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        try {
            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            OutputStream stream = new FileOutputStream(photo);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireActivity(), "error karena " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // only for android 10 above
            String path = Environment.getExternalStorageState();
            file = new File(path, albumName);

        } else {
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
        }


        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        } //Toast.makeText(MainActivity.this, file.getPath(), Toast.LENGTH_SHORT).show();
        return file;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        requireActivity().sendBroadcast(mediaScanIntent);
    }

}