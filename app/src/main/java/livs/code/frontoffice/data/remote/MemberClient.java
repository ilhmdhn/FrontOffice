package livs.code.frontoffice.data.remote;

import livs.code.frontoffice.data.remote.respons.EdcTypeResponse;
import livs.code.frontoffice.data.remote.respons.MemberResponse;
import livs.code.frontoffice.data.remote.respons.VoucherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MemberClient {

    @GET("/member/membership/{web_membership}")
    Call<MemberResponse> checkMember(@Path("web_membership") String kodeMbr);
    //http://192.168.1.111:3000/rsv/getRsv/R-416949916109593571
    @GET("rsv/getRsv/{kode_reservasi}")
    Call<MemberResponse> checkReservasi(@Path("kode_reservasi") String rsv);

    @GET("/voucher/get-voucher-web-membership/{outlet}/{voucher}")
    Call<VoucherResponse> checkVoucherMembership(@Path("outlet") String outlet,
                                             @Path("voucher") String kodeVoucher);

    @GET("/voucher/get-voucher/{voucher}")
    Call<VoucherResponse> checkVoucherOutlet(@Path("voucher") String kodeVoucher);

    @GET("/edc/list-edc")
    Call<EdcTypeResponse> getEdc();
}
