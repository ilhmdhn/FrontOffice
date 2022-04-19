package livs.code.frontoffice.data.remote;

import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.remote.respons.BaseResponse;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.data.remote.respons.VoucherResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentOrderClient {

    @POST("/payment/add")
    Call<RoomOrderResponse> submitPayment(@Body RoomOrder jsonRoomOrder);

    @GET("room/print-tagihan/{roomcode}")
    Call<BaseResponse> printBill(@Path("roomcode") String kodeTransaksi);

    @GET("order/pending-order/{room_code}")
    Call<BaseResponse> infoPendingOrder(@Path("room_code") String roomCode);

}
