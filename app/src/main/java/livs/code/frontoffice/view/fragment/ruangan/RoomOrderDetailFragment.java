package livs.code.frontoffice.view.fragment.ruangan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.view.fragment.roomdetail.RoomOrderExtendsFragment;
import livs.code.frontoffice.view.fragment.roomdetail.RoomOrderFragment;
import livs.code.frontoffice.view.fragment.roomdetail.RoomOrderInventoryCancelFragment;
import livs.code.frontoffice.view.fragment.roomdetail.RoomOrderInventoryFragment;
import livs.code.frontoffice.view.fragment.roomdetail.RoomOrderInventoryProgressFragment;
import livs.code.frontoffice.view.fragment.roomdetail.RoomOrderInvoiceFragment;
import livs.code.frontoffice.view.fragment.roomdetail.RoomOrderPromoInventoryFragment;
import livs.code.frontoffice.view.fragment.roomdetail.RoomOrderPromoRoomFragment;
import livs.code.frontoffice.viewmodel.RoomOrderViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomOrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomOrderDetailFragment extends Fragment {

    @BindView(R.id.room_order_detail_progressbar)
    MKLoader progressBar;

    @BindView(R.id.room_order_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.room_order_view_pager)
    ViewPager2 viewPager2;

    private RoomOrderViewModel roomOrderViewModel;
    private Room room;
    private RoomOrder roomOrder;

    private String[] titles = new String[]{"Room", "Room Extends", "Order FnB", "Cancel FnB", "Progress FnB", "Promo FnB", "Promo Room", "Invoice"};
    private static String BASE_URL;


    public RoomOrderDetailFragment() {
        // Required empty public constructor
    }



    public static RoomOrderDetailFragment newInstance(String param1, String param2) {
        RoomOrderDetailFragment fragment = new RoomOrderDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_room_order_detail, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
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
                        .TitleFragment(room.getRoomType()+" "+room.getRoomCode()));
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

    private void setViewPager() {
        roomOrder.setCheckinRoom(room);
        viewPager2.setAdapter(new ViewPagerFragmentAdapter(getActivity()));
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(titles[position]))
                .attach();
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
                    return RoomOrderFragment.newInstance(roomOrder);
                case 1:
                    return RoomOrderExtendsFragment.newInstance(roomOrder);
                case 2:
                    return RoomOrderInventoryFragment.newInstance(roomOrder);
                case 3:
                    return RoomOrderInventoryCancelFragment.newInstance(roomOrder);
                case 4:
                    return RoomOrderInventoryProgressFragment.newInstance(roomOrder);
                case 5:
                    return RoomOrderPromoInventoryFragment.newInstance(roomOrder);
                case 6:
                    return RoomOrderPromoRoomFragment.newInstance(roomOrder);
                case 7:
                    return RoomOrderInvoiceFragment.newInstance(roomOrder);
            }
            return new Fragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }

}