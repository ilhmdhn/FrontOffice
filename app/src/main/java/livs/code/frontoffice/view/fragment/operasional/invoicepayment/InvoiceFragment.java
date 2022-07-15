package livs.code.frontoffice.view.fragment.operasional.invoicepayment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Inventory;
import livs.code.frontoffice.data.entity.Invoice;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.entity.Time;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.RoomOrderClient;
import livs.code.frontoffice.data.remote.UserClient;
import livs.code.frontoffice.data.remote.respons.PrintStatusResponse;
import livs.code.frontoffice.data.remote.respons.UserResponse;
import livs.code.frontoffice.data.repository.IhpRepository;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.helper.AppUtils;
import livs.code.frontoffice.helper.UserAuthRole;
import livs.code.frontoffice.viewmodel.OtherViewModel;
import livs.code.frontoffice.viewmodel.RoomOrderViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceFragment extends Fragment {

    @BindView(R.id.payment_progressbar)
    MKLoader progressBar;

    @BindView(R.id.tv_value_jam_checkin)
    TextView valueCheckin;

    @BindView(R.id.tv_value_jam_checkout)
    TextView valueCheckout;

    @BindView(R.id.tv_value_jam_durasi)
    TextView valueDurasi;

    @BindView(R.id.value_total_ruangan)
    TextView valueRuangan;

    @BindView(R.id.value_voucher)
    TextView valueVoucher;

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

    @BindView(R.id.label_total_transfer_room)
    TextView labelTransferRuangan;

    @BindView(R.id.value_gift)
    TextView valueGift;

    @BindView(R.id.value_uang_muka)
    TextView valueDownPayment;

    @BindView(R.id.value_final_total)
    TextView valueFinalTotal;

    @BindView(R.id.info_value_total_invoice)
    TextView infoValueTotalInvoice;

    @BindView(R.id.label_voucher)
    TextView labelVoucher;

    @BindView(R.id.label_gift)
    TextView labelGift;

    @BindView(R.id.label_penjualan)
    TextView labelPenjualan;

    @BindView(R.id.btn_to_print)
    ImageButton btnToPrint;

    @BindView(R.id.btn_to_payment)
    AppCompatButton btnToPayment;

    private Invoice invoice;
    private Time timeRcp;
    private IhpRepository ihpRepository;
    private String user;
    private String userLevel;
    private OtherViewModel otherViewModel;

    private RoomOrderViewModel roomOrderViewModel;
    private RoomOrder roomOrder;
    private static String BASE_URL;
    private LinearLayout detailOrderLayout, detailTransferLayout;
    private static final String ARG_PARAM1 = "INVOICE";
    private static final String ARG_PARAM2 = "ROOM_ORDER";
    private static final String ARG_PARAM3 = "TIME_RCP";

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        ButterKnife.bind(this, view);

        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        user = ((MyApp) getActivity().getApplicationContext()).getUserFo().getUserId();
        userLevel = ((MyApp) getActivity().getApplicationContext()).getUserFo().getLevelUser();
        ihpRepository = new IhpRepository();
        otherViewModel = new OtherViewModel();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        roomOrderViewModel = new ViewModelProvider(getActivity())
                .get(RoomOrderViewModel.class);
        roomOrderViewModel.init(BASE_URL);
        initInvoice();
        btnToPayment.setOnClickListener(view -> {
            GlobalBus
                    .getBus()
                    .post(new EventsWrapper.NavigationInvoicePayment(true));
        });

        btnToPrint.setOnClickListener(view -> {
            otherViewModel.printStatus(BASE_URL, roomOrder.getCheckinRoom().getRoomRcp()).observe(getViewLifecycleOwner(), statusPrint -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());


                if (statusPrint.getState()) {
                    switch (statusPrint.getData().getPrint()) {
                        case "-1":
                        case "0":
                            builder.setMessage(R.string.ask_print_bill);

                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ihpRepository.printBill(BASE_URL, roomOrder.getCheckinRoom().getRoomRcp(), user, requireActivity());
                                    dialogInterface.dismiss();
                                }
                            });

                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialogPrintBill = builder.create();
                            dialogPrintBill.show();
                            break;

                        default:
                            otherViewModel.getJumlahApproval(BASE_URL, user).observe(getViewLifecycleOwner(), data -> {
                                boolean kasirApproval = data.getState();
                                Log.d("cek approval kasir ", String.valueOf(kasirApproval));
                                if (kasirApproval) {
                                    builder.setMessage(R.string.ask_reprint_bill);

                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(ihpRepository.printBill(BASE_URL, roomOrder.getCheckinRoom().getRoomRcp(), user, requireActivity())){
                                                ihpRepository.submitApproval(BASE_URL, user, userLevel, roomOrder.getCheckinRoom().getRoomCode(), "Reprint Bill");
                                            }
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
                                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme);
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
                                                Toasty.warning(getContext(), "Anda belum input user dan password ", Toast.LENGTH_SHORT, true)
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
                                                            if(ihpRepository.printBill(BASE_URL, roomOrder.getCheckinRoom().getRoomRcp(), user, requireActivity())){
                                                                ihpRepository.submitApproval(BASE_URL, user, userLevel, roomOrder.getCheckinRoom().getRoomCode(), "Reprint Bill");
                                                            }
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

                                            //---------------------
                                        });
                                        buttonCancel.setOnClickListener(it -> {
                                            alertDialog.dismiss();
                                        });
                                    });
                                    alertDialog.show();
                                }
                            });
                            break;
                    }
                } else {
                    Toasty.error(requireActivity(), statusPrint.getMessage(), Toasty.LENGTH_SHORT, true).show();


                }
            });
