package com.ihp.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import com.ihp.frontoffice.data.entity.Member;


/**
 * Created by program on 24/10/2017.
 */

public class MemberResponse extends BaseResponse {

    @SerializedName("data")
    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
