package livs.code.frontoffice.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import livs.code.frontoffice.data.entity.RoomType;
import livs.code.frontoffice.data.remote.respons.RoomListResponse;
import livs.code.frontoffice.data.repository.RemoteRepository;

public class RoomViewModel extends ViewModel {
    private MutableLiveData<RoomListResponse> roomLiveData;
    private RemoteRepository remoteRepository;
    private static String TAG = "RoomViewModel";

    public void init(String baseUrl){
        if(roomLiveData !=null){
            return;
        }
        remoteRepository = RemoteRepository.getInstance(baseUrl);
        //getAllRoom("");
    }

    public LiveData<RoomListResponse> getAllRoom(String cariData) {
        roomLiveData = remoteRepository.getAllRoom(cariData);
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getAllRoomHistory(String cariData) {
        roomLiveData = remoteRepository.getAllRoomHistory(cariData);
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getRoomReady(){
        roomLiveData = remoteRepository.getAllRoomReady();
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getRoomByType(RoomType roomType){
        roomLiveData = remoteRepository.getAllRoomByType(roomType);
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getRoomReadyByTypeGrouping(RoomType roomType){
        roomLiveData = remoteRepository.getAllRoomReadyByTypeGrouping(roomType);
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getRoomReadyByType(RoomType roomType){
        roomLiveData = remoteRepository.getAllRoomReadyByType(roomType);
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getRoomCheckin(String roomCode){
        roomLiveData = remoteRepository.getAllRoomCheckin(roomCode);
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getRoomCheckinByType(String roomCode,String roomMbr,String roomType,String roomCapacity){
        roomLiveData = remoteRepository.getAllRoomCheckinByType(roomCode,roomMbr,roomType,roomCapacity);
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getRoomPaid(String roomCode){
        roomLiveData = remoteRepository.getAllRoomPaid(roomCode);
        return roomLiveData;
    }

    public LiveData<RoomListResponse> getRoomCheckout(String roomCode){
        roomLiveData = remoteRepository.getAllRoomCheckout(roomCode);
        return roomLiveData;
    }
}
