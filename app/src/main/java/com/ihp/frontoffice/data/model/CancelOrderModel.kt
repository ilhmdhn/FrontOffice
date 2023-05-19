package com.ihp.frontoffice.data.model

import com.google.gson.annotations.SerializedName

data class CancelOrderModel(
        val inventory: String,
        val nama: String,
        val order_penjualan: String,
        val qty: String,
        val slip_order: String,
)

data class OrderInventory(
        @SerializedName("inventory") val inventory: String,
        @SerializedName("nama") val nama: String,
        @SerializedName("order_penjualan") val orderPenjualan: String,
        @SerializedName("qty") val qty: Int,
        @SerializedName("slip_order") val slipOrder: String
)

data class RequestData(
        @SerializedName("chusr") val chusr: String,
        @SerializedName("room") val room: String,
        @SerializedName("rcp_old") val rcpOld: String,
        @SerializedName("order_inventory") val orderInventory: List<OrderInventory>
)