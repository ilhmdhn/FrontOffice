package com.ihp.frontoffice.view.fragment.operasional.invoicepayment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.ihp.frontoffice.helper.utils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ihp.frontoffice.data.remote.respons.xBillResponse;
import com.ihp.frontoffice.helper.Printer;
import com.ihp.frontoffice.view.fragment.operasional.invoicepayment.adapter.OrderItemAdapter;
import com.ihp.frontoffice.view.fragment.operasional.invoicepayment.adapter.TransferItemAdapter;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Inventory;
import com.ihp.frontoffice.data.entity.Invoice;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.entity.Time;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.UserClient;
import com.ihp.frontoffice.data.remote.respons.UserResponse;
import com.ihp.frontoffice.data.repository.IhpRepository;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.helper.UserAuthRole;
import com.ihp.frontoffice.viewmodel.OtherViewModel;
import com.ihp.frontoffice.viewmodel.RoomOrderViewModel;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceFragment extends Fragment {

    @BindView(R.id.tv_room_price)
    TextView tvRoomPrice;

    @BindView(R.id.tv_clock)
    TextView tvClock;

    @BindView(R.id.tv_room_discount)
    TextView tvRoomDiscountPrice;

    @BindView(R.id.tv_dummy_voucher)
    TextView tvDummyVoucher;

    @BindView(R.id.tv_voucher_value)
    TextView tvVoucherValue;

    @BindView(R.id.tv_room_plus_fnb)
    TextView tvRoomPlusFnb;

    @BindView(R.id.tv_service_price)
    TextView tvServicePrice;

    @BindView(R.id.tv_tax_price)
    TextView tvTaxPrice;

    @BindView(R.id.rv_order_fnb)
    RecyclerView rvOrder;

    @BindView(R.id.rv_transfer)
    RecyclerView rvTransfer;

    @BindView(R.id.info_value_total_invoice)
    TextView infoValueTotalInvoice;

    @BindView(R.id.sv_invoice)
    ScrollView svInvoice;

    @BindView(R.id.payment_progressbar)
    MKLoader loadingIndicator;

    @BindView(R.id.btn_to_print)
    ImageButton btnToPrint;

    @BindView(R.id.btn_to_payment)
    AppCompatButton btnToPayment;

    @BindView(R.id.tv_dp_nominal)
    TextView tvDpNominal;

    @BindView(R.id.tv_dummy_dp)
    TextView tvDpDummy;



    private Invoice invoice;
    private Time timeRcp;
    private IhpRepository ihpRepository;
    private String user;
    private String userLevel;
    private OtherViewModel otherViewModel;

    private OrderItemAdapter orderItemAdapter;
    private TransferItemAdapter transferAdapter;
    private RoomOrderViewModel roomOrderViewModel;
    private RoomOrder roomOrder;
    private static String BASE_URL;
    private Printer printer;
    private static final String ARG_PARAM1 = "INVOICE";
    private static final String ARG_PARAM2 = "ROOM_ORDER";
    private static final String ARG_PARAM3 = "TIME_RCP";
    private Integer statusPrint;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    public static InvoiceFragment newInstance(Invoice invoice, RoomOrder roomOrder, Time timeRcp) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, invoice);
        args.putSerializable(ARG_PARAM2, roomOrder);
        args.putSerializable(ARG_PARAM3, timeRcp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            invoice = (Invoice) getArguments().getSerializable(ARG_PARAM1);
            roomOrder = (RoomOrder) getArguments().getSerializable(ARG_PARAM2);
            timeRcp = (Time) getArguments().getSerializable(ARG_PARAM3);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        ButterKnife.bind(this, view);

        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        user = ((MyApp) requireActivity().getApplicationContext()).getUserFo().getUserId();
        userLevel = ((MyApp) requireActivity().getApplicationContext()).getUserFo().getLevelUser();
        ihpRepository = new IhpRepository();
        otherViewModel = new OtherViewModel();
        printer = new Printer();
        orderItemAdapter = new OrderItemAdapter();
        transferAdapter =  new TransferItemAdapter();

        SharedPreferences sharedPref = requireActivity().getSharedPreferences(getString(R.string.preference_print), Context.MODE_PRIVATE);
        statusPrint = sharedPref.getInt(getString(R.string.preference_print), 2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        roomOrderViewModel = new ViewModelProvider(requireActivity())
                .get(RoomOrderViewModel.class);
        roomOrderViewModel.init(BASE_URL);
        btnToPayment.setOnClickListener(view -> {
            GlobalBus
                    .getBus()
                    .post(new EventsWrapper.NavigationInvoicePayment(true));
        });

        btnToPrint.setOnClickListener(view -> {

            if (statusPrint == 1 || statusPrint == 3){
                otherViewModel.getBillData(BASE_URL, roomOrder.getCheckinRoom().getRoomCode()).observe(getViewLifecycleOwner(), data->{
                    if (Boolean.TRUE.equals(data.getState())){

                        String statusPrint = Objects.requireNonNull(Objects.requireNonNull(data.getData()).getDataInvoice()).getStatusPrint();

                        if (Objects.equals(statusPrint, "0")){

                            AlertDialog.Builder builder;

                            builder = new AlertDialog.Builder(requireActivity());

                            builder.setMessage("")
                                    .setCancelable(true)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (printer.printBill(data, user, requireActivity(), false)){
                                                ihpRepository.updateStatusPrint(BASE_URL, roomOrder.getCheckinRoom().getRoomRcp(), "-2", requireActivity());
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Cetak Tagihan?");
                            alert.show();
                        }else if( Objects.equals(statusPrint, "-2")){
                            AlertDialog.Builder builder;

                            builder = new AlertDialog.Builder(requireActivity());

                            builder
                                    .setCancelable(true)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (printer.printBill(data, user, requireActivity(), true)){
                                                ihpRepository.updateStatusPrint(BASE_URL, roomOrder.getCheckinRoom().getRoomRcp(), "-1", requireActivity());
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Cetak Ulang Tagihan?");
                            alert.show();
                        }

                        else{
                            //hari ini
                            otherViewModel.getJumlahApproval(BASE_URL, user).observe(getViewLifecycleOwner(), dataApproval -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialogDark);
                                boolean kasirApproval = dataApproval.getState();
                                if (kasirApproval) {
                                    builder.setMessage(R.string.ask_reprint_bill);
                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            otherViewModel.getBillData(BASE_URL, roomOrder.getCheckinRoom().getRoomCode()).observe(getViewLifecycleOwner(), data->{
                                                if (Boolean.TRUE.equals(data.getState())){
                                                    boolean statusPrint = printer.printBill(data, user, requireActivity(), true);
                                                    if (statusPrint){
                                                        ihpRepository.submitApproval(BASE_URL, user, userLevel, roomOrder.getCheckinRoom().getRoomCode(), "Reprint Bill");
                                                    }
                                                }else{
                                                    Toast.makeText(requireActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            dialogInterface.dismiss();
                                        }
                                    });

                                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    AlertDialog dialogReprintBill = builder.create();
                                    dialogReprintBill.show();
                                } else {
                                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme);
                                    LayoutInflater dialogInflater = this.getLayoutInflater();
                                    View dialogView = dialogInflater.inflate(R.layout.dialog_otorisasi, null);
                                    dialogBuilder.setView(dialogView);
                                    dialogBuilder.setCancelable(false);
                                    AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
                                    AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);

                                    EditText _usernameTxt = dialogView.findViewById(R.id.input_username_otorisasi);
                                    EditText _passwordTxt = dialogView.findViewById(R.id.input_password_otorisasi);
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
                                            UserClient userClient = ApiRestService.getClient(BASE_URL).create(UserClient.class);
                                            Call<UserResponse> call = userClient.login(email, password);
                                            //---------------------

                                            call.enqueue(new Callback<UserResponse>() {
                                                @Override
                                                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                                    UserResponse res = response.body();
                                                    //_loginProgress.setVisibility(View.GONE);
                                                    res.displayMessage(requireActivity());
                                                    if (res.isOkay()) {
                                                        User userCek = res.getUser();
                                                        if (UserAuthRole.isAllowReprintBill(userCek)) {
                                                            otherViewModel.getBillData(BASE_URL, roomOrder.getCheckinRoom().getRoomCode()).observe(getViewLifecycleOwner(), data->{
                                                                if (Boolean.TRUE.equals(data.getState())){
                                                                    boolean statusPrint = printer.printBill(data, user, requireActivity(), true);
                                                                    if (statusPrint){
                                                                        ihpRepository.submitApproval(BASE_URL, user, userLevel, roomOrder.getCheckinRoom().getRoomCode(), "Reprint Bill");
                                                                    }
                                                                }else{
                                                                    Toast.makeText(requireActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            //---------------------
                                        });
                                        buttonCancel.setOnClickListener(it -> {
                                            alertDialog.dismiss();
                                        });
                                    });
                                    alertDialog.show();
                                }
                            });
                            //hari ini
                        }

                    }else{
                        Toast.makeText(requireActivity(), "opo kene? 1 "+data.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //Print POS LORONG

            if (statusPrint == 2 || statusPrint ==3) {
            GlobalBus.getBus()
                    .post(new EventsWrapper
                            .PrintBillInvoice(roomOrder.getCheckinRoom().getRoomCode()));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        otherViewModel.getBillData(BASE_URL, roomOrder.getCheckinRoom().getRoomCode()).observe(getViewLifecycleOwner(), dataBill ->{

            if(dataBill.getState() == null){
                svInvoice.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.VISIBLE);
            }else{
                loadingIndicator.setVisibility(View.GONE);
                if(dataBill.getState()){
                    svInvoice.setVisibility(View.VISIBLE);
                    createInvoiceLayout(dataBill);
                }else{
                    Toasty.error(requireActivity(), dataBill.getMessage()).show();
                }
            }
        });
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    void createInvoiceLayout(xBillResponse data){

        orderItemAdapter.setData(data.getData());
        transferAdapter.setData(data.getData().getTransferListData());
        orderItemAdapter.notifyDataSetChanged();
        transferAdapter.notifyDataSetChanged();

        if(data.getData().getDataInvoice().getUangMuka() == 0){
            tvDpDummy.setVisibility(View.GONE);
            tvDpNominal.setVisibility(View.GONE);
        }else{
            tvDpNominal.setText(AppUtils.formatNominal(Double.valueOf(data.getData().getDataInvoice().getUangMuka())));
        }

        rvOrder.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvOrder.setHasFixedSize(true);
        rvOrder.setAdapter(orderItemAdapter);

        rvTransfer.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvTransfer.setHasFixedSize(true);
        rvTransfer.setAdapter(transferAdapter);

        tvRoomPrice.setText(AppUtils.formatNominal(Double.valueOf(data.getData().getDataInvoice().getSewaRuangan())));
        tvRoomDiscountPrice.setText("("+AppUtils.formatNominal(Double.valueOf(data.getData().getDataInvoice().getPromo()))+")");
        tvClock.setText(data.getData().getDataRoom().getCheckin() +" - "+data.getData().getDataRoom().getCheckout());
        tvRoomPlusFnb.setText(AppUtils.formatNominal(Double.valueOf(data.getData().getDataInvoice().getJumlahRuangan() + data.getData().getDataInvoice().getJumlahPenjualan())));
        tvServicePrice.setText(AppUtils.formatNominal(Double.valueOf(data.getData().getDataInvoice().getJumlahService())));
        tvTaxPrice.setText(AppUtils.formatNominal(Double.valueOf(data.getData().getDataInvoice().getJumlahPajak())));
        infoValueTotalInvoice.setText(AppUtils.formatNominal(Double.valueOf(data.getData().getDataInvoice().getJumlahBersih())));
        if(data.getData().getDataInvoice().getVoucher() == 0){
            tvDummyVoucher.setVisibility(View.GONE);
            tvVoucherValue.setVisibility(View.GONE);
        }else{
            tvVoucherValue.setText(AppUtils.formatNominal(Double.valueOf(data.getData().getDataInvoice().getVoucher())));
        }
    }
}