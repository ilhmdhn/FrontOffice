package livs.code.frontoffice.view.fragment.reporting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.R;
import livs.code.frontoffice.databinding.FragmentReportingBinding;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportingFragment extends Fragment {

    @BindView(R.id.btn_status_kas_masuk)
    MaterialCardView btnKasMasuk;

    @BindView(R.id.btn_my_sales)
    MaterialCardView btnMySales;

    @BindView(R.id.btn_my_sale_item)
    MaterialCardView btnSalesItem;

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
        setMainTitle();
        btnKasMasuk.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                          ReportingFragmentDirections
                          .actionNavReportingFragmentToStatusKasFragment()
                    );
        });
        btnMySales.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(
                          ReportingFragmentDirections
                          .actionNavReportingFragmentToMySalesReportParentFragment()
                    );
        });
        btnSalesItem.setOnClickListener(view->{
            Navigation.findNavController(view)
                    .navigate(
                            ReportingFragmentDirections
                            .actionNavReportingFragmentToItemSalesFragment()
                    );
        });
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
