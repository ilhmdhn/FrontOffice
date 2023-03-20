package com.ihp.frontoffice.view.listadapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ihp.frontoffice.MyApp;
import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;
import com.ihp.frontoffice.data.entity.User;
import com.ihp.frontoffice.data.remote.ApiRestService;
import com.ihp.frontoffice.data.remote.UserClient;
import com.ihp.frontoffice.data.remote.respons.UserResponse;
import com.ihp.frontoffice.data.repository.IhpRepository;
import com.ihp.frontoffice.events.EventsWrapper;
import com.ihp.frontoffice.events.GlobalBus;
import com.ihp.frontoffice.helper.AppUtils;
import com.ihp.frontoffice.helper.Printer;
import com.ihp.frontoffice.helper.RoomState;
import com.ihp.frontoffice.helper.UserAuthRole;
import com.ihp.frontoffice.view.fragment.history.ListHistoryRoomFragmentDirections;
import com.ihp.frontoffice.viewmodel.OtherViewModel;

public class ListRoomHistoryAdapter extends RecyclerView.Adapter<ListRoomHistoryAdapter.RoomViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Room> roomList;
    private IhpRepository ihpRepository;

    public ListRoomHistoryAdapter(Context context, ArrayList<Room> roomArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.roomList = roomArrayList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_room, parent, false);
        return new RoomViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        StringBuilder alias = new StringBuilder();
        final RoomViewHolder roomViewHolder = (RoomViewHolder) holder;
        roomViewHolder.setGone();
        roomViewHolder.setRoom(room);
        if (room.getAlias() != null){
            alias.append(" ("+room.getAlias()+")");
        }
        roomViewHolder._roomKode.setText(room.getRoomCode() + alias);
        roomViewHolder._roomTipe.setText(room.getRoomType());
        roomViewHolder._checkinTime.setText(AppUtils.getTanggal(room.getRoomCheckinHours()));
        roomViewHolder._checkoutTime.setText(AppUtils.getTanggal(room.getRoomCheckoutHours()));
        if(room.getMemberName()!=null){
            roomViewHolder._roomMember.setText(room.getMemberName());
        }
        else{
            roomViewHolder._roomMember.setText(room.getRoomGuessName());
        }

        if (roomViewHolder.getRoom().isRoomNotLobby()) {

            if (RoomState.READY.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._bttnCheckin.setVisibility(View.VISIBLE);
                roomViewHolder._bttnCheckin.setText("Check In");

            } else if (RoomState.CHECKIN.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._layoutTime.setVisibility(View.VISIBLE);
                roomViewHolder._bttnDetail.setVisibility(View.VISIBLE);
            } else if (RoomState.CLAIMED.getState() == roomViewHolder.getRoom().getRoomState()) {

            } else if (RoomState.PAID.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._layoutTime.setVisibility(View.VISIBLE);
                roomViewHolder._bttnCheckout.setVisibility(View.VISIBLE);
                roomViewHolder.btnReprintInvoice.setVisibility(View.VISIBLE);
            } else if ((RoomState.CHECKOUT_REPAIRED.getState() == roomViewHolder.getRoom().getRoomState()) ||
                    (RoomState.CHECKSOUND.getState() == roomViewHolder.getRoom().getRoomState())) {
                roomViewHolder._bttnClean.setVisibility(View.VISIBLE);
                roomViewHolder.btnReprintInvoice.setVisibility(View.VISIBLE);
            } else if (RoomState.HISTORY.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._layoutTime.setVisibility(View.VISIBLE);
                roomViewHolder._bttnHistory.setVisibility(View.VISIBLE);
                roomViewHolder.btnReprintInvoice.setVisibility(View.VISIBLE);
            }

        } else {
            roomViewHolder._layoutTime.setVisibility(View.GONE);
            roomViewHolder._bttnOrder.setVisibility(View.VISIBLE);

            if (RoomState.READY.getState() == roomViewHolder.getRoom().getRoomState()) {
            } else if (RoomState.CHECKSOUND.getState() == roomViewHolder.getRoom().getRoomState()) {
            } else if (RoomState.CHECKIN.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._bttnPayment.setVisibility(View.VISIBLE);
            } else if (RoomState.CLAIMED.getState() == roomViewHolder.getRoom().getRoomState()) {
            } else if (RoomState.PAID.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._bttnCheckout.setVisibility(View.VISIBLE);
            } else if (RoomState.CHECKOUT_REPAIRED.getState() == roomViewHolder.getRoom().getRoomState()) {
            } else if (RoomState.HISTORY.getState() == roomViewHolder.getRoom().getRoomState()) {
                roomViewHolder._bttnHistory.setVisibility(View.VISIBLE);
            }
        }

        // roomViewHolder._checkinDurasi.setText(room.getRoomDurasiCheckin());
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


    class RoomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_room_code)
        TextView _roomKode;

        @BindView(R.id.row_room_type)
        TextView _roomTipe;

        @BindView(R.id.row_jam_checkin)
        TextView _checkinTime;

        @BindView(R.id.row_jam_checkout)
        TextView _checkoutTime;

        @BindView(R.id.row_room_member)
        TextView _roomMember;

        @BindView(R.id.checkin_time_layout)
        LinearLayout _layoutTime;

        @BindView(R.id.bttn_checkin)
        Button _bttnCheckin;

        @BindView(R.id.bttn_order)
        Button _bttnOrder;


        @BindView(R.id.bttn_extends)
        Button _bttnExtend;

        @BindView(R.id.bttn_transfer)
        Button _bttnTransfer;

        @BindView(R.id.bttn_payment)
        Button _bttnPayment;

        @BindView(R.id.bttn_checkout)
        Button _bttnCheckout;

        @BindView(R.id.bttn_clean)
        Button _bttnClean;

        @BindView(R.id.bttn_detail)
        Button _bttnDetail;

        @BindView(R.id.bttn_history)
        Button _bttnHistory;

        @BindView(R.id.btn_print_invoice)
        Button btnReprintInvoice;


        private Context context;
        private Room room;
        private final OtherViewModel otherViewModel;

        private final Printer printer;

        private RoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();
            otherViewModel = new ViewModelProvider((ViewModelStoreOwner) itemView.getContext()).get(OtherViewModel.class);
            ihpRepository = new IhpRepository();

            printer = new Printer();

            _bttnCheckin.setVisibility(View.GONE);
            _bttnOrder.setVisibility(View.GONE);
            _bttnCheckout.setVisibility(View.GONE);
            _bttnClean.setVisibility(View.GONE);
            _bttnExtend.setVisibility(View.GONE);
            _bttnPayment.setVisibility(View.GONE);
            _bttnTransfer.setVisibility(View.GONE);
            _bttnDetail.setVisibility(View.GONE);
            _bttnHistory.setVisibility(View.GONE);
            _layoutTime.setVisibility(View.GONE);

            _bttnCheckin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (room.isRoomNotLobby()) {
                       /* Navigation
                                .findNavController(view)
                                .navigate(ListRoomFragmentDirections
                                .actionNavRoomToCheckinRoomFragment(room));
                                */
                    } else {

                    }
                }
            });

            _bttnTransfer.setOnClickListener(view -> {
                /*Navigation
                        .findNavController(view)
                        .navigate(ListRoomFragmentDirections.actionNavListRoomToTransferRoomFragment(room));*/
            });

            _bttnExtend.setOnClickListener(view -> {
                /*Navigation
                        .findNavController(view)
                        .navigate(ListRoomFragmentDirections.actionNavRoomToExtendsRoomFragment(room));*/

            });

            _bttnPayment.setOnClickListener(view -> {
                /*Navigation
                        .findNavController(view)
                        .navigate(ListRoomFragmentDirections
                                .actionNavRoomToPaymentInvoiceFragment(room));*/
            });

            _bttnDetail.setOnClickListener(view -> {
                Navigation
                        .findNavController(view)
                        .navigate(ListHistoryRoomFragmentDirections
                                .actionNavListHistoryRoomFragmentToNavDetailHistoryRoomFragment(room));

            });

            _bttnHistory.setOnClickListener(view -> {
                Navigation
                        .findNavController(view)
                        .navigate(ListHistoryRoomFragmentDirections
                                .actionNavListHistoryRoomFragmentToNavDetailHistoryRoomFragment(room));
            });

            _bttnCheckout.setOnClickListener(view -> {
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper.CheckoutRoom(this.room));
            });
            _bttnClean.setOnClickListener(view -> {
                GlobalBus
                        .getBus()
                        .post(new EventsWrapper.CleanRoom(this.room));
            });

