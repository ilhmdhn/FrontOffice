package com.ihp.frontoffice.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ihp.frontoffice.data.remote.respons.RoomPromoResponse;
import com.ihp.frontoffice.data.repository.RemoteRepository;

public class RoomPromoViewModel extends ViewModel {
    private MutableLiveData<RoomPromoResponse> roomPromoResponseMutableLiveData;
    private RemoteRepository remoteRepository;
    private static String TAG = "RoomViewModel";

    public void init(String baseUrl){
        if(roomPromoResponseMutableLiveData !=null){
            return;
        }
        remoteRepository = RemoteRepository.getInstance(baseUrl);
    }

    public MutableLiveData<RoomPromoResponse> getRoomPromoResponseMutableLiveData(String roomType) {
        roomPromoResponseMutableLiveData = remoteRepository.getAllRoomPromo(roomType);
        return roomPromoResponseMutableLiveData;
    }

}
