package com.ihp.frontoffice.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ihp.frontoffice.data.remote.respons.FoodPromoResponse;
import com.ihp.frontoffice.data.repository.RemoteRepository;

public class InventoryPromoViewModel extends ViewModel {
    private MutableLiveData<FoodPromoResponse> foodPromoResponseMutableLiveData;
    private RemoteRepository remoteRepository;
    private static String TAG = "RoomViewModel";

    public void init(String baseUrl){
        if(foodPromoResponseMutableLiveData !=null){
            return;
        }
        remoteRepository = RemoteRepository.getInstance(baseUrl);
    }

    public MutableLiveData<FoodPromoResponse> getFoodPromoResponseMutableLiveData(String roomType,String roomCode) {
        foodPromoResponseMutableLiveData = remoteRepository.getAllFoodPromo(roomType,roomCode);
        return foodPromoResponseMutableLiveData;
    }
}
