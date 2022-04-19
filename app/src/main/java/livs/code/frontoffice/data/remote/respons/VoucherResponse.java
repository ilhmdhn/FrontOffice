package livs.code.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import livs.code.frontoffice.data.entity.Voucher;


/**
 * Created by program on 24/10/2017.
 */

public class VoucherResponse extends BaseResponse {

    @SerializedName("data")
    private Voucher voucher;

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }
}
