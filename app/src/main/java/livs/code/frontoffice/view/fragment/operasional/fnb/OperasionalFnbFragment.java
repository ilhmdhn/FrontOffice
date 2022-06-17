package livs.code.frontoffice.view.fragment.operasional.fnb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Inventory;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.InventoryOrderClient;
import livs.code.frontoffice.data.remote.UserClient;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.data.remote.respons.UserResponse;
import livs.code.frontoffice.data.repository.IhpRepository;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.helper.UserAuthRole;
import livs.code.frontoffice.view.fragment.ruangan.RoomOrderDetailFragmentArgs;
import livs.code.frontoffice.viewmodel.RoomOrderViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalFnbFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalFnbFragment extends Fragment {

    @BindView(R.id.room_inventory_detail_progressbar)
    MKLoader progressBar;

    @BindView(R.id.room_inventory_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.room_inventory_view_pager)
    ViewPager2 viewPager2;

    private String[] titles = new String[]{"Confirm", "Done", "Cancel"};

    private Room room;
    private RoomOrder roomOrder;
    private RoomOrderViewModel roomOrderViewModel;
    private InventoryOrderClient inventoryOrderClient;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater dialogInflater;
    private View dialogView;
    private TextInputLayout inventoryName, inventoryCode, inventoryQty;
    private TextView countCancel, countConfirm;
    private int doQty, cancelQty;
    private IhpRepository ihpRepository;

    private ImageView buttonPlusCancel, buttonMinusCancel;
    private ImageView buttonPlusConfirm, buttonMinusConfirm;

    private static String BASE_URL;
    private User USER_FO;

    public OperasionalFnbFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OperasionalFnbFragment.
     */

    public static OperasionalFnbFragment newInstance() {
        OperasionalFnbFragment fragment = new OperasionalFnbFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        room = RoomOrderDetailFragmentArgs.fromBundle(getArguments()).getRoom();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_operasional_fnb, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();
        inventoryOrderClient = ApiRestService.getClient(BASE_URL).create(InventoryOrderClient.class);
        roomOrderViewModel = new ViewModelProvider(getActivity())
                .get(RoomOrderViewModel.class);
        roomOrderViewModel.init(BASE_URL);
        roomOrderSetupData();
        ihpRepository = new IhpRepository();
    }

    private void roomOrderSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        roomOrder = null;
        roomOrderViewModel
                .getRoomOrderResponseMutableLiveData(room.getRoomCode())
                .observe(getActivity(), roomOrderResponse -> {
                    roomOrderResponse.displayMessage(getContext());
                    if (roomOrderResponse.isOkay()) {
                        roomOrder = roomOrderResponse.getRoomOrder();
                        setViewPager();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment(room.getRoomType() + " " + room.getRoomCode()));
    }


    private void setViewPager() {
        viewPager2.setAdapter(new OperasionalFnbFragment.ViewPagerFragmentAdapter(getActivity()));
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(titles[position]))
                .attach();
    }

    private void deliveryOrderInventoryDialog(Inventory inventory) {
        progressBar.setVisibility(View.GONE);
        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogInflater = this.getLayoutInflater();
        dialogView = dialogInflater.inflate(R.layout.dialog_delivery_order_inventory, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("DO Pesanan ?");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);

        inventoryName = dialogView.findViewById(R.id.inventory_name_dialog);
        inventoryCode = dialogView.findViewById(R.id.inventory_code_dialog);
        inventoryQty = dialogView.findViewById(R.id.inventory_qty_dialog);

        inventoryName.setEnabled(false);
        inventoryCode.setEnabled(false);
        inventoryQty.setEnabled(false);

        buttonPlusConfirm = dialogView.findViewById(R.id.inventory_plus_deliveryorder);
        countConfirm = dialogView.findViewById(R.id.inventory_count_deliveryorder);
        buttonMinusConfirm = dialogView.findViewById(R.id.inventory_min_deliveryorder);


        int qty = inventory.getQty();

        inventoryName.getEditText().setText(inventory.getInventoryName());
        inventoryCode.getEditText().setText(inventory.getInventoryCode() + " | " + inventory.getSlipOrder());
        inventoryQty.getEditText().setText(String.valueOf(qty));

        //*Disable edit qty DO*/
        doQty = qty;
        countConfirm.setText(String.valueOf(doQty));
        countConfirm.setVisibility(View.INVISIBLE);
        buttonPlusConfirm.setVisibility(View.INVISIBLE);
        buttonMinusConfirm.setVisibility(View.INVISIBLE);
        buttonMinusConfirm.setOnClickListener(view -> {
            if (doQty == 0) {
                countConfirm.setText(String.valueOf(doQty));
            }
            if (doQty > 0) {
                doQty = doQty - 1;
                countConfirm.setText(String.valueOf(doQty));
            }
        });
        buttonPlusConfirm.setOnClickListener(view -> {
            if (doQty == 0) {
                countConfirm.setText(String.valueOf(doQty));
            }
            if (doQty < qty) {
                doQty = doQty + 1;
                countConfirm.setText(String.valueOf(doQty));
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
                        if (doQty == 0) {
                            Toast
                                    .makeText(getContext(), "Belum ada perubahan data", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        visibleProgressBar(true);
                        button.setEnabled(false);

                        Inventory doInventory = inventory;
                        doInventory.setQty(doQty);

                        List<Inventory> listDoInventory = new ArrayList<>();
                        listDoInventory.add(doInventory);

                        RoomOrder roomOrder = new RoomOrder();
                        roomOrder.setRoomCode(room.getRoomCode());
                        roomOrder.setKodeRcp(room.getRoomRcp());
                        roomOrder.setChuser(USER_FO.getUserId());
                        roomOrder.setInventories(listDoInventory);

                        Call<RoomOrderResponse> call = inventoryOrderClient.submitDeliveryOrderInventory(roomOrder);
                        call.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                                visibleProgressBar(false);
                                button.setEnabled(true);
                                RoomOrderResponse res = response.body();
                                res.displayMessage(getContext());
                                if (!res.isOkay()) {
                                    return;
                                }
                                roomOrderSetupData();
                                dialogInterface.dismiss();
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                                visibleProgressBar(false);
                                button.setEnabled(true);
                            }
                        });
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void cancelInventoryOrderDialog(Inventory inventory) {
        progressBar.setVisibility(View.GONE);
        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogInflater = this.getLayoutInflater();
        dialogView = dialogInflater.inflate(R.layout.dialog_cancel_order_inventory, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("Cancel Pesanan ?");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);

        inventoryName = dialogView.findViewById(R.id.inventory_name_dialog);
        inventoryCode = dialogView.findViewById(R.id.inventory_code_dialog);
        inventoryQty = dialogView.findViewById(R.id.inventory_qty_dialog);
        EditText _usernameTxt = dialogView.findViewById(R.id.input_username_otorisasi);
        EditText _passwordTxt = dialogView.findViewById(R.id.input_password_otorisasi);

        inventoryName.setEnabled(false);
        inventoryCode.setEnabled(false);
        inventoryQty.setEnabled(false);
        int qty = inventory.getQtyBeforeOcd();

        inventoryName.getEditText().setText(inventory.getInventoryName());
        inventoryCode.getEditText().setText(inventory.getInventoryCode() + " | " + inventory.getSlipOrder());
        inventoryQty.getEditText().setText(String.valueOf(qty));


        buttonPlusCancel = dialogView.findViewById(R.id.inventory_plus_cancel);
        countCancel = dialogView.findViewById(R.id.inventory_count_cancel);
        buttonMinusCancel = dialogView.findViewById(R.id.inventory_min_cancel);

        cancelQty = 0;
        buttonMinusCancel.setOnClickListener(view -> {
            if (cancelQty == 0) {
                countCancel.setText(String.valueOf(cancelQty));
            }

            if (cancelQty > 0) {
                cancelQty = cancelQty - 1;
                countCancel.setText(String.valueOf(cancelQty));
            }

        });
        buttonPlusCancel.setOnClickListener(view -> {
            if (cancelQty == 0) {
                countCancel.setText(String.valueOf(cancelQty));
            }

            if (cancelQty < qty) {
                cancelQty = cancelQty + 1;
                countCancel.setText(String.valueOf(cancelQty));
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

                        String email = _usernameTxt.getText().toString();
                        String password = _passwordTxt.getText().toString();
                        if (email.isEmpty() && password.isEmpty()) {
                            Toasty.warning(getContext(), "Anda belum input user dan password ", Toast.LENGTH_SHORT, true)
                                    .show();
                            return;
                        }
                        if (cancelQty == 0) {
                            Toasty.warning(getContext(), "Belum ada perubahan data", Toast.LENGTH_SHORT, true)
                                    .show();
                            return;
                        }

                        visibleProgressBar(true);
                        button.setEnabled(false);

                        Inventory cancelInventory = inventory;
                        cancelInventory.setQty(cancelQty);

                        List<Inventory> listDoInventory = new ArrayList<>();
                        listDoInventory.add(cancelInventory);

                        RoomOrder roomOrder = new RoomOrder();
                        roomOrder.setRoomCode(room.getRoomCode());
                        roomOrder.setKodeRcp(room.getRoomRcp());
                        roomOrder.setChuser(USER_FO.getUserId());
                        roomOrder.setInventories(listDoInventory);

                        UserClient userClient = ApiRestService.getClient(BASE_URL).create(UserClient.class);
                        Call<UserResponse> callOtorisasi = userClient.login(email, password);
                        callOtorisasi.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call1, Response<UserResponse> response) {
                                UserResponse res = response.body();
                                button.setEnabled(true);
                                res.displayMessage(getContext());
                                if (res.isOkay()) {
                                    User user = res.getUser();
                                    if (UserAuthRole.isAllowCancelOrder(user)) {
                                        String kodeRoom = room.getRoomCode();
                                        if (kodeRoom  == null){
                                            kodeRoom = "";
                                        }
                                        ihpRepository.submitApproval(BASE_URL, user.getUserId(), user.getLevelUser(), kodeRoom, "Cancel Order");
                                        Call<RoomOrderResponse> callCancelOrder = inventoryOrderClient.submitCancelOrderInventory(roomOrder);
                                        callCancelOrder.enqueue(new Callback<RoomOrderResponse>() {
                                            @Override
                                            public void onResponse(Call<RoomOrderResponse> call2, Response<RoomOrderResponse> response) {
                                                visibleProgressBar(false);
                                                RoomOrderResponse res = response.body();
                                                res.displayMessage(getContext());
                                                if (!res.isOkay()) {
                                                    return;
                                                }
                                                roomOrderSetupData();
                                                dialogInterface.dismiss();
                                            }

                                            @Override
                                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                                Toasty.error(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT)
                                                        .show();
                                                visibleProgressBar(false);
                                            }
                                        });
                                    } else {
                                        Toasty.warning(getContext(), "User tidak dapat melakukan operasi ini", Toast.LENGTH_SHORT, true)
                                                .show();
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                button.setEnabled(true);
                                Toasty.error(getContext(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT, true)
                                        .show();
                            }
                        });
                    }
                });
            }
        });

        alertDialog.show();
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

    //TODO :: doOrderDialog
    @Subscribe
    public void deliverOrder(EventsWrapper.DeliveryOrderInventory inventory) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                deliveryOrderInventoryDialog(inventory.getInventory());
            }
        }, 1 * 1000);
    }

    //TODO :: cancelOrderDialog
    @Subscribe
    public void cancelInventory(EventsWrapper.CancelOrderInventory inventory) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cancelInventoryOrderDialog(inventory.getInventory());
            }
        }, 1 * 1000);
    }

    @Subscribe
    public void refreshPage(EventsWrapper.RefreshCurrentFragment refreshCurrentFragment) {
        roomOrderSetupData();
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

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return FnbConfirmFragment.newInstance(roomOrder);
                case 1:
                    return FnbProgressFragment.newInstance(roomOrder);
                case 2:
                    return FnbCancelFragment.newInstance(roomOrder);

            }
            return new Fragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
}