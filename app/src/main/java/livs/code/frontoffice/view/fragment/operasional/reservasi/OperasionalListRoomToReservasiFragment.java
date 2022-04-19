package livs.code.frontoffice.view.fragment.operasional.reservasi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Member;
import livs.code.frontoffice.data.entity.Payment;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.entity.RoomType;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.RoomOrderClient;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.helper.AppUtils;
import livs.code.frontoffice.view.component.BasePagination;
import livs.code.frontoffice.view.listadapter.ListOperasionalReadyRoomAdapter;
import livs.code.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperasionalListRoomToReservasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperasionalListRoomToReservasiFragment extends Fragment {

    @BindView(R.id.image_member)
    ImageView memberFoto;

    @BindView(R.id.name_member)
    TextView memberName;

    @BindView(R.id.phone_member)
    TextView memberPhone;

    @BindView(R.id.checkin_poin_member)
    TextView memberPoin;

    @BindView(R.id.reservasi_durasi_room_type)
    TextView infoReservasiDurasi;

    @BindView(R.id.reservasi_time_checkin)
    TextView infoReservasiTimeCheckin;


    @BindView(R.id.reservasi_type_room)
    TextView infoReservasiTypeRoom;

    @BindView(R.id.reservasi_payment)
    TextView infoReservasiPayment;

    @BindView(R.id.ready_room_recyclerview)
    RecyclerView readyRoomRecyclerView;

    @BindView(R.id.bttn_previous)
    ImageButton buttonPrevious;

    @BindView(R.id.bttn_next)
    ImageButton buttonNext;


    @BindView(R.id.progressbar)
    MKLoader progressBar;


    private RoomViewModel roomViewModel;
    private Member reservasiMember;
    private ListOperasionalReadyRoomAdapter roomAdapter;
    private ArrayList<Room> roomArrayList = new ArrayList<>();

    private TextView typeRoom, countHours;
    private ImageView increment, decrement;
    private AppCompatButton buttonChooseRoom, buttonCheckin, buttonCancel;

    private int jamCheckin,menitCheckin;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater dialogInflater;
    private View dialogView;
    private RoomOrderClient roomOrderClient;
    private String TAG = "OperasionalListRoomTypeToCheckinFragment";
    private static String BASE_URL;
    private User USER_FO;

    //pagination
    private BasePagination p;
    private int totalPages;
    private int currentPage = 0;
    private RoomType reservasiRoomType;
    private Payment reservasiPayment;



    public OperasionalListRoomToReservasiFragment() {
    }
    // Required empty public constructor

    public static OperasionalListRoomToReservasiFragment newInstance() {
        OperasionalListRoomToReservasiFragment fragment = new OperasionalListRoomToReservasiFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reservasiMember = OperasionalListRoomToReservasiFragmentArgs
                .fromBundle(getArguments()).getMember();

        reservasiRoomType = reservasiMember.getReservasiRoomType();
        reservasiPayment = reservasiMember.getReservasiPayment();
        jamCheckin = reservasiRoomType.getReservasiHourDuration();
        menitCheckin = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_operasional_reservasi, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();


        roomViewModel = new ViewModelProvider(getActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage += 1;
                bindData(currentPage);
                toggleButtons();
            }
        });
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage -= 1;
                bindData(currentPage);
                toggleButtons();
            }
        });

        setDataMember();
        readyRoomSetupData();

    }



    @Override
    public void onResume() {
        super.onResume();
        setDataMember();
        readyRoomSetupData();
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Reservasi"));
    }

    private void readyRoomSetupData() {
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel.getRoomReadyByType(reservasiRoomType).observe(getActivity(), roomResponse -> {
            progressBar.setVisibility(View.GONE);
            roomResponse.displayMessage(getContext());
            if (roomResponse.isOkay()) {
                roomArrayList.clear();
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                bindData(currentPage);
            }
        });
    }

    private void bindData(int page) {
        p = new BasePagination(roomArrayList);
        p.setItemsPerPage(9);
        totalPages = p.getTotalPages();
        roomAdapter = new ListOperasionalReadyRoomAdapter(getContext(), p.getCurrentData(page));
        readyRoomRecyclerView.setAdapter(roomAdapter);
        readyRoomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        roomAdapter.notifyDataSetChanged();
        toggleButtons();
    }

    private void toggleButtons() {
        //SINGLE PAGE DATA
        Log.i("PAGING", "totalPages = " + totalPages);
        Log.i("PAGING", "currentPage = " + currentPage);
        if (totalPages <= 1) {
           /* buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(false);*/
            buttonNext.setVisibility(View.GONE);
            buttonPrevious.setVisibility(View.GONE);
        }
        //LAST PAGE
        else if ((totalPages - currentPage) == 1) {
            buttonNext.setEnabled(false);
            buttonPrevious.setEnabled(true);
        }
        //FIRST PAGE
        else if (currentPage == 0) {
            buttonPrevious.setEnabled(false);
            buttonNext.setEnabled(true);
        }
        //SOMEWHERE IN BETWEEN
        else if (currentPage >= 1 && (totalPages - currentPage) == 1) {
            buttonNext.setEnabled(true);
            buttonPrevious.setEnabled(true);
        }

    }

    private void setDataMember() {
        Glide.with(getContext())
                .load(reservasiMember.getFotoPathNode())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.user)
                .skipMemoryCache(true)
                .into(memberFoto);
        memberName.setText(reservasiMember.getFullName());
        memberPhone.setText(reservasiMember.getHp());
        memberPoin.setText(" Poin " + String.valueOf(reservasiMember.getPointReward()));
        infoReservasiDurasi
                .setText(reservasiRoomType.getRoomType() + " / "
                        + reservasiRoomType.getReservasiHourDuration() + " Jam");
        infoReservasiPayment
                .setText("Pembayaran Reservasi " + AppUtils.formatNominal(reservasiPayment.getNominal()));
        infoReservasiTypeRoom
                .setText("Reservasi untuk type " + reservasiRoomType.getRoomType());
        infoReservasiTimeCheckin
                .setText(reservasiRoomType.getReservasiCheckinTime()+" -> "+reservasiRoomType.getReservasiCheckoutTime());
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
    public void operasionalCheckinRoom(EventsWrapper.OperasionalBusCheckinRoom operasionalBusCheckinRoom) {

        RoomOrder roomOrder = new RoomOrder();
        roomOrder.setMember(reservasiMember);
        roomOrder.setCheckinRoomType(reservasiRoomType);
        roomOrder.setChuser(USER_FO.getUserId());
        roomOrder.setDurasiJam(jamCheckin);
        Room room = operasionalBusCheckinRoom.getRoom();

        new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme)
                .setTitle(reservasiRoomType.getRoomType() + " " + reservasiRoomType.getRoomTypeCapacity())
                .setMessage("Kode room " + room.getRoomCode() + ", Durasi " + jamCheckin + " Jam")
                .setPositiveButton("Checkin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        roomOrder.setCheckinRoom(room);
                        submitCheckin(roomOrder);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    private void submitCheckin(RoomOrder roomOrder) {

        String keteranganUangMuka = reservasiPayment.getPaymentType();
        String input1JenisKartu = reservasiPayment.getBankNameTf();
        String input2Nama = reservasiPayment.getBankAccountNameTf();
        String input3NomorKartu = "";
        String input4NomorApproval = "";
        String edcMachine = "0";

        List<String> promoSelected = new ArrayList<>();


        progressBar.setVisibility(View.VISIBLE);
        Call<RoomOrderResponse> req = roomOrderClient
                .submitOrderRoom(
                        roomOrder.getCheckinRoom().getRoomCode(),
                        reservasiMember.getFullName(),
                        reservasiMember.getMemberCode(),
                        String.valueOf(jamCheckin),
                        String.valueOf(menitCheckin),
                        String.valueOf(0),
                        String.valueOf(0),
                        String.valueOf(0),
                        String.valueOf(0),
                        String.valueOf(0),
                        String.valueOf(0),
                        String.valueOf(0),
                        String.valueOf(0),
                        reservasiMember.getHp(),
                        String.valueOf(reservasiPayment.getNominal()),
                        "",
                        "",
                        USER_FO.getUserId(),
                        "",
                        keteranganUangMuka,
                        input1JenisKartu,
                        input2Nama,
                        input3NomorKartu,
                        input4NomorApproval,
                        edcMachine,
                        promoSelected,
                        reservasiMember.getMemberReservasiCode()
                );


        req.enqueue(new Callback<RoomOrderResponse>() {
            @Override
            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                progressBar.setVisibility(View.GONE);
                RoomOrderResponse res = response.body();
                if (!res.isOkay()) {
                    res.displayMessage(getContext());
                    return;
                }
                navToMain(roomOrder.getCheckinRoom());

            }

            @Override
            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Subscribe
    public void refreshPage(EventsWrapper.RefreshCurrentFragment refreshCurrentFragment) {
        readyRoomSetupData();
    }

    private void navToMain(Room roomOrder) {
           Navigation
                .findNavController(getView())
                   .navigate(OperasionalListRoomToReservasiFragmentDirections
                   .actionNavOperasionalReservasiFragmentToNavOperasionalReservasiAddInfoFragment(roomOrder,reservasiPayment));
    }

}