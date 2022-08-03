package com.ihp.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.ihp.frontoffice.data.entity.TypeEdc;

public class EdcTypeResponse extends BaseResponse{
    @SerializedName("data")
    private List<TypeEdc> typeEdcs;

    public List<TypeEdc> getTypeEdcs() {
        return typeEdcs;
    }

}
