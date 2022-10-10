package com.ihp.frontoffice.view.fragment.ruangan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Invoice;
import com.ihp.frontoffice.data.entity.Payment;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.entity.TypeEdc;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.PaymentOrderClient;
import com.ihp.frontoffice.data.remote.MemberClient;
import com.ihp.frontoffice.data.remote.respons.EdcTypeResponse;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.view.listadapter.ListEdcTypeAdapter;
import com.ihp.frontoffice.view.listadapter.ListPaymentAdapter;
import com.ihp.frontoffice.viewmodel.RoomOrderViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentInvoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentInvoiceFragment extends Fragment
        implements ListPaymentAdapter.RemovePayment,ListPaymentAdapter.EditPayment {


    @BindView(R.id.btn_add_payment)
    Button buttonAddMorePayment;

    @BindView(R.id.btn_submit_payment)
    Button buttonSubmitPayment;

    @BindView(R.id.payment_progressbar)
    MKLoader progressBar;

    @BindView(R.id.payment_list_recyclerview)
    RecyclerView paymentListRecycle;

    @BindView(R.id.value_total_ruangan)
    TextView valueRuangan;

    @BindView(R.id.value_voucher)
    TextView valueVoucher;

    @BindView(R.id.value_total_penjualan)
    TextView valueFnb;

    @BindView(R.id.value_total_all)
    TextView valueJumlah;

    @BindView(R.id.value_total_service)
    TextView valueService;

    @BindView(R.id.value_total_tax)
    TextView valueTax;

    @BindView(R.id.value_jumlah_total)
    TextView valueJumlahTotal;

    @BindView(R.id.value_total_transfer_room)
    TextView valueTransferRuangan;

    @BindView(R.id.value_gift)
    TextView valueGift;

    @BindView(R.id.value_uang_muka)
    TextView valueDownPayment;

    @BindView(R.id.value_final_total)
    TextView valueFinalTotal;

    @BindView(R.id.info_value_total_payments)
    TextView valueTotalPayments;


    // TODO : Rename and change types of parameters

    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater dialogInflater;
    private View dialogView;
    private View viewPaymentDebetCredit;
    private View viewPaymentEmoney;
    private View viewPaymentCompliment;
    private View viewPaymentPiutang;
    private ChipGroup typePayment;
    MemberClient memberClient;
    PaymentOrderClient paymentOrderClient;

    private ArrayList<TypeEdc> typesListEdc = new ArrayList<>();
    private ListEdcTypeAdapter listEdcTypeAdapter;

    private String[] banks;
    private ArrayAdapter<String> adapterBanks;
    private AppCompatButton buttonChooseEdc,
            buttonChooseCard;
    private TextInputLayout inputNamaDebetCredit,
            inputCardNumberDebetCredit,
            inputApprovalDebetCredit,
            inputNominalPayment,
            inputNamaEmoney,
            inputPaymentAkunEmoney,
            inputRefKodeEmoney,
            inputNamaCompliment,
            inputInstruksiCompliment,
            inputInstansiCompliment, inputNamaPiutang, inputIdMemberPiutang;
    private TextView txtEdcDebetCredit, txtCardDebetCredit;
    private RadioGroup rgTypePaymentEmoney, rgTypePaymentPiutang;

    private int mYear, mMonth, mDay;
    private Room room;
    private Invoice invoice;
    private String defaultPaymentType;
    private String emoneyPaymentType;
    private String piutangPaymentType;
    private ArrayList<Payment> payments = new ArrayList<>();
    private ListPaymentAdapter listPaymentAdapter;
    private static String EMPTY_EDC_DP;
    private static String EMPTY_CARD_DP;

    private RoomOrderViewModel roomOrderViewModel;

    private RoomOrder roomOrder;
    private double totalPayment;
    private static String BASE_URL;
    private User USER_FO;

    public PaymentInvoiceFragment() {
        // Required empty public constructor
    }


    public static PaymentInvoiceFragment newInstance() {
        PaymentInvoiceFragment fragment = new PaymentInvoiceFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        room = PaymentInvoiceFragmentArgs.fromBundle(getArguments()).getRoom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_invoice, container, false);
        ButterKnife.bind(this, view);
        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) requireActivity().getApplicationContext()).getUserFo();
        memberClient = ApiRestService.getClient(BASE_URL).create(MemberClient.class);
        paymentOrderClient = ApiRestService.getClient(BASE_URL).create(PaymentOrderClient.class);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setMainTitle();

        buttonAddMorePayment.setOnClickListener(view -> {
            inputPaymentDialog();
        });
        buttonSubmitPayment.setOnClickListener(view -> {
            submitPayment();
        });
        roomOrderViewModel = new ViewModelProvider(requireActivity())
                .get(RoomOrderViewModel.class);
        roomOrderViewModel.init(BASE_URL);
        roomOrderSetupData();
        initEdcType();
        setupPaymentRecyclerView();
        countTotalPayment();
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment(room.getRoomType()+" "+room.getRoomCode()));
    }

    private void roomOrderSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        invoice = null;
        roomOrderViewModel
                .getRoomOrderResponseMutableLiveData(room.getRoomCode())
                .observe(getViewLifecycleOwner(), roomOrderResponse -> {
                    roomOrderResponse.displayMessage(requireActivity());
                    if (roomOrderResponse.isOkay()) {
                        roomOrder = roomOrderResponse.getRoomOrder();
                        invoice = roomOrder.getInvoice();
                        initInvoice();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void initInvoice() {
        valueRuangan.setText(AppUtils.formatNominal(invoice.getTotalRoom()));

        valueFnb.setText(AppUtils.formatNominal(invoice.getTotalInventory()));
        valueJumlah.setText(AppUtils.formatNominal(invoice.getTotalRoomAndInventory()));
        valueService.setText(AppUtils.formatNominal(invoice.getTotalService()));
        valueTax.setText(AppUtils.formatNominal(invoice.getTotalTax()));
        valueJumlahTotal.setText(AppUtils.formatNominal(invoice.getTotalAll()));
        valueTransferRuangan.setText(AppUtils.formatNominal(invoice.getTotalTransfer()));
        valueDownPayment.setText(AppUtils.formatNominal(invoice.getTotalUangMuka()));
        if (invoice.isGift()) {
            valueVoucher.setText("Rp0");
            valueGift.setText(AppUtils.formatNominal(invoice.getTotalVoucher()));
        } else {
            valueVoucher.setText(AppUtils.formatNominal(invoice.getTotalVoucher()));
            valueGift.setText("Rp0");
        }


        valueFinalTotal.setText(AppUtils.formatNominal(invoice.getTotalFinal()));
    }


    private void setupPaymentRecyclerView() {
        if (listPaymentAdapter == null) {
            listPaymentAdapter = new ListPaymentAdapter(requireActivity(), payments, this,this);
            paymentListRecycle.setAdapter(listPaymentAdapter);
            paymentListRecycle.setLayoutManager(new LinearLayoutManager(requireActivity()));
        } else {
            listPaymentAdapter.notifyDataSetChanged();
        }
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

    private void visibleLayoutTypePayment(String type) {
        viewPaymentDebetCredit.setVisibility(View.GONE);
        viewPaymentEmoney.setVisibility(View.GONE);
        viewPaymentCompliment.setVisibility(View.GONE);
        viewPaymentPiutang.setVisibility(View.GONE);
        if (type.equals("CASH")) {
            defaultPaymentType = "CASH";
        } else if (type.equals("DEBET CARD")) {
            defaultPaymentType = "DEBET CARD";
            viewPaymentDebetCredit.setVisibility(View.VISIBLE);
        } else if (type.equals("CREDIT CARD")) {
            defaultPaymentType = "CREDIT CARD";
            viewPaymentDebetCredit.setVisibility(View.VISIBLE);
        } else if (type.equals("E-MONEY")) {
            defaultPaymentType = "E-MONEY";
            viewPaymentEmoney.setVisibility(View.VISIBLE);
        } else if (type.equals("COMPLIMENTARY")) {
            defaultPaymentType = "COMPLIMENTARY";
            viewPaymentCompliment.setVisibility(View.VISIBLE);
        } else if (type.equals("PIUTANG")) {
            defaultPaymentType = "PIUTANG";
            viewPaymentPiutang.setVisibility(View.VISIBLE);
        }
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
                        txtEdcDebetCredit.setText(listEdcTypeAdapter.getItem(i).getEdcName());
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
                        txtCardDebetCredit.setText(adapterBanks.getItem(i));
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

    private void initEdcType() {
        progressBar.setVisibility(View.VISIBLE);
        Call<EdcTypeResponse> call = memberClient.getEdc();
        call.enqueue(new Callback<EdcTypeResponse>() {
            @Override
            public void onResponse(Call<EdcTypeResponse> call, Response<EdcTypeResponse> response) {
                progressBar.setVisibility(View.GONE);
                EdcTypeResponse res = response.body();
                res.displayMessage(requireActivity());
                if (!res.isOkay()) {
                    return;
                }

                typesListEdc.addAll(res.getTypeEdcs());

            }

            @Override
            public void onFailure(Call<EdcTypeResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // TODO : input payments
    private void inputPaymentDialog() {
        dialogBuilder = new AlertDialog.Builder(requireActivity());
        dialogInflater = this.getLayoutInflater();
        dialogView = dialogInflater.inflate(R.layout.dialog_payment_room, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("Total Tagihan " + AppUtils.formatNominal(invoice.getTotalFinal()));
        dialogBuilder.setPositiveButton(android.R.string.ok, null);

        //TODO :: init view layout component
        viewPaymentDebetCredit = dialogView.findViewById(R.id.view_payment_debet_credit);
        viewPaymentEmoney = dialogView.findViewById(R.id.view_payment_emoney);
        viewPaymentCompliment = dialogView.findViewById(R.id.view_payment_compliment);
        viewPaymentPiutang = dialogView.findViewById(R.id.view_payment_piutang);
        typePayment = dialogView.findViewById(R.id.chipgroup_payment_type);
        //TODO :: init default input
        inputNominalPayment = dialogView.findViewById(R.id.payment_input_nominal);
        //TODO :: init optional input debet||credit

        buttonChooseEdc = dialogView.findViewById(R.id.button_payment_choose_edc);
        txtEdcDebetCredit = dialogView.findViewById(R.id.txt_payment_edc);
        buttonChooseCard = dialogView.findViewById(R.id.button_payment_choose_card);
        txtCardDebetCredit = dialogView.findViewById(R.id.txt_payment_card);
        inputNamaDebetCredit = dialogView.findViewById(R.id.input_payment_nama_debetcredit);
        inputCardNumberDebetCredit = dialogView.findViewById(R.id.input_payment_cardnumber_debetcredit);
        inputApprovalDebetCredit = dialogView.findViewById(R.id.input_payment_approval_number_debetcredit);

        //TODO :: init optional input emoney
        rgTypePaymentEmoney = dialogView.findViewById(R.id.type_emoney);
        inputNamaEmoney = dialogView.findViewById(R.id.input_payment_nama_emoney);
        inputPaymentAkunEmoney = dialogView.findViewById(R.id.input_payment_account_emoney);
        inputRefKodeEmoney = dialogView.findViewById(R.id.input_payment_ref_trans_emoney);
        //TODO :: init optional input compliment
        inputNamaCompliment = dialogView.findViewById(R.id.input_payment_nama_compliment);
        inputInstansiCompliment = dialogView.findViewById(R.id.input_payment_instansi_compliment);
        inputInstruksiCompliment = dialogView.findViewById(R.id.input_payment_instruksi_compliment);
        //TODO :: init optional input piutang
        rgTypePaymentPiutang = dialogView.findViewById(R.id.type_piutang);
        inputNamaPiutang = dialogView.findViewById(R.id.input_payment_nama_piutang);
        inputIdMemberPiutang = dialogView.findViewById(R.id.input_payment_id_member_piutang);
        //TODO :: add event on component type payment
        defaultPaymentType = "CASH";
        typePayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.payment_cash) {
                visibleLayoutTypePayment("CASH");
            } else if (checkedId == R.id.payment_credit) {
                visibleLayoutTypePayment("CREDIT CARD");
            } else if (checkedId == R.id.payment_debet) {
                visibleLayoutTypePayment("DEBET CARD");
            } else if (checkedId == R.id.payment_emoney) {
                visibleLayoutTypePayment("E-MONEY");
            } else if (checkedId == R.id.payment_complimentary) {
                visibleLayoutTypePayment("COMPLIMENTARY");
            } else if (checkedId == R.id.payment_piutang) {
                visibleLayoutTypePayment("PIUTANG");
            }
        });

        EMPTY_EDC_DP = getResources().getString(R.string.edc_mesin);
        EMPTY_CARD_DP = getResources().getString(R.string.type_card);
        buttonChooseEdc.setOnClickListener(view -> {
            if (txtEdcDebetCredit.getText().toString().equals(EMPTY_EDC_DP)) {
                dialogEdcType();
            } else {
                buttonChooseEdc.setText("Pilih");
                buttonChooseEdc.setTextColor(Color.BLUE);
                txtEdcDebetCredit.setText(EMPTY_EDC_DP);
            }

        });
        buttonChooseCard.setOnClickListener(view -> {
            if (txtCardDebetCredit.getText().toString().equals(EMPTY_CARD_DP)) {
                dialogCardType();
            } else {
                buttonChooseCard.setText("Pilih");
                buttonChooseEdc.setTextColor(Color.BLUE);
                txtCardDebetCredit.setText(EMPTY_CARD_DP);
            }
        });

        //TODO :: add event on component type payment emoney
        rgTypePaymentEmoney.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.dana) {
                emoneyPaymentType = "DANA";
            } else if (i == R.id.ovo) {
                emoneyPaymentType = "OVO";
            } else if (i == R.id.gopay) {
                emoneyPaymentType = "GOPAY";
            }
        });
        //TODO :: add event on component type payment piutang
        piutangPaymentType = "";
        rgTypePaymentPiutang.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.paymentPiutangPemegangSahamOutlet) {
                piutangPaymentType = "PEMEGANG SAHAM OUTLET";
            } else if (i == R.id.paymentPiutangPemegangSahamWaralaba) {
                piutangPaymentType = "PEMEGANG SAHAM WARALABA";
            } else if (i == R.id.paymentPiutangOutlet) {
                piutangPaymentType = "OUTLET";
            } else if (i == R.id.paymentPiutangKonsumenTerdaftar) {
                piutangPaymentType = "KONSUMEN TERDAFTAR";
            } else if (i == R.id.paymentPiutangAdvanced) {
                piutangPaymentType = "ADVANCED";
            }
        });


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Payment payment = null;
                        if (defaultPaymentType.equals("CASH")) {
                            if (!isInputValid(inputNominalPayment.getEditText())) {
                                Toast
                                        .makeText(requireActivity(), "Mohon Isi Nominal", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            payment = new Payment();
                            int valuePayment = Integer.parseInt(inputNominalPayment.getEditText().getText().toString());
                            payment.setNominal(valuePayment);
                            payment.setPaymentType(defaultPaymentType);
                        } else if (defaultPaymentType.equals("DEBET CARD")) {
                            if (!isInputValid(inputNominalPayment.getEditText()) ||
                                    !isInputValid(inputNamaDebetCredit.getEditText()) ||
                                    !isInputValid(inputApprovalDebetCredit.getEditText()) ||
                                    !isInputValid(inputCardNumberDebetCredit.getEditText()) ||
                                    txtCardDebetCredit.getText().toString().equals(EMPTY_CARD_DP) ||
                                    txtEdcDebetCredit.getText().toString().equals(EMPTY_EDC_DP)

                            ) {
                                Toast
                                        .makeText(requireActivity(), "Mohon Periksa Kembali", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            payment = new Payment();
                            int valuePayment = Integer.parseInt(inputNominalPayment.getEditText().getText().toString());
                            payment.setNominal(valuePayment);
                            payment.setPaymentType(defaultPaymentType);

                            payment.setEdcDebet(txtEdcDebetCredit.getText().toString());
                            payment.setCardDebet(txtEdcDebetCredit.getText().toString());
                            payment.setNamaUserDebet(inputNamaDebetCredit.getEditText().getText().toString());
                            payment.setApprovalCodeDebet(inputApprovalDebetCredit.getEditText().getText().toString());
                            payment.setCardCodeDebet(inputCardNumberDebetCredit.getEditText().getText().toString());


                        } else if (defaultPaymentType.equals("CREDIT CARD")) {
                            if (!isInputValid(inputNominalPayment.getEditText()) ||
                                    !isInputValid(inputNamaDebetCredit.getEditText()) ||
                                    !isInputValid(inputApprovalDebetCredit.getEditText()) ||
                                    !isInputValid(inputCardNumberDebetCredit.getEditText()) ||
                                    txtCardDebetCredit.getText().toString().equals(EMPTY_CARD_DP) ||
                                    txtEdcDebetCredit.getText().toString().equals(EMPTY_EDC_DP)
                            ) {
                                Toast
                                        .makeText(requireActivity(), "Mohon Lengkapi Inputan Credit", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            payment = new Payment();
                            int valuePayment = Integer.parseInt(inputNominalPayment.getEditText().getText().toString());
                            payment.setNominal(valuePayment);
                            payment.setPaymentType(defaultPaymentType);

                            payment.setEdcCredit(txtEdcDebetCredit.getText().toString());
                            payment.setCardCredit(txtEdcDebetCredit.getText().toString());
                            payment.setNamaUserCredit(inputNamaDebetCredit.getEditText().getText().toString());
                            payment.setApprovalCodeCredit(inputApprovalDebetCredit.getEditText().getText().toString());
                            payment.setCardCodeCredit(inputCardNumberDebetCredit.getEditText().getText().toString());


                        } else if (defaultPaymentType.equals("E-MONEY")) {
                            if (!isInputValid(inputNominalPayment.getEditText()) ||
                                    !isInputValid(inputNamaEmoney.getEditText()) ||
                                    !isInputValid(inputPaymentAkunEmoney.getEditText()) ||
                                    !isInputValid(inputRefKodeEmoney.getEditText())
                            ) {
                                Toast
                                        .makeText(requireActivity(), "Mohon Lengkapi Inputan E-money", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            payment = new Payment();
                            int valuePayment = Integer.parseInt(inputNominalPayment.getEditText().getText().toString());
                            payment.setNominal(valuePayment);
                            payment.setPaymentType(defaultPaymentType);

                            payment.setTypeEmoney(emoneyPaymentType);
                            payment.setNamaUserEmoney(inputNamaEmoney.getEditText().getText().toString());
                            payment.setAccountEmoney(inputPaymentAkunEmoney.getEditText().getText().toString());
                            payment.setRefCodeEmoney(inputRefKodeEmoney.getEditText().getText().toString());

                        } else if (defaultPaymentType.equals("COMPLIMENTARY")) {
                            if (!isInputValid(inputNominalPayment.getEditText()) ||
                                    !isInputValid(inputNamaCompliment.getEditText()) ||
                                    !isInputValid(inputInstansiCompliment.getEditText()) ||
                                    !isInputValid(inputInstruksiCompliment.getEditText())
                            ) {
                                Toast
                                        .makeText(requireActivity(), "Mohon Lengkapi Inputan Compliment", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            payment = new Payment();
                            int valuePayment = Integer.parseInt(inputNominalPayment.getEditText().getText().toString());
                            payment.setNominal(valuePayment);
                            payment.setPaymentType(defaultPaymentType);

                            payment.setNamaUserCompliment(inputNamaCompliment.getEditText().getText().toString());
                            payment.setInstansiCompliment(inputInstansiCompliment.getEditText().getText().toString());
                            payment.setInstruksiCompliment(inputInstruksiCompliment.getEditText().getText().toString());

                        } else if (defaultPaymentType.equals("PIUTANG")) {
                            if (!isInputValid(inputNominalPayment.getEditText()) ||
                                    !isInputValid(inputNamaPiutang.getEditText()) ||
                                    !isInputValid(inputIdMemberPiutang.getEditText())||
                                    piutangPaymentType.isEmpty()||piutangPaymentType.equals("")
                            ) {
                                Toast
                                        .makeText(requireActivity(), "Mohon Lengkapi Inputan Compliment", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            payment = new Payment();

                            int valuePayment = Integer.parseInt(inputNominalPayment.getEditText().getText().toString());
                            payment.setNominal(valuePayment);
                            payment.setPaymentType(defaultPaymentType);

                            payment.setTypePiutang(piutangPaymentType);
                            payment.setNamaUserPiutang(inputNamaPiutang.getEditText().getText().toString());
                            payment.setIdMemberPiutang(inputIdMemberPiutang.getEditText().getText().toString());

                        } else {
                            Toast
                                    .makeText(requireActivity(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }


                        if (null == payment) {

                            Toast
                                    .makeText(requireActivity(), "Ok Messages", Toast.LENGTH_SHORT)
                                    .show();

                        } else {
                            payments.add(payment);
                            countTotalPayment();
                            listPaymentAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        }


                    }
                });
            }
        });

        alertDialog.show();
    }

    //TODO :: submit data payment
    private void submitPayment() {

        if (totalPayment < invoice.getTotalFinal()) {
            Toast.makeText(requireActivity(), "Total Pembayaran Kurang", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        RoomOrder roomOrder = new RoomOrder();
        roomOrder.setRoomCode(room.getRoomCode());
        roomOrder.setKodeRcp(room.getRoomRcp());
        roomOrder.setOrderRoomPayments(payments);
        roomOrder.setChuser(USER_FO.getUserId());

       /* Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonRoomOrder = gson.toJson(roomOrder);

        RequestBody postData = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRoomOrder);
        RequestBody partForm = RequestBody.create(okhttp3.MultipartBody.FORM, jsonRoomOrder);
        RequestBody postData = RequestBody.create(MediaType.parse("multipart/form-data"), jsonRoomOrder);
        */
        progressBar.setVisibility(View.VISIBLE);
        Call<RoomOrderResponse> call = paymentOrderClient.submitPayment(roomOrder);

        call.enqueue(new Callback<RoomOrderResponse>() {
            @Override
            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void navToMain() {
       /* Navigation
                .findNavController(this.getView())
                .navigate(PaymentInvoiceFragmentDirections
                        .actionNavPaymentFragmentToNavListRoomFragment());*/
    }



    private void countTotalPayment() {
        totalPayment = 0;
        if (payments.size() > 0) {
            for (Payment pay : payments) {
                totalPayment = totalPayment + pay.getNominal();
            }
        }
        valueTotalPayments.setText(AppUtils.formatNominal(totalPayment));
        if (totalPayment <= 0) {
            valueTotalPayments.setVisibility(View.GONE);
        } else {
            valueTotalPayments.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onRemovePayment(Payment payment, int index) {
        payments.remove(index);
        listPaymentAdapter.notifyDataSetChanged();
        countTotalPayment();
        //listPaymentAdapter.removeFromList(index);
        //Toast.makeText(requireActivity(), "Delete Payment : " + payment.getPaymentType(), Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onEditPayment(Payment payment, int index) {

    }
}