package livs.code.frontoffice.viewmodel;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import livs.code.frontoffice.data.entity.Notification;
import livs.code.frontoffice.data.repository.LocalRepository;

public class NotificationViewModel extends ViewModel {
    private LiveData<List<Notification>> notificationMutableLiveData;
    private LocalRepository remoteRepository;
    private LiveData<Integer> unreadNotificationLiveData;

    public void init(Context application){
        if(notificationMutableLiveData !=null){
            return;
        }
        remoteRepository = LocalRepository.getInstance(application);
        notificationMutableLiveData = remoteRepository.getAllNotification();
        unreadNotificationLiveData = remoteRepository.countUnreadNotification(false);
    }

    public LiveData<List<Notification>> getNotificationMutableLiveData() {
        return notificationMutableLiveData;
    }

    public LiveData<Integer> getUnreadNotificationLiveData() {
        return unreadNotificationLiveData;
    }

    public void resetNotify(){
        remoteRepository.updateNotification();
    }
}
