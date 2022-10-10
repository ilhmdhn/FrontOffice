package com.ihp.frontoffice.view.fragment.operasional;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ihp.frontoffice.events.DataBusEvent;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.MemberClient;
import com.ihp.frontoffice.data.remote.respons.MemberResponse;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.QRScanType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalFragment extends Fragment {

    @BindView(R.id.operasional_checkin_act)
    MaterialCardView bttnCheckin;

    @BindView(R.id.operasional_reservasi_act)
    MaterialCardView bttnReservasi;

    @BindView(R.id.operasional_add_info_checkin_act)
    MaterialCardView bttnAddInfoCheckin;

    @BindView(R.id.operasional_fnb_act)
    MaterialCardView bttnFnb;

    @BindView(R.id.operasional_payment_act)
    MaterialCardView bttnPayment;

    @BindView(R.id.operasional_checkout_act)
    MaterialCardView bttnCheckout;

    @BindView(R.id.operasional_clean_act)
    MaterialCardView bttnClean;

    @BindView(R.id.operasional_extend_act)
    MaterialCardView bttnExtend;

    @BindView(R.id.operasional_transfer_act)
    MaterialCardView bttnTransfer;

    @BindView(R.id.operasional_checksound)
    MaterialCardView bttnChecksound;

    @BindView(R.id.dashboard_nama_user)
    TextView textViewNameUser;

    @BindView(R.id.dashboard_user_role)
    TextView textViewRoleUser;

    @BindView(R.id.operasional_progress_bar)
    MKLoader progressBar;

    private static final String TAG = "OperasionalFragment";
    private boolean isCheckinScanActive,
            isReservationScanActive, isLobbyScanActive;


    private MemberClient memberClient;
    private static String BASE_URL;
    private User user;

    public OperasionalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OperasionalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OperasionalFragment newInstance(String param1, String param2) {
        OperasionalFragment fragment = new OperasionalFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                return;
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operasional, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) requireActivity().getApplicationContext()).getBaseUrl();
        user = ((MyApp) requireActivity().getApplicationContext()).getUserFo();

        memberClient = ApiRestService.getClient(BASE_URL).create(MemberClient.class);

        bttnCheckin.setOnClickListener(view -> {
            if (!isCheckinScanActive) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GlobalBus
                                    .getBus()
                                    .post(new EventsWrapper
                                            .XZscan(QRScanType.CHECKIN.getType())
                                    );
                            isCheckinScanActive = true;
                        }
                    }, 500);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(getContext(), "Error Karena " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bttnReservasi.setOnClickListener(view -> {
            if (!isReservationScanActive) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GlobalBus
                                    .getBus()
                                    .post(new EventsWrapper
                                            .XZscan(QRScanType.RESERVASI.getType()));
                            isReservationScanActive = true;
                        }
                    }, 500);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(getContext(), "Error Karena " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bttnAddInfoCheckin.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                            OperasionalFragmentDirections
                                    .actionNavOperasionalFragmentToNavOperasionalListRoomToAddInfoFragment()
                    );
        });

        bttnFnb.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                            OperasionalFragmentDirections
                                    .actionNavOperasionalFragmentToNavOperasionalListRoomToFnbFragment()
                    );
        });

        bttnPayment.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                            OperasionalFragmentDirections
                                    .actionNavOperasionalFragmentToNavOperasionalRoomCheckinFragment());
        });

        bttnCheckout.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                            OperasionalFragmentDirections
                                    .actionNavOperasionalFragmentToOperasionalListRoomToCheckoutFragment()
                    );
        });

        bttnClean.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                            OperasionalFragmentDirections
                                    .actionNavOperasionalFragmentToNavOperasionalListRoomToCleanFragment()
                    );
        });

        bttnExtend.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                            OperasionalFragmentDirections
                                    .actionNavOperasionalFragmentToNavOperasionalListRoomToExtendFragment()
                    );
        });

        bttnTransfer.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                            OperasionalFragmentDirections
                                    .actionNavOperasionalFragmentToNavOperasionalListRoomToTransferFragment()
                    );
        });

        textViewNameUser.setText(user.getUserId());
        textViewRoleUser.setText(user.getLevelUser());
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Operasional"));
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

    @Subscribe
    public void setDataQrCode(EventsWrapper.ScanResult scanResult) {

        if (!scanResult.isSuccess()) {
            isCheckinScanActive = false;
            isReservationScanActive = false;
            return;
        }

        if (isCheckinScanActive) {
            if (scanResult.getData() != null) {
                getMemberCheckin(scanResult.getData());
            }

            isCheckinScanActive = false;
        } else if (isReservationScanActive) {
            if (scanResult.getData() != null) {
                getReservationMemberCheckin(scanResult.getData());
            }
            isReservationScanActive = false;
        } else if (isLobbyScanActive) {
            isLobbyScanActive = false;
        }

    }

    private void getReservationMemberCheckin(String codeReservasiMember) {
        Call<MemberResponse> call = memberClient.checkReservasi(codeReservasiMember);
        call.enqueue(new Callback<MemberResponse>() {
            @Override
            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                progressBar.setVisibility(View.GONE);
                MemberResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    showInfoDialog(res.getMessage(), "Info Reservasi");
                    return;
                }
                Navigation.findNavController(getView())
                        .navigate(OperasionalFragmentDirections
                                .actionNavOperasionalFragmentToNavOperasionalReservasiFragment(res.getMember()));
            }

            @Override
            public void onFailure(Call<MemberResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showInfoDialog(String message, String title) {
        new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }


    private void getMemberCheckin(String codeMbr) {

        Call<MemberResponse> call = memberClient.checkMember(codeMbr);
        call.enqueue(new Callback<MemberResponse>() {
            @Override
            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                progressBar.setVisibility(View.GONE);
                MemberResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    return;
                }
                Navigation.findNavController(getView())
                        .navigate(OperasionalFragmentDirections
                                .actionNavOperasionalFragmentToOperasionalCheckinFragment(res.getMember()));
            }

            @Override
            public void onFailure(Call<MemberResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void approvalResponse(DataBusEvent.approvalResponse data){
        Log.d("approval sampe sini", data.toString());
        if (data.isApprove()){
            Toast.makeText(requireActivity(), data.toString(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(requireActivity(), "Spv Menolak", Toast.LENGTH_SHORT).show();
        }
    }
}