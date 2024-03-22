package com.ihp.frontoffice.data.model
data class ServiceTaxModel(
    val roomServicePercent: Int,
    val roomTaxPercent: Int,
    val fnbServicePercent: Int,
    val fnbTaxPercent: Int,
    val roomServiceValue: Int,
    val roomTaxValue: Int,
    val fnbServiceValue: Int,
    val fnbTaxValue: Int,
    val amount: Int,
    val otherCharge: StringBuilder,
    val transfer: StringBuilder,
    val downPayment: StringBuilder,
    val total: Int,
    val netAmount: Int
)