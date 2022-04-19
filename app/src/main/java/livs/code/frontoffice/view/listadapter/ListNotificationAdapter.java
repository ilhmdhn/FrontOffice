package livs.code.frontoffice.view.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Notification;
import livs.code.frontoffice.data.entity.Payment;

public class ListNotificationAdapter extends RecyclerView.Adapter<ListNotificationAdapter.NotificationViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Notification> notificationList;
    private OnDetailPageListener onDetailPageListener;

    public ListNotificationAdapter(Context context,OnDetailPageListener onDetailPageListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.onDetailPageListener = onDetailPageListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.holder_view_notify, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification ntf = notificationList.get(position);
        final NotificationViewHolder viewHolder = (NotificationViewHolder) holder;
        viewHolder.setNotification(ntf);
        String roomAndType = ntf.getRoomType()+" "+ntf.getRoomCode();
        viewHolder.roomAndType.setText(roomAndType);
        viewHolder.timeNotif.setText(ntf.getCreateUser());
        String typeMessage = "";
        if(ntf.getNotifType().equals("NEW_ORDER")){
            typeMessage="ORDER F&B";
        }else if(ntf.getNotifType().equals("ROOM_CALL")){
             typeMessage = "ROOM " + ntf.getRoomCode() + " MEMANGGIL";
        }
        viewHolder.typeNotif.setText(typeMessage);

        if(ntf.getNotifType().equals("NEW_ORDER")){
            viewHolder.detailButton.setOnClickListener(view -> {
                if(ntf.getNotifType().equals("NEW_ORDER")){
                    onDetailPageListener.onDetailPageOrder(ntf);
                }else if(ntf.getNotifType().equals("ROOM_CALL")){
                    onDetailPageListener.onDetailRoomCall(ntf);
                }
            });
        }else if(ntf.getNotifType().equals("ROOM_CALL")){
            viewHolder.detailButton.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        if (notificationList != null)
            return notificationList.size();
        else return 0;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_notif)
        ImageView imageNotif;

        @BindView(R.id.room_type)
        TextView roomAndType;

        @BindView(R.id.bttn_detail)
        ImageButton detailButton;

        @BindView(R.id.time_notif)
        TextView timeNotif;

        @BindView(R.id.type_notif)
        TextView typeNotif;

        private Notification notification;

        private NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

        public void setNotification(Notification notification) {
            this.notification = notification;
        }
    }

    public interface OnDetailPageListener {
        void onDetailPageOrder(Notification notification);
        void onDetailRoomCall(Notification notification);
    }


}
