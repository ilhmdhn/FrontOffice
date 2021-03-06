package livs.code.frontoffice.view.fragment.operasional.invoicepayment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Invoice;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.entity.Time;
import livs.code.frontoffice.data.entity.TypeEdc;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.PaymentOrderClient;
import livs.code.frontoffice.data.remote.MemberClient;
import livs.code.frontoffice.data.remote.respons.BaseResponse;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.view.listadapter.ListEdcTypeAdapter;
import livs.code.frontoffice.viewmodel.RoomOrderViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalInvoiceAndPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalInvoiceAndPaymentFragment extends Fragment {


    @BindView(R.id.progressbar)
    MKLoader progressBar;
    @BindView(R.id.invoice_payment_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.invoice_payment_view_pager)
    ViewPager2 viewPager2;

    // TODO : Rename and change types of parameters

    private MemberClient memberClient;
    private PaymentOrderClient paymentOrderClient;

    private ArrayList<TypeEdc> typesListEdc = new ArrayList<>();
    private ListEdcTypeAdapter listEdcTypeAdapter;
    private String[] banks;
    private ArrayAdapter<String> adapterBanks;

    private Room room;
    private Invoice invoice;
    private Time timeRcp;


    private RoomOrderViewModel roomOrderViewModel;

    private static String BASE_URL;
    private User USER_FO;
    private String[] titles = new String[]{"Tagihan", "Pembayaran"};
    private RoomOrder roomOrder;


    public OperasionalInvoiceAndPaymentFragment() {
        // Required empty public constructor
    }


    public static OperasionalInvoiceAndPaymentFragment newInstance() {
        OperasionalInvoiceAndPaymentFragment fragment = new OperasionalInvoiceAndPaymentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        room = OperasionalInvoiceAndPaymentFragmentArgs.fromBundle(getArguments()).getRoom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_operasional_invoice_and_payment, container, false);
        ButterKnife.bind(this, view);

        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();
        memberClient = ApiRestService.getClient(BASE_URL).create(MemberClient.class);
        paymentOrderClient = ApiRestService.getClient(BASE_URL).create(PaymentOrderClient.class);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        roomOrderViewModel = new ViewModelProvider(getActivity())
                .get(RoomOrderViewModel.class);
        roomOrderViewModel.init(BASE_URL);
        roomOrderSetupData();
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment(room.getRoomCode()));
    }

    private void activeTouch(boolean set) {
        if (set) {
            getActivity()
                    .getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }
        getActivity()
                .getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void roomOrderSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        activeTouch(false);
        invoice = null;
        roomOrderViewModel
                .getRoomOrderResponseMutableLiveData(room.getRoomCode())
                .observe(getActivity(), roomOrderResponse -> {
                    roomOrderResponse.displayMessage(getContext());
                    if (roomOrderResponse.isOkay()) {
                        roomOrder = roomOrderResponse.getRoomOrder();
                        invoice = roomOrder.getInvoice();
                        timeRcp = roomOrder.getTime();
                        setViewPager();
                    }
                    progressBar.setVisibility(View.GONE);
                    activeTouch(true);
                });
    }

    private void setViewPager() {
        viewPager2.setAdapter(new ViewPagerFragmentAdapter(getActivity()));
        viewPager2.setUserInputEnabled(false);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(titles[position]))
                .attach();


        progressBar.setVisibility(View.VISIBLE);
        Call<BaseResponse> call = paymentOrderClient.infoPendingOrder(roomOrder.getRoomCode());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().isOkay()) {
                    showInfoDialog(response.body().getMessage(), "Info Order");
                    return;
                } else {
                    //Toasty.warning(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toasty.error(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showInfoDialog(String message, String title) {
        new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
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
        roomOrderSetupData();
    }

    @Subscribe
    public void viewPaymentFragment(EventsWrapper.NavigationInvoicePayment navigationInvoicePayment) {
        progressBar.setVisibility(View.VISIBLE);
        Call<BaseResponse> call = paymentOrderClient.infoPendingOrder(roomOrder.getRoomCode());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().isOkay()) {
                    showInfoDialog(response.body().getMessage(), "Info Order");
                    return;
                } else {
                    if (navigationInvoicePayment.isToPayment()) {
                        viewPager2.setCurrentItem(2);
                    } else {
                        viewPager2.setCurrentItem(1);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toasty.error(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Subscribe
    public void viewInvoiceFragment(EventsWrapper.NavigationPaymentInvoice navigationPaymentInvoice) {
        roomOrderSetupData();
    }

    @Subscribe
    public void printBillInvoice(EventsWrapper.PrintBillInvoice printBillInvoice) {
        new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme)
                .setTitle("Print Tagihan")
                .setMessage("Anda ingin melanjutkan")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        Call<BaseResponse> call = paymentOrderClient.printBill(printBillInvoice.getCode());
                        call.enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                progressBar.setVisibility(View.GONE);
                                if (response.body().isOkay()) {
                                    Toasty.info(getContext(), "Print OK", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    Toasty.warning(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Toasty.error(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
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

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return InvoiceFragment.newInstance(invoice, roomOrder, timeRcp);
                case 1:
                    return PaymentFragment.newInstance(invoice, room);

            }
            return new Fragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }


}