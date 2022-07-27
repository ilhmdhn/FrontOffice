package livs.code.frontoffice.view.fragment.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.local.FrontOfficeDatabase;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.helper.RoomState;
import livs.code.frontoffice.viewmodel.RoomViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    @BindView(R.id.dashboard_foto_user)
    ImageView userFoto;

    @BindView(R.id.dashboard_nama_user)
    TextView userNama;

    @BindView(R.id.dashboard_user_role)
    TextView userRole;

    @BindView(R.id.value_total_all_room)
    TextView totalAllRoom;

    @BindView(R.id.value_total_room_checkin)
    TextView totalRoomCheckin;

    @BindView(R.id.value_total_room_paid)
    TextView totalRoomPaid;

    @BindView(R.id.value_total_room_ready)
    TextView totalRoomReady;

    @BindView(R.id.value_total_room_clean)
    TextView totalRoomClean;

    @BindView(R.id.dashboard_progress_bar)
    MKLoader progressBar;

    private RoomViewModel roomViewModel;
    private FrontOfficeDatabase db = MyApp.frontOfficeDatabase;
    private User user;
    private String EMPTY_STRING = "";
    private List<Room> listAllRoom;

    private ArrayList<Room> filterListRoomCheckin = new ArrayList<>();
    private ArrayList<Room> filterListRoomReady = new ArrayList<>();
    private ArrayList<Room> filterListRoomClean = new ArrayList<>();
    private ArrayList<Room> filterListRoomPaid = new ArrayList<>();
    private static String BASE_URL;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */

    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        setMainTitle();
        user = db.userDao().getLastUser();

        userNama.setText(user.getUserId());
        userRole.setText(user.getLevelUser());

        roomViewModel = new ViewModelProvider(getActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);

        allRoomSetupData();

    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Dashboard"));
    }

    private void allRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel
                .getAllRoom(EMPTY_STRING)
                .observe(getViewLifecycleOwner(), roomResponse -> {
                    roomResponse.displayMessage(getContext());
                    if (roomResponse.isOkay()) {
                        listAllRoom = roomResponse.getRooms();
                        setDataCountRoom();
                    }
                    progressBar.setVisibility(View.GONE);

                });
    }

    private void setDataCountRoom() {
        filterListRoomReady = (ArrayList<Room>) listAllRoom.stream()
                .filter(room -> room.getRoomState() == RoomState.READY.getState())
                .collect(Collectors.toList());
        filterListRoomCheckin = (ArrayList<Room>) listAllRoom.stream()
                .filter(room -> room.getRoomState() == RoomState.CHECKIN.getState())
                .collect(Collectors.toList());
        filterListRoomPaid = (ArrayList<Room>) listAllRoom.stream()
                .filter(room -> room.getRoomState() == RoomState.PAID.getState())
                .collect(Collectors.toList());
        filterListRoomClean = (ArrayList<Room>) listAllRoom.stream()
                .filter(room -> room.getRoomState() == RoomState.CHECKOUT_REPAIRED.getState())
                .collect(Collectors.toList());

        

        int countAll = listAllRoom.size();
        int countReady = filterListRoomReady.size();
        int countCheckin = filterListRoomCheckin.size();
        int countPaid = filterListRoomPaid.size();
        int countClean = filterListRoomClean.size();

        totalAllRoom.setText(String.valueOf(countAll));
        totalRoomReady.setText(String.valueOf(countReady));
        totalRoomCheckin.setText(String.valueOf(countCheckin));
        totalRoomPaid.setText(String.valueOf(countPaid));
        totalRoomClean.setText(String.valueOf(countClean));

        progressBar.setVisibility(View.GONE);
    }

    @Subscribe
    public void refreshPage(EventsWrapper.RefreshCurrentFragment refreshCurrentFragment) {
        allRoomSetupData();
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

}