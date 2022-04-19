package livs.code.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import livs.code.frontoffice.data.entity.User;

public class UserResponse extends BaseResponse{
    @SerializedName("data")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
