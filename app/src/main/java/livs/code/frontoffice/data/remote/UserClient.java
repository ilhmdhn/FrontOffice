package livs.code.frontoffice.data.remote;

import livs.code.frontoffice.data.remote.respons.UserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserClient {

    @FormUrlEncoded
    @POST("user/login-fo-droid")
    Call<UserResponse> login(
            @Field("user_id") String userId,
            @Field("user_password") String userPassword);
}