//            GlobalBus.getBus()
//                    .post(new EventsWrapper
//                            .PrintBillInvoice(roomOrder.getCheckinRoom().getRoomCode()));
            });
    }

    private void addDetailInventoryOrder() {
        detailOrderLayout = getView().findViewById(R.id.detail_order_layout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Inventory data : roomOrder.getSummaryOrderInventories()) {
            View rowView = inflater.inflate(R.layout.invoice_payment_detail_fnb, null);
            TextView fnbName = rowView.findViewById(R.id.fnb_name);
            TextView labelDisc = rowView.findViewById(R.id.fnb_disc_label);
            TextView fnbQty = rowView.findViewById(R.id.fnb_qty);
            TextView fnbPrice = rowView.findViewById(R.id.fnb_price);
            TextView fnbDiskon = rowView.findViewById(R.id.fnb_diskon);
            TextView fnbTotal = rowView.findViewById(R.id.fnb_total);

            fnbName.setText(data.getInventoryName());
            fnbQty.setText("(" + data.getQty());
            fnbPrice.setText(AppUtils.formatNominal(data.getUnitPrice()) + ")");
            if (data.getTotalDiscount() > 0) {
                fnbDiskon.setText("(" + AppUtils.formatNominal(data.getTotalDiscount())+")");
                //labelDisc.setVisibility(View.VISIBLE);
            } else {
                fnbDiskon.setVisibility(View.GONE);
                labelDisc.setVisibility(View.GONE);
            }
            fnbTotal.setText(AppUtils.formatNominal(data.getTotalAfterDiscount()+data.getTotalDiscount()));

            detailOrderLayout.addView(rowView);
        }
    }

    private void addDetailTransferRoom() {
        detailTransferLayout = getView().findViewById(R.id.detail_room_transfer_layout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Room roomCheckin = roomOrder.getCheckinRoom();
        for (Room room : roomOrder.getHistoryTransferOrderRoom()) {
            if (!room.getRoomCode().equals(roomCheckin.getRoomCode())) {
                View rowView = inflater.inflate(R.layout.invoice_payment_detail_room_transfer, null);
                TextView roomCode = rowView.findViewById(R.id.room_code);
                TextView total = rowView.findViewById(R.id.total);

                roomCode.setText(room.getRoomType() +" "+ room.getRoomCode());
                total.setText(AppUtils.formatNominal(room.getTotalAllInvoice()));

                detailTransferLayout.addView(rowView);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void initInvoice() {
        labelPenjualan.setVisibility(View.GONE);
        if (roomOrder.getSummaryOrderInventories().size() > 0) {
            labelPenjualan.setVisibility(View.VISIBLE);
            addDetailInventoryOrder();
        }

        if (roomOrder.getHistoryTransferOrderRoom().size() > 1) {
            labelTransferRuangan.setVisibility(View.VISIBLE);
            addDetailTransferRoom();
        }else{
            labelTransferRuangan.setVisibility(View.GONE);
        }

        valueRuangan.setText(AppUtils.formatNominal(invoice.getTotalRoom()));

        //valueFnb.setText(AppUtils.formatNominal(invoice.getTotalInventory()));
        valueCheckin.setText(timeRcp.getCheckinTime());
        valueCheckout.setText(timeRcp.getCheckoutTime());
        valueDurasi.setText(timeRcp.getTimeClock()+" jam");

        valueJumlah.setText(AppUtils.formatNominal(invoice.getTotalRoomAndInventory()));
        valueService.setText(AppUtils.formatNominal(invoice.getTotalService()));
        valueTax.setText(AppUtils.formatNominal(invoice.getTotalTax()));
        valueJumlahTotal.setText(AppUtils.formatNominal(invoice.getTotalAll()));

        valueTransferRuangan.setText(AppUtils.formatNominal(invoice.getTotalTransfer()));
        valueTransferRuangan.setVisibility(View.INVISIBLE);
        valueDownPayment.setText(AppUtils.formatNominal(invoice.getTotalUangMuka()));
        if (invoice.isGift()) {
            valueVoucher.setText("Rp0");
            valueVoucher.setVisibility(View.GONE);
            valueGift.setText(AppUtils.formatNominal(invoice.getTotalVoucher()));
            if (invoice.getTotalVoucher() < 1) {
                valueGift.setVisibility(View.GONE);
                labelVoucher.setVisibility(View.GONE);
                labelGift.setVisibility(View.GONE);
            }
        } else {
            valueVoucher.setText(AppUtils.formatNominal(invoice.getTotalVoucher()));
            valueGift.setText("Rp0");
            valueGift.setVisibility(View.GONE);
            if (invoice.getTotalVoucher() < 1) {
                valueVoucher.setVisibility(View.GONE);
                labelVoucher.setVisibility(View.GONE);
                labelGift.setVisibility(View.GONE);
            }
        }


        valueFinalTotal.setText(AppUtils.formatNominal(invoice.getTotalFinal()));
        infoValueTotalInvoice.setText(AppUtils.formatNominal(invoice.getTotalFinal()));
    }
}