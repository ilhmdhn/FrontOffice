package com.ihp.frontoffice.events

object DataBusEvent {
    data class approvalResponse(
        val user: String,
        val password: String,
        val isApprove: Boolean,
    )
}