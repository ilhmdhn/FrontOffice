package com.ihp.frontoffice.view.listadapter;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Inventory;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.helper.InventoryState;
import com.ihp.frontoffice.helper.RoomState;

public class ListRoomStatusAdapter extends RecyclerView.Adapter<ListRoomStatusAdapter.RoomViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Room> roomList;

    public ListRoomStatusAdapter(Context context, ArrayList<Room> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_room_status, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        final RoomViewHolder vh = holder;

        vh.setRoom(room);
        vh._roomCode.setText(room.getRoomCode());

        if(room.getEventOwner().length()<1){
            vh._roomGuess.setText("Incognito");
        } else{
            vh._roomGuess.setText(room.getEventOwner());
        }

        if ((room.getInventoryOnOrderProgress().size() > 1) && (RoomState.PAID.getState() != room.getRoomState())) {
//            vh._middleLayout.setVisibility(View.INVISIBLE);
                vh._paidState.setVisibility(View.INVISIBLE);
                vh._orderState.setVisibility(View.GONE);
        } else {
            if (room.getInventoryOnOrderProgress().size() > 1) {
                vh._orderState.setVisibility(View.GONE);
                vh.lyStatusORder.setVisibility(View.VISIBLE);
            } else {
                vh._orderState.setVisibility(View.VISIBLE);
                vh.lyStatusORder.setVisibility(View.GONE);
            }
            if (RoomState.PAID.getState() == room.getRoomState()) {
                vh._paidState.setVisibility(View.VISIBLE);
            } else {
                vh._paidState.setVisibility(View.INVISIBLE);
            }
        }

        vh.startTimer(room.getRoomResidualCheckinHoursTime(), room.getRoomResidualCheckinHoursMinutesTime());
        vh.countOrder();
        vh.setRoomCall();

    }

    public void setRooms(List<Room> rooms) {
        roomList = rooms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (roomList != null)
            return roomList.size();
        else return 0;
    }


    class RoomViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_room_so_count)
        TextView _countAllSO;

        @BindView(R.id.tv_room_order_process)
        TextView _countAllAcceptedSO;

        @BindView(R.id.tv_room_order_return)
        TextView _countAllCancelSO;

        @BindView(R.id.tv_room_order_success)
        TextView _countAllSuccessSO;

        @BindView(R.id.tv_room_code)
        TextView _roomCode;

        @BindView(R.id.tv_room_guest_name)
        TextView _roomGuess;

        @BindView(R.id.tv_room_order_empty)
        TextView _orderState;

        @BindView(R.id.ly_room_order_status)
        LinearLayout lyStatusORder;

        @BindView(R.id.tv_room_paid)
        TextView _paidState;

        @BindView(R.id.tv_room_duration)
        TextView _time;

