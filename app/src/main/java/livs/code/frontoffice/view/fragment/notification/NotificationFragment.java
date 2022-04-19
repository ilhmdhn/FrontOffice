package livs.code.frontoffice.view.fragment.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Notification;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.RoomOrderClient;
import livs.code.frontoffice.data.remote.respons.RoomResponse;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.view.listadapter.ListNotificationAdapter;
import livs.code.frontoffice.viewmodel.NotificationViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment
implements ListNotificationAdapter.OnDetailPageListener{
    @BindView(R.id.notify_recyclerview)
    RecyclerView notifyRecyclerView;

    @BindView(R.id.notify_progressbar)
    MKLoader progressBar;

    @BindView(R.id.search_notify_by_code)
    SearchView searchView;

    private ListNotificationAdapter listNotificationAdapter;
    private NotificationViewModel notificationViewModel;
    private RoomOrderClient roomOrderClient;
    private static String BASE_URL;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NotificationFragment.
     */

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
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

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
/*        TransitionInflater inflateAnim = TransitionInflater.from(requireContext());
        setEnterTransition(inflateAnim.inflateTransition(R.transition.fade));*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMainTitle();
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        notificationViewModel = new ViewModelProvider(getActivity())
                .get(NotificationViewModel.class);
        notificationViewModel.init(getActivity().getApplicationContext());

        listNotificationAdapter = new ListNotificationAdapter(getContext(),this);
        notifyRecyclerView.setAdapter(listNotificationAdapter);
        notifyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);
        observableNotifyData();
        setMainNotify();
    }

    void observableNotifyData(){
        notificationViewModel
                .getNotificationMutableLiveData()
                .observe(getActivity(), new Observer<List<Notification>>() {
                    @Override
                    public void onChanged(List<Notification> notifications) {
                        listNotificationAdapter.setNotificationList(notifications);
                    }
                });

    }

    private void setMainNotify(){
        notificationViewModel.resetNotify();
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .ResetNotifikasi(true));
    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment("Notifikasi"));
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

    }

    @Override
    public void onDetailPageOrder(Notification notification) {
        progressBar.setVisibility(View.VISIBLE);
        Call<RoomResponse> call = roomOrderClient.getRoomDetail(notification.getRoomCode());
        call.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {

                RoomResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                goToDetailOrder(res.getRoom());

            }

            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
                Toast.makeText(getContext(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

       /* Navigation
                .findNavController(view)
                .navigate(ListInventoryFragmentDirections.actionNavListInventoryFragmentToNavInventoryOrderDetailFragment(room));
*/
    }



    @Override
    public void onDetailRoomCall(Notification notification) {

    }

    private void goToDetailOrder(Room room) {
        Navigation
                .findNavController(this.getView())
                .navigate(NotificationFragmentDirections
                        .actionNavNotificationFragmentToNavOperasionalFnbFragment(room));
    }
}