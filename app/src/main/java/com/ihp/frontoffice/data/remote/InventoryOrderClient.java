package com.ihp.frontoffice.data.remote;

import com.ihp.frontoffice.data.entity.RoomOrder;
import com.ihp.frontoffice.data.remote.respons.RoomOrderResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InventoryOrderClient {

    @POST("cancelorder/add")
    Call<RoomOrderResponse> submitCancelOrderInventory(@Body RoomOrder jsonRoomInventoryOrder);

    @POST("neworder/add")
    Call<RoomOrderResponse> submitDeliveryOrderInventory(@Body RoomOrder jsonRoomInventoryOrder);
}
