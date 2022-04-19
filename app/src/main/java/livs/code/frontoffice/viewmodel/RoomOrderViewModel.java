package livs.code.frontoffice.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.data.repository.RemoteRepository;

public class RoomOrderViewModel extends ViewModel {
    private MutableLiveData<RoomOrderResponse> roomOrderResponseMutableLiveData;
    private RemoteRepository remoteRepository;
    private static String TAG = "RoomViewModel";

    public void init(String baseUrl){
        if(roomOrderResponseMutableLiveData !=null){
            return;
        }
        remoteRepository = RemoteRepository.getInstance(baseUrl);
    }

    public MutableLiveData<RoomOrderResponse> getRoomOrderResponseMutableLiveData(String room) {
        roomOrderResponseMutableLiveData = remoteRepository.getRoomOrder(room);
        return roomOrderResponseMutableLiveData;
    }

    public MutableLiveData<RoomOrderResponse> getHistoryRoomOrderResponseMutableLiveData(String reception,String room) {
        roomOrderResponseMutableLiveData = remoteRepository.getHistoryRoomOrder(reception,room);
        return roomOrderResponseMutableLiveData;
    }
}
