package com.ihp.frontoffice.data.remote;

import com.ihp.frontoffice.data.remote.respons.EdcTypeResponse;
import com.ihp.frontoffice.data.remote.respons.MemberResponse;
import com.ihp.frontoffice.data.remote.respons.VoucherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MemberClient {

    @GET("/member/membership/{web_membership}")
    Call<MemberResponse> checkMember(@Path("web_membership") String kodeMbr);

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
