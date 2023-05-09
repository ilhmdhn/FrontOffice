package com.ihp.frontoffice.view.fragment.reporting;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.UserAuthRole;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportingFragment extends Fragment {

    @BindView(R.id.btn_status_kas_masuk)
    MaterialCardView btnKasMasuk;

    @BindView(R.id.ll_kas)
    LinearLayout llKas;

    @BindView(R.id.btn_my_sales)
    MaterialCardView btnMySales;


    @BindView(R.id.btn_report_cancel_order)
    MaterialCardView btnCancelOrder;

    @BindView(R.id.ll_rph)
    LinearLayout llRph;

    @BindView(R.id.btn_report_rph)
    MaterialCardView btnReportRPH;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ReportingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportingFragment newInstance(String param1, String param2) {
        ReportingFragment fragment = new ReportingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reporting, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        User user = ((MyApp) requireActivity().getApplicationContext()).getUserFo();
        if(UserAuthRole.isAllowSendRPH(user)){
            llRph.setVisibility(View.VISIBLE);
        }else{
            llRph.setVisibility(View.GONE);
        }

        setMainTitle();

        if(!UserAuthRole.isAllowViewKasReport(user)){
             llKas.setVisibility(View.GONE);
        }


        btnKasMasuk.setOnClickListener(view -> {
            if(UserAuthRole.isAllowViewKasReport(user)){
                Navigation.findNavController(view)
                        .navigate(
                                ReportingFragmentDirections
                                        .actionNavReportingFragmentToStatusKasFragment()
                        );
            }else{
                ShowDialogCantAccess();
            }
        });
        btnMySales.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                          ReportingFragmentDirections
                          .actionNavReportingFragmentToMySalesReportParentFragment()
                    );
        });

        btnCancelOrder.setOnClickListener(view ->{
            Navigation.findNavController(view)
                    .navigate(
                            ReportingFragmentDirections
                            .actionNavReportingFragmentToCancelReportFragment()
                    );
        });
    }

    void ShowDialogCantAccess(){
        MaterialAlertDialogBuilder dialog;
        dialog = new MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogDarkTheme);
        dialog.setTitle("Tidak memiliki akses");
        dialog.setMessage("User anda tidak memiliki akses ini")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        dialog.show();
    }
    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Report"));
    }

    @Override
    public void onStart() {
        super.onStart();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
