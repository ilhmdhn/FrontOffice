package livs.code.frontoffice.data.remote;

import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InventoryOrderClient {

    @POST("cancelorder/add")
    Call<RoomOrderResponse> submitCancelOrderInventory(@Body RoomOrder jsonRoomInventoryOrder);

    @POST("neworder/add")
    Call<RoomOrderResponse> submitDeliveryOrderInventory(@Body RoomOrder jsonRoomInventoryOrder);
}
