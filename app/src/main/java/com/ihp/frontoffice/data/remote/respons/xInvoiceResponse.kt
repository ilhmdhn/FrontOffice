package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class xInvoiceResponse (
        @field:SerializedName("data")
        val data: xInvoiceData? = null,

        @field:SerializedName("state")
        val state: Boolean? = null,

        @field:SerializedName("message")
        val message: String? = null
)

data class xInvoiceData(

        @field:SerializedName("transferBillData")
        val transferBillData: List<xTransferBillDataItem?>? = null,

        @field:SerializedName("cancelOrderData")
        val cancelOrderData: List<xCancelOrderDataItem?>? = null,

        @field:SerializedName("dataRoom")
        val dataRoom: xDataRoom? = null,

        @field:SerializedName("transferListData")
        val transferListData: List<xTransferListDataItem?>? = null,

        @field:SerializedName("orderData")
        val orderData: List<xOrderDataItem?>? = null,

        @field:SerializedName("promoOrderCancel")
        val promoOrderCancel: List<xPromoOrderCancelItem?>? = null,

        @field:SerializedName("promoOrderData")
        val promoOrderData: List<xPromoOrderDataItem?>? = null,

        @field:SerializedName("dataOutlet")
        val dataOutlet: xDataOutlet? = null,

        @field:SerializedName("dataInvoice")
        val dataInvoice: xDataInvoice? = null,

        @field:SerializedName("paymentDetail")
        val paymentDetail: xpaymentDetail? = null,

        @field:SerializedName("paymentData")
        val paymentList: List<xpaymentList>? = null,

        @field:SerializedName("service_percent")
        val serviceTax: taxServiceModel
)

data class xpaymentDetail(
        @field:SerializedName("pay_value")
        val payValue: Int? = null,

        @field:SerializedName("pay_change")
        val changeValue: Int? = null,
)

data class xpaymentList(
        @field:SerializedName("nama_payment")
        val payMethod: String? = null,

        @field:SerializedName("total")
        val payValue: Int? = null,
)