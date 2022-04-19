package livs.code.frontoffice.data.remote.respons;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import es.dmoral.toasty.Toasty;


/**
 * Created by program on 22/08/2017.
 */

public class BaseResponse {
    @SerializedName("state")
    private boolean okay;

    @SerializedName("length")
    private int length;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void displayMessage(Context context){
        if(isOkay()){
            //Toasty.success(context, "Server Response : "+getMessage(), Toast.LENGTH_SHORT, true).show();
        }else{
            Toasty.warning(context, "Server Response : "+getMessage(), Toast.LENGTH_SHORT, true).show();
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isOkay() {
        return okay;
    }

    public void setOkay(boolean okay) {
        this.okay = okay;
    }

}
