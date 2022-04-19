package livs.code.frontoffice.data.remote;

import livs.code.frontoffice.data.remote.respons.RoomListResponse;
import livs.code.frontoffice.data.remote.respons.RoomTypeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoomClient {

    @GET("room/all-room")
    Call<RoomListResponse> getAllRoom(@Query("keyword") String roomCode);

    @GET("room/all-room-ready")
    Call<RoomListResponse> getAllRoomReady();

    @GET("room/all-room-checkin")
    Call<RoomListResponse> getAllRoomCheckin(@Query("keyword") String roomCode);

    @GET("room/all-room-checkin-by-type")
    Call<RoomListResponse> getAllRoomCheckinByType(@Query("room_code") String roomCode,
                                                   @Query("mbr_name") String mbrName,
                                                   @Query("room_type") String roomTypeLabel,
                                                   @Query("capacity")String capacity,
                                                   @Query("page")int page);

    @GET("room/all-room-paid")
    Call<RoomListResponse> getAllRoomPaid(@Query("keyword") String roomCode);

    @GET("room/all-room-checkout")
    Call<RoomListResponse> getAllRoomCheckout(@Query("keyword") String roomCode);

    @GET("room/all-room-by-type/{room_type}/{capacity}")
    Call<RoomListResponse> getAllRoomByType(@Path("room_type") String roomTypeLabel,
                                            @Path("capacity")String capacity);

    @GET("room/all-room-ready-by-type/{room_type}/{capacity}")
    Call<RoomListResponse> getAllRoomReadyByType(@Path("room_type") String roomTypeLabel,
                                                 @Path("capacity")String capacity);

    @GET("room/all-room-ready-by-type-grouping/{room_type}")
    Call<RoomListResponse> getAllRoomReadyByTypeGrouping(@Path("room_type") String roomTypeLabel);

    @GET("room/history-checkin/{hari}")
    Call<RoomListResponse> getAllRoomHistory(@Path("hari") int hari,
                                             @Query("keyword") String roomCode);

}