//            btnReprintInvoice.setOnClickListener(view ->{
//                Log.d("isinya apa ajaa", "");
//                context = itemView.getContext();
//                String BASE_URL = ((MyApp) itemView.getContext().getApplicationContext()).baseUrl;
//                String user = ((MyApp) itemView.getContext().getApplicationContext()).getUserFo().getUserId();
//                otherViewModel.getInvoiceData(BASE_URL, room.getRoomRcp()).observe((LifecycleOwner) itemView.getContext(), data->{
//                    if (Boolean.TRUE.equals(data.getState())){
//                        Objects.requireNonNull(Objects.requireNonNull(data.getData()).getDataInvoice()).getStatusPrint();
//                        if (Objects.equals(data.getData().getDataInvoice().getStatusPrint(), "2")){
//
//                            AlertDialog.Builder builder;
//                            builder = new AlertDialog.Builder(context);
//
//                            builder.setCancelable(true)
//                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    dialogInterface.dismiss();
//                                                }
//                                            })
//                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            if (printer.printInvoice(data, context,user, true)){
//                                                ihpRepository.updateStatusPrint(BASE_URL, room.getRoomRcp(), "1", context);
//                                            }
//                                        }
//                                    });
//                            AlertDialog alert = builder.create();
//                            alert.setMessage("Cetak Copy Tagihan?");
//                            alert.show();
//                        }else if(Objects.equals(data.getData().getDataInvoice().getStatusPrint(), "1")){
//                            otherViewModel.getJumlahApproval(BASE_URL, user).observe((LifecycleOwner) context, dataApproval ->{
//                                if (dataApproval.getState()){
//                                    AlertDialog.Builder builder;
//                                    builder = new AlertDialog.Builder(context);
//                                    builder.setCancelable(true)
//                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    dialogInterface.dismiss();
//                                                }
//                                            })
//                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    if(printer.printInvoice(data, context, user, true)){
//                                                        ihpRepository.submitApproval(BASE_URL, user, user, room.getRoomCode(), "Reprint Invoice");
//                                                    }
//                                                }
//                                            });
//                                    AlertDialog alert = builder.create();
//                                    alert.setMessage("Cetak Ulang Tagihan?1");
//                                    alert.show();
//                                }else{
//
//                                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme);
//                                    LayoutInflater dialogInflater = LayoutInflater.from(context);
//                                    View dialogView = dialogInflater.inflate(R.layout.dialog_otorisasi, null);
//                                    dialogBuilder.setView(dialogView);
//                                    dialogBuilder.setCancelable(false);
//                                    AppCompatButton buttonOk = dialogView.findViewById(R.id.btn_ok);
//                                    AppCompatButton buttonCancel = dialogView.findViewById(R.id.btn_cancel);
//
//                                    EditText _usernameTxt = dialogView.findViewById(R.id.input_username_otorisasi);
//                                    EditText _passwordTxt = dialogView.findViewById(R.id.input_password_otorisasi);
//                                    AlertDialog alertDialog = dialogBuilder.create();
//
//                                    alertDialog.setOnShowListener(dialogInterface -> {
//                                        buttonOk.setOnClickListener(it -> {
//                                            String email = _usernameTxt.getText().toString();
//                                            String password = _passwordTxt.getText().toString();
//                                            if (email.isEmpty() && password.isEmpty()) {
//                                                Toasty.warning(context, "Anda belum input user dan password ", Toast.LENGTH_SHORT, true)
//                                                        .show();
//                                                return;
//                                            }
//                                            UserClient userClient = ApiRestService.getClient(BASE_URL).create(UserClient.class);
//                                            Call<UserResponse> call = userClient.login(email, password);
//                                            //---------------------
//
//                                            call.enqueue(new Callback<UserResponse>() {
//                                                @Override
//                                                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                                                    UserResponse res = response.body();
//                                                    //_loginProgress.setVisibility(View.GONE);
//                                                    res.displayMessage(context);
//                                                    if (res.isOkay()) {
//                                                        User userCek = res.getUser();
//                                                        if (UserAuthRole.isAllowReprintInvoice(userCek)) {
//                                                            if(printer.printInvoice(data, context,user, true)){
//                                                                ihpRepository.submitApproval(BASE_URL, user, user, room.getRoomCode(), "Reprint Invoice");
//                                                            }
//                                                        } else {
//                                                            Toasty.warning(context, "User tidak dapat melakukan operasi ini", Toast.LENGTH_SHORT, true)
//                                                                    .show();
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<UserResponse> call, Throwable t) {
//                                                    //_loginProgress.setVisibility(View.GONE);
//                                                    Toasty.error(context, "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT, true)
//                                                            .show();
//                                                }
//                                            });
//                                            alertDialog.dismiss();
//                                            //---------------------
//                                        });
//                                        buttonCancel.setOnClickListener(it -> {
//                                            alertDialog.dismiss();
//                                        });
//                                    });
//                                    alertDialog.show();
//                                }
//                            });
//                        }else{
//                            Toasty.warning(context, "Silahkan cetak melalui POS Lorong Desktop", Toasty.LENGTH_SHORT, true).show();
//                        }
//
//                    }else{
//                        Toast.makeText(itemView.getContext(), data.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            });
        }

        public void setGone() {
            _bttnCheckin.setVisibility(View.GONE);
            _bttnOrder.setVisibility(View.GONE);
            _bttnCheckout.setVisibility(View.GONE);
            _bttnExtend.setVisibility(View.GONE);
            _bttnPayment.setVisibility(View.GONE);
            _bttnTransfer.setVisibility(View.GONE);
            _bttnDetail.setVisibility(View.GONE);
            _bttnHistory.setVisibility(View.GONE);
            _layoutTime.setVisibility(View.GONE);
        }

        public void setRoom(Room room) {
            this.room = room;
        }

        public Room getRoom() {
            return room;
        }
    }
}
