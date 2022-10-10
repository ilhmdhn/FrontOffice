package com.ihp.frontoffice.view.fragment.operasional.invoicepayment;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.ihp.frontoffice.helper.Printer;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Invoice;
import com.ihp.frontoffice.data.entity.Payment;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.entity.TypeEdc;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.MemberClient;
import com.ihp.frontoffice.data.remote.PaymentOrderClient;
import com.ihp.frontoffice.data.remote.UserClient;
import com.ihp.frontoffice.data.remote.respons.EdcTypeResponse;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import com.ihp.frontoffice.data.remote.respons.UserResponse;
import com.ihp.frontoffice.data.repository.IhpRepository;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.helper.UserAuthRole;
import com.ihp.frontoffice.view.listadapter.ListEdcTypeAdapter;
import com.ihp.frontoffice.view.listadapter.ListPaymentAdapter;
import com.ihp.frontoffice.viewmodel.OtherViewModel;
import com.ihp.frontoffice.viewmodel.RoomOrderViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment
        implements ListPaymentAdapter.RemovePayment, ListPaymentAdapter.EditPayment {

    @BindView(R.id.btn_add_more_payment)
    Button buttonAddMorePayment;
    @BindView(R.id.btn_submit_payment)
    Button buttonSubmitPayment;
    @BindView(R.id.payment_progressbar)
    MKLoader progressBar;
    @BindView(R.id.payment_list_recyclerview)
    RecyclerView paymentListRecycle;
    @BindView(R.id.info_value_total_payments)
    TextView infoValueTotalPayments;
    @BindView(R.id.info_value_reduce_payments)
    TextView infoValueReducePayments;
    @BindView(R.id.info_value_total_invoice)
    TextView infoValueTotalInvoice;

    @BindView(R.id.label_1)
    TextView labelTotal;

    @BindView(R.id.label_2)
    TextView labelReduce;
    //TODO :: init chip group type payment
    @BindView(R.id.chipgroup_payment_type)
    ChipGroup typePayment;
    //TODO :: init view layout component
    @BindView(R.id.view_payment_debet_credit)
    View viewPaymentDebetCredit;
    @BindView(R.id.view_payment_emoney)
    View viewPaymentEmoney;
    @BindView(R.id.view_payment_compliment)
    View viewPaymentCompliment;
    @BindView(R.id.view_payment_piutang)
    View viewPaymentPiutang;

    //TODO :: init default input
    @BindView(R.id.payment_input_nominal)
    TextInputLayout inputNominalPayment;

    //TODO :: init optional input debet||credit
    @BindView(R.id.button_payment_choose_edc)
    AppCompatButton buttonChooseEdc;
    @BindView(R.id.txt_payment_edc)
    TextView txtEdcDebetCredit;
    @BindView(R.id.button_payment_choose_card)
    AppCompatButton buttonChooseCard;
    @BindView(R.id.txt_payment_card)
    TextView txtCardDebetCredit;
    @BindView(R.id.input_payment_nama_debetcredit)
    TextInputLayout inputNamaDebetCredit;
    @BindView(R.id.input_payment_cardnumber_debetcredit)
    TextInputLayout inputCardNumberDebetCredit;
    @BindView(R.id.input_payment_approval_number_debetcredit)
    TextInputLayout inputApprovalDebetCredit;

    //TODO :: init optional input emoney
    @BindView(R.id.type_emoney)
    Spinner spinnerTypePaymentEmoney;
    @BindView(R.id.input_payment_nama_emoney)
    TextInputLayout inputNamaEmoney;
    @BindView(R.id.input_payment_account_emoney)
    TextInputLayout inputPaymentAkunEmoney;
    @BindView(R.id.input_payment_ref_trans_emoney)
    TextInputLayout inputRefKodeEmoney;
    //TODO :: init optional input compliment

    @BindView(R.id.input_payment_nama_compliment)
    TextInputLayout inputNamaCompliment;
    @BindView(R.id.input_payment_instansi_compliment)
    TextInputLayout inputInstansiCompliment;
    @BindView(R.id.input_payment_instruksi_compliment)
    TextInputLayout inputInstruksiCompliment;
    //TODO :: init optional input piutang

    @BindView(R.id.type_piutang)
    Spinner spinnerTypePaymentPiutang;
    @BindView(R.id.input_payment_nama_piutang)
    TextInputLayout inputNamaPiutang;
    @BindView(R.id.input_payment_id_member_piutang)
    TextInputLayout inputIdMemberPiutang;

    @BindView(R.id.btn_to_invoice)
    ImageButton btnToInvoice;


    private MemberClient memberClient;
    private PaymentOrderClient paymentOrderClient;
    private ArrayList<TypeEdc> typesListEdc = new ArrayList<>();
    private ListEdcTypeAdapter listEdcTypeAdapter;
    private String[] banks;
    private ArrayAdapter<String> adapterBanks;


    private Invoice invoice;

    private String defaultPaymentType;
    private String emoneyPaymentType;
    private String piutangPaymentType;
    private final ArrayList<Payment> payments = new ArrayList<>();
    private ListPaymentAdapter listPaymentAdapter;
    private static String EMPTY_EDC_DP;
    private static String EMPTY_CARD_DP;

    private RoomOrderViewModel roomOrderViewModel;

    private Room room;
    private double totalPayment;
    private static String BASE_URL;
    private User USER_FO;
    private double reminderValue;
    private static final String ARG_PARAM1 = "INVOICE";
    private static final String ARG_PARAM2 = "ROOM";
    private ArrayAdapter<CharSequence> adapterPaymentEmoney;
    private ArrayAdapter<CharSequence> adapterPaymentPiutang;
    private boolean isEdited = false;
    private int indexEditPayment;
    private static final String EMPTY_STRING = "";
    private IhpRepository ihpRepository;
    private OtherViewModel otherViewModel;
    private final boolean kasirApproval = false;
    private Printer printer;

    private String current = "";

    public PaymentFragment() {
        // Required empty public constructor
    }


    public static PaymentFragment newInstance(Invoice invoice, Room room) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, invoice);
        args.putSerializable(ARG_PARAM2, room);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            invoice = (Invoice) getArguments().getSerializable(ARG_PARAM1);
            room = (Room) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this, view);

        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();
        memberClient = ApiRestService.getClient(BASE_URL).create(MemberClient.class);
        paymentOrderClient = ApiRestService.getClient(BASE_URL).create(PaymentOrderClient.class);
        printer = new Printer();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ihpRepository = new IhpRepository();
        otherViewModel = new ViewModelProvider(requireActivity()).get(OtherViewModel.class);

        buttonAddMorePayment.setOnClickListener(view -> {
            validationPayments();
        });
        buttonSubmitPayment.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                    .setTitle("Proses Pembayaran")
                    .setMessage("Anda ingin melanjutkan")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            validatePayment();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

        });
        roomOrderViewModel = new ViewModelProvider(getActivity())
                .get(RoomOrderViewModel.class);
        roomOrderViewModel.init(BASE_URL);
        inputNominalPayment.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().length()>11){
                    inputNominalPayment.getEditText().setText(current);
                    inputNominalPayment.getEditText().setSelection(current.length());
                    Toasty
                            .warning(getContext(),"Pembayaran Maksimal Ratusan Juta")
                            .show();
                    return;
                }
                if (!s.toString().equals(current)) {
                    inputNominalPayment.getEditText().removeTextChangedListener(this);

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
                    inputNominalPayment.getEditText().setText(formattedDotNonRp);
                    inputNominalPayment.getEditText().setSelection(formattedDotNonRp.length());
                    inputNominalPayment.getEditText().addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnToInvoice.setOnClickListener(view -> {
            GlobalBus
                    .getBus()
                    .post(new EventsWrapper.NavigationPaymentInvoice(true));
        });

        initTypePayment();
        initEdcType();


    }


    private void initDefaultCashPayment() {
        payments.clear();
        listPaymentAdapter.notifyDataSetChanged();
        Payment defaultCashPayment = new Payment();
        //AppUtils.formatRupiah(invoice.getTotalFinal())
        defaultCashPayment.setNominal(invoice.getTotalFinal());
        defaultCashPayment.setPaymentType("CASH");
        payments.add(defaultCashPayment);
        countTotalPayment();

        listPaymentAdapter.notifyDataSetChanged();
    }


    private void initTypePayment() {
        txtEdcDebetCredit.setText(EMPTY_EDC_DP);
        txtCardDebetCredit.setText(EMPTY_CARD_DP);
        infoValueTotalInvoice.setText(AppUtils.formatNominal(invoice.getTotalFinal()));
        inputIdMemberPiutang.getEditText().setText(room.getMemberCode());
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
                buttonChooseCard.setTextColor(Color.BLUE);
                txtCardDebetCredit.setText(EMPTY_CARD_DP);

            }
        });

        //TODO :: add event on component type payment emoney
        adapterPaymentEmoney = ArrayAdapter.createFromResource(getContext(),
                R.array.emoney_type, android.R.layout.simple_spinner_item);
        adapterPaymentEmoney.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emoneyPaymentType = adapterPaymentEmoney.getItem(0).toString();
        spinnerTypePaymentEmoney.setAdapter(adapterPaymentEmoney);
        spinnerTypePaymentEmoney.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                emoneyPaymentType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                emoneyPaymentType = adapterPaymentEmoney.getItem(0).toString();
            }
        });

        //TODO :: add event on component type payment piutang
        adapterPaymentPiutang = ArrayAdapter.createFromResource(getContext(),
                R.array.piutang_type, android.R.layout.simple_spinner_item);
        adapterPaymentPiutang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        piutangPaymentType = adapterPaymentPiutang.getItem(0).toString();
        spinnerTypePaymentPiutang.setAdapter(adapterPaymentPiutang);
        spinnerTypePaymentPiutang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                piutangPaymentType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                piutangPaymentType = adapterPaymentPiutang.getItem(0).toString();
            }
        });

        setupPaymentRecyclerView();
        initDefaultCashPayment();
        countTotalPayment();
    }

    private void dialogEdcType() {
        if (typesListEdc.size() < 1) {
            initEdcType();
        }

        listEdcTypeAdapter = new ListEdcTypeAdapter(getContext(), typesListEdc);
        listEdcTypeAdapter.notifyDataSetChanged();

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Pilih EDC")
                .setSingleChoiceItems(listEdcTypeAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtEdcDebetCredit.setText(listEdcTypeAdapter.getItem(i).getEdcName());
                        txtEdcDebetCredit.setTag(listEdcTypeAdapter.getItem(i));
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
        adapterBanks = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, banks);

        new MaterialAlertDialogBuilder(getContext())
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
        visibleProgressBar(true);
        Call<EdcTypeResponse> call = memberClient.getEdc();
        call.enqueue(new Callback<EdcTypeResponse>() {
            @Override
            public void onResponse(Call<EdcTypeResponse> call, Response<EdcTypeResponse> response) {

                visibleProgressBar(false);
                EdcTypeResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    return;
                }
                typesListEdc.addAll(res.getTypeEdcs());
            }

            @Override
            public void onFailure(Call<EdcTypeResponse> call, Throwable t) {
                Toast.makeText(getContext(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();

                visibleProgressBar(false);
            }
        });
    }

    private void setupPaymentRecyclerView() {
        if (listPaymentAdapter == null) {
            listPaymentAdapter = new ListPaymentAdapter(getContext(), payments, this::onRemovePayment, this::onEditPayment);
            paymentListRecycle.setAdapter(listPaymentAdapter);
            paymentListRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
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
            //input.setError(null);
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
            if(inputVal.length()!=6){
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
            if(inputVal.length()!=4){
                return false;
            }
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
            typePayment.check(R.id.payment_cash);
        } else if (type.equals("DEBET CARD")) {
            txtEdcDebetCredit.setText(EMPTY_EDC_DP);
            txtEdcDebetCredit.setTag(null);
            txtCardDebetCredit.setText(EMPTY_CARD_DP);
            defaultPaymentType = "DEBET CARD";
            viewPaymentDebetCredit.setVisibility(View.VISIBLE);
        } else if (type.equals("CREDIT CARD")) {
            txtEdcDebetCredit.setText(EMPTY_EDC_DP);
            txtEdcDebetCredit.setTag(null);
            txtCardDebetCredit.setText(EMPTY_CARD_DP);
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


    //TODO :: submit data payment
    private void validatePayment() {

        if (totalPayment < invoice.getTotalFinal()) {
            Toast.makeText(getContext(), "Total Pembayaran Kurang", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme)
                .setTitle("Email Invoice")
                .setMessage("Apakah invoice akan di email??")
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       submitPayment(true);
                    }
                })
                .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        submitPayment(false);
                    }
                })
                .show();



    }

    private void submitPayment(boolean isInvoiceMailed){
        RoomOrder roomOrder = new RoomOrder();
        roomOrder.setRoomCode(room.getRoomCode());
        roomOrder.setKodeRcp(room.getRoomRcp());
        roomOrder.setOrderRoomPayments(payments);
        roomOrder.setChuser(USER_FO.getUserId());
        roomOrder.setSendEmailInvoice(isInvoiceMailed);

        visibleProgressBar(true);
        Call<RoomOrderResponse> call = paymentOrderClient.submitPayment(roomOrder);

        call.enqueue(new Callback<RoomOrderResponse>() {
            @Override
            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {

                visibleProgressBar(false);
                RoomOrderResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    return;
                }
                otherViewModel.getInvoiceData(BASE_URL, room.getRoomRcp()).observe(getViewLifecycleOwner(), data->{
                    if (Boolean.TRUE.equals(data.getState())){
                        if (printer.printInvoice(data, requireActivity(), false)){
                            ihpRepository.updateStatusPrint(BASE_URL, room.getRoomRcp(), "2", requireActivity());
                        }
                    }else{
                        Toast.makeText(requireActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                navToMain();
//                ihpRepository.printInvoice(BASE_URL, room.getRoomRcp(), requireActivity());
            }

            @Override
            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();

                visibleProgressBar(false);
            }
        });
    }

    private void navToMain() {
    /*   Navigation
                .findNavController(this.getView())
                .navigate(OperasionalPaymentFragmentDirections
                        .actionNavOperasionalPaymentFragmentToNavOperasionalListRoomToPaymentFragment());*/
        Navigation
                .findNavController(this.getView())
                .popBackStack();
    }


    private void countTotalPayment() {
        totalPayment = 0;
        if (payments.size() > 0) {
            for (Payment pay : payments) {
                totalPayment = totalPayment + pay.getNominal();
            }
        }

        labelTotal.setText("Total");
        infoValueTotalPayments.setText(AppUtils.formatNominal(totalPayment));

        if (totalPayment == invoice.getTotalFinal()) {
            reminderValue = invoice.getTotalFinal() - totalPayment;
            infoValueReducePayments.setText("0");
            labelReduce.setText("Total");
            infoValueReducePayments.setVisibility(View.GONE);
            labelReduce.setVisibility(View.GONE);
        } else if (totalPayment < invoice.getTotalFinal()) {
            reminderValue = invoice.getTotalFinal() - totalPayment;
            infoValueReducePayments.setText(AppUtils.formatNominal(reminderValue));
            labelReduce.setText("Kurang");
            infoValueReducePayments.setVisibility(View.VISIBLE);
            labelReduce.setVisibility(View.VISIBLE);
        } else if (totalPayment > invoice.getTotalFinal()) {
            reminderValue = invoice.getTotalFinal() - totalPayment;
            infoValueReducePayments.setText(AppUtils.formatNominal(totalPayment - invoice.getTotalFinal()));
            labelReduce.setText("Lebih");
            infoValueReducePayments.setVisibility(View.VISIBLE);
            labelReduce.setVisibility(View.VISIBLE);
        }
        if (reminderValue > 0) {
            inputNominalPayment.getEditText().setText(String.valueOf((int) reminderValue));
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onRemovePayment(Payment payment, int index) {
        payments.remove(index);
        listPaymentAdapter.notifyDataSetChanged();
        countTotalPayment();
        isEdited = false;
    }

    private void clearInputPayment() {
        inputNominalPayment.getEditText().setText(EMPTY_STRING);
        //TODO :: clear debet credit input
        buttonChooseEdc.setText("Pilih");
        buttonChooseEdc.setTextColor(Color.BLUE);
        txtEdcDebetCredit.setText(EMPTY_EDC_DP);
        txtEdcDebetCredit.setTag(null);
        buttonChooseCard.setText("Pilih");
        buttonChooseCard.setTextColor(Color.BLUE);
        txtCardDebetCredit.setText(EMPTY_CARD_DP);
        inputNamaDebetCredit.getEditText().setText(EMPTY_STRING);
        inputApprovalDebetCredit.getEditText().setText(EMPTY_STRING);
        inputCardNumberDebetCredit.getEditText().setText(EMPTY_STRING);
        //TODO :: clear emoney input
        inputNamaEmoney.getEditText().setText(EMPTY_STRING);
        inputPaymentAkunEmoney.getEditText().setText(EMPTY_STRING);
        inputRefKodeEmoney.getEditText().setText(EMPTY_STRING);
        //TODO :: clear compliment input
        inputNamaCompliment.getEditText().setText(EMPTY_STRING);
        inputInstansiCompliment.getEditText().setText(EMPTY_STRING);
        inputInstruksiCompliment.getEditText().setText(EMPTY_STRING);
        //TODO :: clear piutang input
        inputNamaPiutang.getEditText().setText(EMPTY_STRING);
        inputIdMemberPiutang.getEditText().setText(room.getMemberCode());


    }

    // TODO : input payments
    private void validationPayments() {

        //TODO :: add payment element
        Payment payment = null;
        int valuePayment = 0;
        try{
            valuePayment = Integer.parseInt(
                    inputNominalPayment
                            .getEditText()
                            .getText()
                            .toString()
                            .replaceAll("[.]", ""));
        }catch (Exception e){
            Toasty
                    .warning(getContext(), "Mohon Isi Nominal", Toast.LENGTH_SHORT)
                    .show();
        }

        if(valuePayment<1){
            Toasty
                    .warning(getContext(), "Mohon Isi Nominal", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (defaultPaymentType.equals("CASH")) {
            if (!isInputValid(inputNominalPayment.getEditText())) {
                Toast
                        .makeText(getContext(), "Mohon Isi Nominal", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            payment = new Payment();

            payment.setNominal(valuePayment);
            payment.setPaymentType(defaultPaymentType);
            addPayments(payment);
        } else if (defaultPaymentType.equals("DEBET CARD")) {
            if (!isInputValid(inputNominalPayment.getEditText()) ||
                    !isInputValid(inputNamaDebetCredit.getEditText()) ||
                    !isInputValidApprovalCode(inputApprovalDebetCredit.getEditText()) ||
                    !isInputValidNumberCard(inputCardNumberDebetCredit.getEditText()) ||
                    txtCardDebetCredit.getText().toString().equals(EMPTY_CARD_DP) ||
                    txtEdcDebetCredit.getText().toString().equals(EMPTY_EDC_DP)

            ) {
                Toasty.warning(getContext(), "Mohon Lengkapi Inputan Debet", Toast.LENGTH_SHORT, true)
                        .show();
                return;
            }
            payment = new Payment();
            payment.setNominal(valuePayment);
            payment.setPaymentType(defaultPaymentType);

            payment.setEdcDebet(txtEdcDebetCredit.getText().toString());
            payment.setTypeEdc((TypeEdc) txtEdcDebetCredit.getTag());
            payment.setCardDebet(txtCardDebetCredit.getText().toString());
            payment.setNamaUserDebet(inputNamaDebetCredit.getEditText().getText().toString());
            payment.setApprovalCodeDebet(inputApprovalDebetCredit.getEditText().getText().toString());
            payment.setCardCodeDebet(inputCardNumberDebetCredit.getEditText().getText().toString());
            addPayments(payment);
        } else if (defaultPaymentType.equals("CREDIT CARD")) {
            if (!isInputValid(inputNominalPayment.getEditText()) ||
                    !isInputValid(inputNamaDebetCredit.getEditText()) ||
                    !isInputValid(inputApprovalDebetCredit.getEditText()) ||
                    !isInputValid(inputCardNumberDebetCredit.getEditText()) ||
                    txtCardDebetCredit.getText().toString().equals(EMPTY_CARD_DP) ||
                    txtEdcDebetCredit.getText().toString().equals(EMPTY_EDC_DP)
            ) {
                Toasty.warning(getContext(), "Mohon Lengkapi Inputan Credit", Toast.LENGTH_SHORT, true)
                        .show();
                return;
            }
            payment = new Payment();
            payment.setNominal(valuePayment);
            payment.setPaymentType(defaultPaymentType);

            payment.setEdcCredit(txtEdcDebetCredit.getText().toString());
            payment.setTypeEdc((TypeEdc) txtEdcDebetCredit.getTag());
            payment.setCardCredit(txtCardDebetCredit.getText().toString());
            payment.setNamaUserCredit(inputNamaDebetCredit.getEditText().getText().toString());
            payment.setApprovalCodeCredit(inputApprovalDebetCredit.getEditText().getText().toString());
            payment.setCardCodeCredit(inputCardNumberDebetCredit.getEditText().getText().toString());
            addPayments(payment);
        } else if (defaultPaymentType.equals("E-MONEY")) {
            if (!isInputValid(inputNominalPayment.getEditText()) ||
                    !isInputValid(inputNamaEmoney.getEditText()) ||
                    !isInputValid(inputPaymentAkunEmoney.getEditText()) ||
                    !isInputValid(inputRefKodeEmoney.getEditText())
            ) {
                Toasty.warning(getContext(), "Mohon Lengkapi Inputan E-money", Toast.LENGTH_SHORT, true)
                        .show();
                return;
            }
            payment = new Payment();
            payment.setNominal(valuePayment);
            payment.setPaymentType(defaultPaymentType);
            payment.setTypeEmoney(emoneyPaymentType);
            payment.setNamaUserEmoney(inputNamaEmoney.getEditText().getText().toString());
            payment.setAccountEmoney(inputPaymentAkunEmoney.getEditText().getText().toString());
            payment.setRefCodeEmoney(inputRefKodeEmoney.getEditText().getText().toString());
            addPayments(payment);
        } else if (defaultPaymentType.equals("COMPLIMENTARY")) {
            if (!isInputValid(inputNominalPayment.getEditText()) ||
                    !isInputValid(inputNamaCompliment.getEditText()) ||
                    !isInputValid(inputInstansiCompliment.getEditText()) ||
                    !isInputValid(inputInstruksiCompliment.getEditText())
            ) {
                Toasty.warning(getContext(), "Mohon Lengkapi Inputan Compliment", Toast.LENGTH_SHORT, true)
                        .show();
                return;
            }
            payment = new Payment();
            payment.setNominal(valuePayment);
            payment.setPaymentType(defaultPaymentType);
            payment.setNamaUserCompliment(inputNamaCompliment.getEditText().getText().toString());
            payment.setInstansiCompliment(inputInstansiCompliment.getEditText().getText().toString());
            payment.setInstruksiCompliment(inputInstruksiCompliment.getEditText().getText().toString());

            authPayment(payment);
        } else if (defaultPaymentType.equals("PIUTANG")) {
            if (!isInputValid(inputNominalPayment.getEditText()) ||
                    !isInputValid(inputNamaPiutang.getEditText()) ||
                    !isInputValid(inputIdMemberPiutang.getEditText()) ||
                    piutangPaymentType.isEmpty() || piutangPaymentType.equals("")
            ) {
                Toasty.warning(getContext(), "Mohon Lengkapi Inputan Piutang", Toast.LENGTH_SHORT, true)
                        .show();
                return;
            }
            payment = new Payment();

            payment.setNominal(valuePayment);
            payment.setPaymentType(defaultPaymentType);
            payment.setTypePiutang(piutangPaymentType);
            payment.setNamaUserPiutang(inputNamaPiutang.getEditText().getText().toString());
            payment.setIdMemberPiutang(inputIdMemberPiutang.getEditText().getText().toString());

            authPayment(payment);
        } else {
            Toasty.warning(getContext(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT, true)
                    .show();
            return;
        }
    }

    private void addPayments(Payment pay) {
        if (null == pay) {
            Toast
                    .makeText(getContext(), "Ok Messages", Toast.LENGTH_SHORT)
                    .show();

        } else {
            hideKeyboard(getActivity());
            if (isEdited) {
                payments.remove(indexEditPayment);
                listPaymentAdapter.notifyDataSetChanged();
            }
            isEdited = false;
            payments.add(pay);
            visibleLayoutTypePayment("CASH");
            clearInputPayment();
            countTotalPayment();
            listPaymentAdapter.notifyDataSetChanged();
        }

    }

    private void authPayment(Payment pay) {

        visibleProgressBar(false);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater dialogInflater = this.getLayoutInflater();

        View dialogView = dialogInflater.inflate(R.layout.dialog_otorisasi, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        //dialogBuilder.setTitle("Otentifikasi User");

        AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
        AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);
        TextView infoText = dialogView.findViewById(R.id.text_info_transfer);
        infoText.setText("Otentifikasi User");
        //infoText.setVisibility(View.GONE);

        EditText _usernameTxt = dialogView.findViewById(R.id.input_username_otorisasi);
        EditText _passwordTxt = dialogView.findViewById(R.id.input_password_otorisasi);
        MKLoader _loginProgress = dialogView.findViewById(R.id.progress_dialog);
        otherViewModel.getJumlahApproval(BASE_URL, USER_FO.getUserId()).observe(getActivity(), data ->{
            boolean kasirApproval = data.getState();

            if (kasirApproval){

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.payment_confirmation);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ihpRepository.submitApproval(BASE_URL, USER_FO.getUserId(), USER_FO.getLevelUser(), room.getRoomCode(), "Pembayaran Piutang");
                        addPayments(pay);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
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
                                    if (UserAuthRole.isAllowPiutangComplimentPayment(user)) {
                                        ihpRepository.submitApproval(BASE_URL, user.getUserId(), user.getLevelUser(), room.getRoomCode(), "Pembayaran Piutang");
                                        addPayments(pay);
                                        alertDialog.dismiss();
                                    } else {
                                        Toasty.warning(requireActivity(), "User tidak dapat melakukan operasi ini", Toast.LENGTH_SHORT, true)
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

    @Override
    public void onEditPayment(Payment payment, int index) {
        editPayment(payment, index);
    }

    private void editPayment(Payment editPayment, int index) {
        //TODO:: edit payment value
        isEdited = true;
        indexEditPayment = index;
        inputNominalPayment.getEditText().setText(String.valueOf((int) editPayment.getNominal()));
        if (editPayment.getPaymentType().equals("CASH")) {
            defaultPaymentType = "CASH";
            typePayment.check(R.id.payment_cash);
        } else if (editPayment.getPaymentType().equals("DEBET CARD")) {
            defaultPaymentType = "DEBET CARD";
            typePayment.check(R.id.payment_debet);
            txtEdcDebetCredit.setText(editPayment.getEdcDebet());
            txtEdcDebetCredit.setTag(editPayment.getTypeEdc());
            txtCardDebetCredit.setText(editPayment.getCardDebet());
            inputNamaDebetCredit.getEditText().setText(editPayment.getNamaUserDebet());
            inputCardNumberDebetCredit.getEditText().setText(editPayment.getCardCodeDebet());
            inputApprovalDebetCredit.getEditText().setText(editPayment.getApprovalCodeDebet());
        } else if (editPayment.getPaymentType().equals("CREDIT CARD")) {
            defaultPaymentType = "CREDIT CARD";
            typePayment.check(R.id.payment_credit);
            txtEdcDebetCredit.setText(editPayment.getEdcCredit());
            txtEdcDebetCredit.setTag(editPayment.getTypeEdc());
            txtCardDebetCredit.setText(editPayment.getCardCredit());
            inputNamaDebetCredit.getEditText().setText(editPayment.getNamaUserCredit());
            inputCardNumberDebetCredit.getEditText().setText(editPayment.getCardCodeCredit());
            inputApprovalDebetCredit.getEditText().setText(editPayment.getApprovalCodeCredit());
        } else if (editPayment.getPaymentType().equals("E-MONEY")) {
            defaultPaymentType = "E-MONEY";
            typePayment.check(R.id.payment_emoney);
            if (editPayment.getTypeEmoney().equals(adapterPaymentEmoney.getItem(0).toString())) {
                spinnerTypePaymentEmoney.setSelection(0);
            } else if (editPayment.getTypeEmoney().equals(adapterPaymentEmoney.getItem(1).toString())) {
                spinnerTypePaymentEmoney.setSelection(1);
            } else if (editPayment.getTypeEmoney().equals(adapterPaymentEmoney.getItem(2).toString())) {
                spinnerTypePaymentEmoney.setSelection(2);
            } else if (editPayment.getTypeEmoney().equals(adapterPaymentEmoney.getItem(3).toString())) {
                spinnerTypePaymentEmoney.setSelection(3);
            } else if (editPayment.getTypeEmoney().equals(adapterPaymentEmoney.getItem(4).toString())) {
                spinnerTypePaymentEmoney.setSelection(4);
            } else if (editPayment.getTypeEmoney().equals(adapterPaymentEmoney.getItem(5).toString())) {
                spinnerTypePaymentEmoney.setSelection(5);
            }
            emoneyPaymentType = editPayment.getTypeEmoney();
            inputNamaEmoney.getEditText().setText(editPayment.getNamaUserEmoney());
            inputPaymentAkunEmoney.getEditText().setText(editPayment.getAccountEmoney());
            inputRefKodeEmoney.getEditText().setText(editPayment.getRefCodeEmoney());
        } else if (editPayment.getPaymentType().equals("COMPLIMENTARY")) {
            defaultPaymentType = "COMPLIMENTARY";
            typePayment.check(R.id.payment_complimentary);
            inputNamaCompliment.getEditText().setText(editPayment.getNamaUserCompliment());
            inputInstansiCompliment.getEditText().setText(editPayment.getInstansiCompliment());
            inputInstruksiCompliment.getEditText().setText(editPayment.getInstruksiCompliment());
        } else if (editPayment.getPaymentType().equals("PIUTANG")) {
            defaultPaymentType = "PIUTANG";
            typePayment.check(R.id.payment_piutang);
            if (editPayment.getTypePiutang().equals(adapterPaymentPiutang.getItem(0).toString())) {
                spinnerTypePaymentPiutang.setSelection(0);
            } else if (editPayment.getTypePiutang().equals(adapterPaymentPiutang.getItem(1).toString())) {
                spinnerTypePaymentPiutang.setSelection(1);
            } else if (editPayment.getTypePiutang().equals(adapterPaymentPiutang.getItem(2).toString())) {
                spinnerTypePaymentPiutang.setSelection(2);
            } else if (editPayment.getTypePiutang().equals(adapterPaymentPiutang.getItem(3).toString())) {
                spinnerTypePaymentPiutang.setSelection(3);
            } else if (editPayment.getTypePiutang().equals(adapterPaymentPiutang.getItem(4).toString())) {
                spinnerTypePaymentPiutang.setSelection(4);
            }
            inputNamaPiutang.getEditText().setText(editPayment.getNamaUserPiutang());
            inputIdMemberPiutang.getEditText().setText(editPayment.getIdMemberPiutang());
        }

    }

    private void visibleProgressBar(boolean isVisible){
        if(isVisible){
            progressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else{
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }
}