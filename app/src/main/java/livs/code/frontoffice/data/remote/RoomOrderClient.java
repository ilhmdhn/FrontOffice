package livs.code.frontoffice.data.remote;


import java.util.List;

import livs.code.frontoffice.data.entity.RoomOrder;
import livs.code.frontoffice.data.remote.respons.Response;
import livs.code.frontoffice.data.remote.respons.RoomExtendResponse;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.data.remote.respons.RoomResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RoomOrderClient {

    @FormUrlEncoded
    @POST("room/checkout")
    Call<RoomOrderResponse> checkoutRoom(
            @Field("room") String room,
            @Field("chusr") String user
    );

    @FormUrlEncoded
    @POST("room/checksound")
    Call<RoomOrderResponse> checksoundRoom(
            @Field("room") String room,
            @Field("chusr") String user
    );

    @FormUrlEncoded
    @POST("room/clean")
    Call<RoomOrderResponse> cleanRoom(
            @Field("room") String room,
            @Field("chusr") String user
    );


    @Multipart
    @POST("checkin-direct/sign-image")
    Call<RoomOrderResponse> submitSignImg(@Part MultipartBody.Part img);

    @GET("checkin/checkin-result/{kamar}")
    Call<RoomOrderResponse> getDetailRoomOrder(@Path("kamar") String kata);

    @GET("checkin/checkin-history/{reception}/{kamar}")
    Call<RoomOrderResponse> getDetailHistoryRoomOrder(
            @Path("reception") String reception,
            @Path("kamar") String kata);

    @GET("room/room-extend/{kamar}")
    Call<RoomExtendResponse> getExtendInfo(@Path("kamar") String kata);

    @GET("room/room-detail/{kamar}")
    Call<RoomResponse> getRoomDetail(@Path("kamar") String kata);

    @FormUrlEncoded
    @POST("checkin-direct/extend-room")
    Call<RoomOrderResponse> submitExtendsRoom(
            @Field("room") String kodeRoom,
            @Field("durasi_jam") String durasiJam,
            @Field("durasi_menit") String durasiMenit,
            @Field("promo[]") List<String> promoSelected,
            @Field("chusr") String chUsr,
            @Field("minus") Boolean minus);

    @FormUrlEncoded
    @POST("checkin-direct/transfer-room")
    Call<RoomOrderResponse> submitTransferRoom(
            @Field("room") String kodeRoom,
            @Field("old_room") String oldKodeRoom,
            @Field("alasan_transfer") String keterangan,
            @Field("voucher") String voucher,
            @Field("chusr") String chUsr,
            @Field("qm1") int qmAnak,
            @Field("qm2") int qmRemaja,
            @Field("qm3") int qmDewasa,
            @Field("qm4") int qmTua,
            @Field("qf1") int qfAnak,
            @Field("qf2") int qfRemaja,
            @Field("qf3") int qfDewasa,
            @Field("qf4") int qfTua);

    @FormUrlEncoded
    @POST("checkin-direct/direct-checkin")
    Call<RoomOrderResponse> submitOrderRoom(
            @Field("room") String kodeRoom,
            @Field("nama_member") String namaMember,
            @Field("kode_member") String kodeMember,
            @Field("durasi_jam") String durasiJam,
            @Field("durasi_menit") String durasiMenit,
            @Field("qm1") String qmAnak,
            @Field("qm2") String qmRemaja,
            @Field("qm3") String qmDewasa,
            @Field("qm4") String qmTua,
            @Field("qf1") String qfAnak,
            @Field("qf2") String qfRemaja,
            @Field("qf3") String qfDewasa,
            @Field("qf4") String qfTua,
            @Field("hp") String hpMember,
            @Field("uang_muka") String uangMuka,
            @Field("keterangan") String keterangan,
            @Field("event_acara") String event,
            @Field("chusr") String chUsr,
            @Field("voucher") String voucher,
            @Field("keterangan_payment_uang_muka") String keteranganUangMuka,
            @Field("input1_jenis_kartu") String input1JenisKartu,
            @Field("input2_nama") String input2Nama,
            @Field("input3_nomor_kartu") String input3NomorKartu,
            @Field("input4_nomor_approval") String input4NomorApproval,
            @Field("edc_machine") String edcMachine,
            @Field("promo[]") List<String> promoSelected,
            @Field("member_reservasi_code") String memberReservasiCode);

    @FormUrlEncoded
    @POST("checkin-direct/edit-checkin")
    Call<RoomOrderResponse> submitEditOrderRoom(
            @Field("room") String kodeRoom,
            @Field("qm1") String qmAnak,
            @Field("qm2") String qmRemaja,
            @Field("qm3") String qmDewasa,
            @Field("qm4") String qmTua,
            @Field("qf1") String qfAnak,
            @Field("qf2") String qfRemaja,
            @Field("qf3") String qfDewasa,
            @Field("qf4") String qfTua,
            @Field("hp") String hpMember,
            @Field("uang_muka") String uangMuka,
            @Field("keterangan") String keterangan,
            @Field("event_acara") String event,
            @Field("event_owner") String eventOwner,
            @Field("chusr") String chUsr,
            @Field("voucher") String voucher,
            @Field("keterangan_payment_uang_muka") String keteranganUangMuka,
            @Field("input1_jenis_kartu") String input1JenisKartu,
            @Field("input2_nama") String input2Nama,
            @Field("input3_nomor_kartu") String input3NomorKartu,
            @Field("input4_nomor_approval") String input4NomorApproval,
            @Field("edc_machine") String edcMachine,
            @Field("member_ref") String codeMemberRef,
            @Field("promo[]") List<String> promoSelected);

    @POST("checkin-direct/direct-checkin-member")
    Call<RoomOrderResponse> submitOrderRoomMemberCheckin(@Body RoomOrder jsonRoomOrder);

    @POST("checkin-direct/direct-lobby-member")
    Call<RoomOrderResponse> submitOrderLobbyMemberCheckin(@Body RoomOrder jsonRoomOrder);

    @POST("checkin-direct/transfer-room-member")
    Call<RoomOrderResponse> submitTransferRoomToRoom(@Body RoomOrder jsonRoomOrder);

    @POST("checkin-direct/transfer-lobby-to-room")
    Call<RoomOrderResponse> submitTransferLobbyToRoom(@Body RoomOrder jsonRoomOrder);

    @FormUrlEncoded
    @POST("checkin-direct/reduce_duration")
    Call<Response> reduceDurationClient(
            @Field("rcp") String kodeRcp,
            @Field("durasi") String durasi,
            @Field("chusr") String chusr
    );

    //@Path("reception") String reception,
}