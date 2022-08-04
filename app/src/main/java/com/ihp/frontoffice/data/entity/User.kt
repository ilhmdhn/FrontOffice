package com.ihp.frontoffice.data.entity

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "user")
class User{
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    var userId: String = ""

    @ColumnInfo(name = "level_user")
    @SerializedName("level_user")
    var levelUser: String? = null


    @ColumnInfo(name = "is_login")
    @SerializedName("is_login")
    var isLogin = false

}