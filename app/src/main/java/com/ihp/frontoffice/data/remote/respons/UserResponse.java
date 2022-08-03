package com.ihp.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import com.ihp.frontoffice.data.entity.User;

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
