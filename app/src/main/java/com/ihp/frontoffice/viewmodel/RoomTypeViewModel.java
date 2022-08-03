package com.ihp.frontoffice.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ihp.frontoffice.data.remote.respons.RoomTypeResponse;
import com.ihp.frontoffice.data.repository.RemoteRepository;

public class RoomTypeViewModel extends ViewModel {
    private MutableLiveData<RoomTypeResponse> roomTypeLiveData;
    private MutableLiveData<RoomTypeResponse> groupingRoomTypeReadyLiveData;
    private MutableLiveData<RoomTypeResponse> roomTypeReadyLiveData;
    private RemoteRepository remoteRepository;
    private static String TAG = "RoomTypeViewModel";

    public void init(String baseUrl){
        if(roomTypeLiveData !=null){
            return;
        }
        remoteRepository = RemoteRepository.getInstance(baseUrl);
        roomTypeLiveData = remoteRepository.getAllRoomType();
        groupingRoomTypeReadyLiveData = remoteRepository.getGroupingRoomTypeReady();
        roomTypeReadyLiveData = remoteRepository.getRoomTypeReady();
    }

    public MutableLiveData<RoomTypeResponse> getRoomTypeLiveData() {
        return roomTypeLiveData;
    }

    public MutableLiveData<RoomTypeResponse> getGroupingRoomTypeReadyLiveData() {
        groupingRoomTypeReadyLiveData = remoteRepository.getGroupingRoomTypeReady();
        return groupingRoomTypeReadyLiveData;
    }

    public MutableLiveData<RoomTypeResponse> getRoomTypeReadyLiveData() {
        roomTypeReadyLiveData = remoteRepository.getRoomTypeReady();
        return roomTypeReadyLiveData;
    }
}