//        @BindView(R.id.middle_layout)
//        View _middleLayout;

        @BindView(R.id.tv_room_call)
        TextView _callRoom;


        private Context context;
        private Room room;
        private CountDownTimer mCountDownTimer;
        private boolean mTimerRunning;
        private static final long START_TIME_IN_MILLIS = 900000;//600000 = 10 menit
        private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

        private RoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();
            itemView.setOnClickListener(this::onClick);
        }

        private void startTimer(int hours, int minute) {
            //long milliseconds = hours * 60 * 60 *1000;
            long hoursToMillis = TimeUnit.HOURS.toMillis(hours);
            long minuteToMillis = TimeUnit.MINUTES.toMillis(minute);
            mTimeLeftInMillis = hoursToMillis + minuteToMillis;

            int minuteTotal = (int) TimeUnit.MILLISECONDS.toMinutes(mTimeLeftInMillis);
            if (minuteTotal < 10) {
                _time.setTextColor(Color.RED);
                if (minuteTotal < 0) {
                    _time.setText("00:00");
                } else {
                    _time.setText(hmsTimeFormatter(mTimeLeftInMillis));
                }

            } else {
                _time.setText(hmsTimeFormatter(mTimeLeftInMillis));
            }

               /* mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int minuteTotal = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                        if (minuteTotal < 10) {
                            _time.setTextColor(Color.RED);
                            if (minuteTotal < 0) {
                                _time.setText("00:00:00");
                            } else {
                                _time.setText(hmsTimeFormatter(millisUntilFinished));
                            }

                        } else {
                            _time.setText(hmsTimeFormatter(millisUntilFinished));
                        }


                    }

                    @Override
                    public void onFinish() {
                        mTimerRunning = false;
                    }
                }.start();
                mTimerRunning = true;*/


        }

        private String hmsTimeFormatter(long milliSeconds) {

           /* String hms = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(milliSeconds),
                    TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                    TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds))
                    );*/

            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(milliSeconds),
                    TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds))
            );


            return hms;


        }


        public void setRoom(Room room) {
            this.room = room;
        }

        public Room getRoom() {
            return room;
        }


        @Override
        public void onClick(View view) {
//           Navigation
//                    .findNavController(view)
//                    .navigate(RoomStatusFragmentDirections
//                            .actionNavRoomStatusFragmentToNavRoomOrderStatusDetailFragment(room));
        }

        public void setRoomCall() {
            if (room.isRoomCall()) {
                _callRoom.setVisibility(View.VISIBLE);
            } else {
                _callRoom.setVisibility(View.INVISIBLE);
            }
        }

        public void countOrder() {
            int sumQtySo = countQtySlipOrder(room.getInventoryOnOrderProgress());
            _countAllSO.setText(String.valueOf(sumQtySo));

            int sumQtyProgressOrder = countQtyProgressOrder(room.getInventoryOnOrderProgress());
            _countAllAcceptedSO.setText(String.valueOf(sumQtyProgressOrder));

            int sumQtyFinishOrder = countQtyFinishOrder(room.getSummaryOrderInventories());
            _countAllSuccessSO.setText(String.valueOf(sumQtyFinishOrder));

          /*  //revisi
            _countAllAcceptedSO.setText(String.valueOf(sumQtyProgressOrder-sumQtyFinishOrder));*/

            int sumQtyCancelOrder = countQtyCancelOrder(room.getInventoryCancelation());
            _countAllCancelSO.setText(String.valueOf(sumQtyCancelOrder));
        }

        private int countQtySlipOrder(List<Inventory> inventoryOnOrderProgress) {
            ArrayList<Inventory> filterOrder = (ArrayList<Inventory>) inventoryOnOrderProgress.stream()
                    .filter(inventory ->
                            (inventory.getInventoryState() == InventoryState.ORDER_SEND_TO_FO_OR_KITCHEN.getState()))
                    .collect(Collectors.toList());
            if (filterOrder.size() > 0) {
                int sum = 0;
                for (Inventory data : filterOrder) {
                    sum = sum + data.getQty();
                }
                return sum;
            }

            return 0;
        }

        private int countQtyProgressOrder(List<Inventory> inventoryOnOrderProgress) {
            ArrayList<Inventory> filterOrder = (ArrayList<Inventory>) inventoryOnOrderProgress.stream()
                    .filter(inventory ->
                            (inventory.getInventoryState() == InventoryState.ORDER_ACCEPT_BY_FO_OR_KITCHEN.getState()))
                    .collect(Collectors.toList());
            if (filterOrder.size() > 0) {
                int sum = 0;
                for (Inventory data : filterOrder) {
                    sum = sum + data.getUnfinishedAcceptOrderQty();
                }
                return sum;
            }

            return 0;
        }

        private int countQtyFinishOrder(List<Inventory> filterOrder) {
            if (filterOrder.size() > 0) {
                int sum = 0;
                for (Inventory data : filterOrder) {
                    sum = sum + data.getQty();
                }
                return sum;
            }
            return 0;
        }

        private int countQtyCancelOrder(List<Inventory> filterOrder) {
            if (filterOrder.size() > 0) {
                int sum = 0;
                for (Inventory data : filterOrder) {
                    sum = sum + data.getQty();
                }
                return sum;
            }
            return 0;
        }
    }
}
