package livs.code.frontoffice.view.fragment.operasional.clean;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.RoomOrderClient;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.view.component.BasePagination;
import livs.code.frontoffice.view.listadapter.ListOperasionalCleanRoomAdapter;
import livs.code.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OperasionalListRoomToCleanFragment extends Fragment {

    @BindView(R.id.room_list_recyclerview)
    RecyclerView roomRecyclerView;

    @BindView(R.id.room_progressbar)
    MKLoader progressBar;

    @BindView(R.id.search_room_by_code)
    SearchView searchView;

    @BindView(R.id.bttn_previous)
    ImageButton buttonPrevious;

    @BindView(R.id.bttn_next)
    ImageButton buttonNext;

    @BindView(R.id.text_checkout_room_clean)
    TextView textCheckoutRoomClean;

    //pagination
    private BasePagination p;
    private int totalPages=0;
    private int currentPage = 0;

    private RoomViewModel roomViewModel;
    private String cariData = "";
    private ListOperasionalCleanRoomAdapter roomAdapter;
    private ArrayList<Room> roomArrayList = new ArrayList<>();

    //checkout
    RoomOrderClient roomOrderClient;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater dialogInflater;
    private View dialogView;
    private TextInputLayout inputNameVisitor,
            inputPhoneVisitor,
            inputRoom,
            inputCheckoutTime,
            inputCleanRoomCode;
    private static String BASE_URL;
    private User USER_FO;

    public OperasionalListRoomToCleanFragment() {
        // Required empty public constructor
    }


    public static OperasionalListRoomToCleanFragment newInstance() {
        OperasionalListRoomToCleanFragment fragment = new OperasionalListRoomToCleanFragment();
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
        View view = inflater.inflate(R.layout.fragment_operasional_list_room_to_clean, container, false);
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
        searchView.setQueryHint("Kode Room");
        textCheckoutRoomClean.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                //progressBar.setVisibility(View.VISIBLE);

                cariData = keyword;
                if (keyword.length() <= 1) {
                    cariData = "";
                }
                checkoutRoomSetupData();


                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cariData = "";
                checkoutRoomSetupData();
                return false;
            }
        });

        //checkinRoomSetupData();
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

        textCheckoutRoomClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(OperasionalListRoomToCleanFragmentDirections
                                .actionNavOperasionalListRoomToCleanFragmentToOperasionalListRoomToCheckoutFragment()
                        );
            }
        });

    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Clean Room"));
    }

    private void checkoutRoomSetupData() {
        if(roomAdapter.getItemCount()>0){
            roomAdapter.clearItem();
        }
        progressBar.setVisibility(View.VISIBLE);
        roomArrayList.clear();
        roomViewModel.getRoomCheckout(cariData).observe(getActivity(), roomResponse -> {
            roomResponse.displayMessage(getContext());
            progressBar.setVisibility(View.GONE);
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                bindData(0);
            }
            toggleButtons();
        });
    }

    private void bindData(int page) {
        p = new BasePagination(roomArrayList);
        p.setItemsPerPage(9);
        totalPages = p.getTotalPages();

        roomAdapter = new ListOperasionalCleanRoomAdapter(getContext(), p.getCurrentData(page));
        roomRecyclerView.setAdapter(roomAdapter);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        roomAdapter.notifyDataSetChanged();

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

    @Override
    public void onResume() {
        super.onResume();
        roomAdapter = new ListOperasionalCleanRoomAdapter(getContext(), null);
        roomRecyclerView.setAdapter(roomAdapter);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        checkoutRoomSetupData();
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
        checkoutRoomSetupData();
    }

    @Subscribe
    public void operasionalCheckinRoom(EventsWrapper.OperasionalBusCheckinRoom operasionalBusCheckinRoom) {
        Room room = operasionalBusCheckinRoom.getRoom();
        cleanRoomDialog(room);
    }

    private void cleanRoomDialog(Room room) {
        progressBar.setVisibility(View.GONE);
        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogInflater = this.getLayoutInflater();
        dialogView = dialogInflater.inflate(R.layout.dialog_clean_room, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle("Room Clean?");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);

        inputCleanRoomCode = dialogView.findViewById(R.id.clean_room_code);
        inputCleanRoomCode.getEditText().setText(room.getRoomCode());
        inputCleanRoomCode.setEnabled(false);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        Call<RoomOrderResponse> req = roomOrderClient
                                .cleanRoom(room.getRoomCode(), USER_FO.getUserId());

                        req.enqueue(new Callback<RoomOrderResponse>() {
                            @Override
                            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {

                                progressBar.setVisibility(View.GONE);
                                RoomOrderResponse res = response.body();
                                res.displayMessage(getContext());
                             /*   if (res.isOkay()) {

                                }*/
                                checkoutRoomSetupData();

                                alertDialog.dismiss();
                                //submitImageSign(res.getRoomOrder().getKodeRcp());
                            }

                            @Override
                            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });

        alertDialog.show();
    }


}