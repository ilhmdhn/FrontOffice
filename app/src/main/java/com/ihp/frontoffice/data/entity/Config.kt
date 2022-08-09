package com.ihp.frontoffice.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class Config(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 1,

    @ColumnInfo(name = "server_ip")
    val serverIp: String,

    @ColumnInfo(name = "server_port")
    val serverPort: String,

    @ColumnInfo(name = "base_url")
    val baseURL: String,
)